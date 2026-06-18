"""
InstApp/views.py
Vistas del módulo Instructor con todas las correcciones y mejoras.
"""
import io
from types import SimpleNamespace
from datetime import datetime
from django.contrib import messages
from django.shortcuts import redirect, render, get_object_or_404
from django.utils import timezone
from django.db import IntegrityError
from django.db.models import Q
from django.views.decorators.http import require_POST
from django.views.decorators.cache import never_cache

from LoginApp.models import (
    Ambiente, Aprendiz, Ficha, RegistroInasistencia, RegistroMinuta,
    TrasladoRecurso, RegistroIncidente, TipoIncidente, Instructor, Usuario,
    Programas, Jornada, HistoricoIncidentes
)
from .export_utils import (
    generar_pdf_response, construir_pdf,
    generar_excel_response, estilizar_excel, guardar_excel_en_response,
)
from LoginApp.decorators import rol_requerido


def _fecha_hoy():
    return timezone.localdate().isoformat()


NIVELES_GRAVEDAD_INCIDENTE = [opcion for opcion, _ in RegistroIncidente.NIVELES_GRAVEDAD]


# ─────────────────────────────────────────────────────────────────
#  INICIO / INDEX
# ─────────────────────────────────────────────────────────────────

@never_cache
@rol_requerido(['instructor'])
def instructor_index(request):
    return render(request, "instindex.html")


@never_cache
@rol_requerido(['instructor'])
def inicio_instructor(request):
    return instructor_index(request)


@never_cache
@rol_requerido(['instructor'])
def index_instructor(request):
    return instructor_index(request)


# ─────────────────────────────────────────────────────────────────
#  FICHAS
# ─────────────────────────────────────────────────────────────────

@never_cache
@rol_requerido(['instructor'])
def mis_fichas(request):
    uid = request.session.get('usuario_id')
    if not uid:
        messages.warning(request, "Debes iniciar sesión.")
        return redirect('login')

    instructor = get_object_or_404(Instructor, usuario_id_usuario_id=uid)

    fichas_qs = Ficha.objects.filter(
        instructores=instructor
    )

    # Filtro multicriterio
    q = request.GET.get('q', '').strip()
    if q:
        fichas_qs = fichas_qs.filter(num_ficha__icontains=q)

    fichas = []
    for f in fichas_qs:
        aprendices_count = Aprendiz.objects.filter(ficha_idficha=f).count()
        fichas.append({
            "idficha": f.idficha,
            "num_ficha": f.num_ficha,
            "aprendices_count": aprendices_count,
        })

    # Exportación
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("mis_fichas.pdf")
        cabeceras = ["ID Ficha", "Número de Ficha", "Aprendices"]
        filas = [[str(f["idficha"]), str(f["num_ficha"]), str(f["aprendices_count"])] for f in fichas]
        construir_pdf(buffer, "Mis Fichas Asignadas", cabeceras, filas, "vertical")
        response.write(buffer.getvalue())
        return response

    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("mis_fichas.xlsx")
        cabeceras = ["ID Ficha", "Número de Ficha", "Aprendices"]
        filas = [[str(f["idficha"]), str(f["num_ficha"]), str(f["aprendices_count"])] for f in fichas]
        estilizar_excel(ws, cabeceras, filas, "Mis Fichas Asignadas")
        return guardar_excel_en_response(response, wb)

    return render(request, "misFichas.html", {"fichas": fichas, "q": q})


@never_cache
def ver_aprendices(request, ficha_id):
    ficha = get_object_or_404(Ficha, pk=ficha_id)

    aprendices_qs = Aprendiz.objects.filter(
        ficha_idficha=ficha
    ).select_related(
        'usuario_id_usuario',
        'programas_id_programas',
    )

    aprendices = []
    for ap in aprendices_qs:
        u = ap.usuario_id_usuario
        nombre_completo = " ".join(filter(None, [
            u.p_nombre, u.s_nombre, u.p_apellido, u.s_apellido
        ])).strip()
        programa = ap.programas_id_programas.nombre_programa if ap.programas_id_programas else "—"
        # Análisis de estado basado en inasistencias (N)
        fallas = RegistroInasistencia.objects.filter(
            aprendiz_usuario_id_usuario=ap,
            estado_inasistencia='N'
        ).count()
        estado_analisis = "Al día" if fallas < 3 else "En Riesgo"
        
        aprendices.append({
            "id": u.id_usuario,
            "nombre_completo": nombre_completo,
            "programa": programa,
            "num_documento": u.num_documento,
            "correo": u.correo,
            "estado_analisis": estado_analisis,
            "fallas": fallas
        })

    # Exportación
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response(f"aprendices_ficha_{ficha_id}.pdf")
        cabeceras = ["ID", "Nombre Completo", "Programa", "Documento"]
        filas = [[str(a["id"]), a["nombre_completo"], a["programa"], str(a["num_documento"])] for a in aprendices]
        construir_pdf(buffer, f"Aprendices - Ficha {ficha.num_ficha}", cabeceras, filas)
        response.write(buffer.getvalue())
        return response

    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response(f"aprendices_ficha_{ficha_id}.xlsx")
        cabeceras = ["ID", "Nombre Completo", "Programa", "Documento", "Correo"]
        filas = [[str(a["id"]), a["nombre_completo"], a["programa"], str(a["num_documento"]), a["correo"]] for a in aprendices]
        estilizar_excel(ws, cabeceras, filas, f"Aprendices - Ficha {ficha.num_ficha}")
        return guardar_excel_en_response(response, wb)

    return render(request, "aprendicesFicha.html", {
        "ficha": ficha,
        "aprendices": aprendices,
    })


# ─────────────────────────────────────────────────────────────────
#  ASISTENCIAS
# ─────────────────────────────────────────────────────────────────

@never_cache
@rol_requerido(['instructor'])
def listar_asistencias_instructor(request):
    uid = request.session.get('usuario_id')
    if not uid:
        messages.warning(request, "Debes iniciar sesión.")
        return redirect('login')

    instructor = get_object_or_404(Instructor, usuario_id_usuario_id=uid)

    # Obtenemos las fichas asignadas al instructor
    fichas_asignadas = Ficha.objects.filter(instructores=instructor)
    
    # Filtramos las asistencias:
    # 1. Donde el aprendiz pertenece a una de mis fichas
    #    O
    # 2. Donde yo soy el responsable de haber registrado la asistencia
    qs = RegistroInasistencia.objects.filter(
        Q(aprendiz_usuario_id_usuario__ficha_idficha__in=fichas_asignadas) |
        Q(instructor_usuario_id_usuario=instructor)
    ).distinct().select_related(
        'aprendiz_usuario_id_usuario__usuario_id_usuario',
        'jornada',
        'instructor_usuario_id_usuario__usuario_id_usuario',
    ).order_by('-fecha_inasistencia')

    # Filtros multicriterio
    fecha_desde = request.GET.get('fecha_desde', '').strip()
    fecha_hasta = request.GET.get('fecha_hasta', '').strip()
    estado = request.GET.get('estado', '').strip()
    q = request.GET.get('q', '').strip()

    if fecha_desde:
        qs = qs.filter(fecha_inasistencia__gte=fecha_desde)
    if fecha_hasta:
        qs = qs.filter(fecha_inasistencia__lte=fecha_hasta)
    if estado:
        qs = qs.filter(estado_inasistencia=estado)
    if q:
        qs = qs.filter(
            Q(aprendiz_usuario_id_usuario__usuario_id_usuario__p_nombre__icontains=q) |
            Q(aprendiz_usuario_id_usuario__usuario_id_usuario__p_apellido__icontains=q)
        )

    asistencias = list(qs)

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("asistencias.pdf")
        cabeceras = ["ID", "Aprendiz", "Fecha", "Estado", "Jornada"]
        filas = []
        for a in asistencias:
            ap_u = a.aprendiz_usuario_id_usuario.usuario_id_usuario
            nombre_ap = f"{ap_u.p_nombre} {ap_u.p_apellido}"
            estado_lbl = {"S": "Asistió", "R": "Retraso", "N": "No asistió"}.get(
                a.estado_inasistencia, a.estado_inasistencia
            )
            filas.append([
                str(a.id_inasistencia),
                nombre_ap,
                str(a.fecha_inasistencia),
                estado_lbl,
                a.jornada.nombre_jornada if a.jornada else "—",
            ])
        construir_pdf(buffer, "Control de Asistencia", cabeceras, filas)
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("asistencias.xlsx")
        cabeceras = ["ID", "Aprendiz", "Fecha", "Estado", "Jornada"]
        filas = []
        for a in asistencias:
            ap_u = a.aprendiz_usuario_id_usuario.usuario_id_usuario
            nombre_ap = f"{ap_u.p_nombre} {ap_u.p_apellido}"
            estado_lbl = {"S": "Asistió", "R": "Retraso", "N": "No asistió"}.get(
                a.estado_inasistencia, a.estado_inasistencia
            )
            filas.append([
                str(a.id_inasistencia),
                nombre_ap,
                str(a.fecha_inasistencia),
                estado_lbl,
                a.jornada.nombre_jornada if a.jornada else "—",
            ])
        estilizar_excel(ws, cabeceras, filas, "Control de Asistencia")
        return guardar_excel_en_response(response, wb)

    filtros = {
        "fecha_desde": fecha_desde,
        "fecha_hasta": fecha_hasta,
        "estado": estado,
        "q": q,
    }
    return render(request, "asistencias/listarAsistenciasIns.html", {
        "asistencias": asistencias,
        "filtros": filtros,
    })


def listar_asistencia(request):
    return listar_asistencias_instructor(request)


@never_cache
def registrar_asistencia(request):
    uid = request.session.get('usuario_id')
    instructor = get_object_or_404(Instructor, usuario_id_usuario_id=uid)
    
    if request.method == "POST":
        try:
            aprendiz_id = request.POST.get("aprendiz_id")
            fecha = request.POST.get("fecha")
            estado = request.POST.get("estado")
            jornada_id = request.POST.get("jornada_id")
            instructor_id = request.POST.get("instructor_id") or instructor.pk
            
            # Validación de fecha (Solo hoy)
            fecha_dt = datetime.strptime(fecha, "%Y-%m-%d").date()
            hoy = timezone.localdate()
            if fecha_dt != hoy:
                messages.error(request, f"Error: Solo se permite registrar asistencia con la fecha de hoy ({hoy}).")
                return redirect('instructor:listar_asistencias_instructor')

            RegistroInasistencia.objects.create(
                aprendiz_usuario_id_usuario_id=int(aprendiz_id),
                instructor_usuario_id_usuario_id=int(instructor_id),
                fecha_inasistencia=fecha_dt,
                estado_inasistencia=estado,
                jornada_id=int(jornada_id),
            )
            messages.success(request, "Asistencia registrada correctamente.")
            return redirect('instructor:listar_asistencias_instructor')
        except (TypeError, ValueError, IntegrityError) as e:
            messages.error(request, f"No se pudo registrar la asistencia: {str(e)}")

    # Solo mostrar los aprendices de las fichas asignadas a este instructor
    fichas_del_instructor = Ficha.objects.filter(instructores=instructor)
    aprendices_del_instructor = Aprendiz.objects.filter(
        ficha_idficha__in=fichas_del_instructor
    ).select_related('usuario_id_usuario').order_by(
        'usuario_id_usuario__p_apellido', 'usuario_id_usuario__p_nombre'
    )

    return render(request, "asistencias/formAsistencia.html", {
        "aprendices": aprendices_del_instructor,
        "instructores": Instructor.objects.select_related('usuario_id_usuario').all(),
        "jornadas": Jornada.objects.all(),
        "hoy": _fecha_hoy(),
        "instructor": instructor,
    })


@never_cache
def editar_asistencia(request, asistencia_id):
    asistencia = get_object_or_404(RegistroInasistencia, pk=asistencia_id)
    if request.method == "POST":
        # Misma validación "solo hoy"
        fecha = request.POST.get("fecha")
        fecha_dt = datetime.strptime(fecha, "%Y-%m-%d").date()
        hoy = timezone.localdate()
        if fecha_dt != hoy:
            messages.error(request, "Solo se permite editar asistencias con fecha de hoy.")
            return redirect('instructor:listar_asistencias_instructor')
            
        try:
            asistencia.aprendiz_usuario_id_usuario_id = int(request.POST.get("aprendiz_id"))
            asistencia.instructor_usuario_id_usuario_id = int(request.POST.get("instructor_id"))
            asistencia.jornada_id = int(request.POST.get("jornada_id"))
            asistencia.estado_inasistencia = request.POST.get("estado")
            asistencia.fecha_inasistencia = fecha_dt
            asistencia.save()
            messages.success(request, "Asistencia actualizada.")
            return redirect('instructor:listar_asistencias_instructor')
        except (TypeError, ValueError, IntegrityError) as e:
            messages.error(request, f"No se pudo actualizar la asistencia: {str(e)}")
        
    uid = request.session.get('usuario_id')
    instructor_actual = get_object_or_404(Instructor, usuario_id_usuario_id=uid)

    # Solo los aprendices de las fichas de este instructor
    fichas_del_instructor = Ficha.objects.filter(instructores=instructor_actual)
    aprendices_del_instructor = Aprendiz.objects.filter(
        ficha_idficha__in=fichas_del_instructor
    ).select_related('usuario_id_usuario').order_by(
        'usuario_id_usuario__p_apellido', 'usuario_id_usuario__p_nombre'
    )

    return render(request, "asistencias/editarAsistencia.html", {
        "asistencia": asistencia,
        "aprendices": aprendices_del_instructor,
        "instructores": Instructor.objects.select_related('usuario_id_usuario').all(),
        "jornadas": Jornada.objects.all(),
        "hoy": _fecha_hoy(),
        "instructor_actual": instructor_actual,
    })


def eliminar_asistencia(request, asistencia_id):
    asistencia = get_object_or_404(RegistroInasistencia, pk=asistencia_id)
    # Validar que sea de hoy para permitir eliminar
    if asistencia.fecha_inasistencia != timezone.localdate():
        messages.error(request, "No se pueden eliminar registros de asistencias pasadas.")
        return redirect('instructor:listar_asistencias_instructor')
        
    asistencia.delete()
    messages.success(request, "Asistencia eliminada.")
    return redirect('instructor:listar_asistencias_instructor')


# Aliases de exportación para asistencias (usados por URLs existentes)
def exportar_pdf(request):
    return listar_asistencias_instructor(request)


def exportar_excel(request):
    return listar_asistencias_instructor(request)


# ─────────────────────────────────────────────────────────────────
#  MINUTAS
# ─────────────────────────────────────────────────────────────────

@never_cache
@rol_requerido(['instructor'])
def listar_minutas(request):
    from GuardaApp.views.utils import _liberar_minutas_vencidas
    _liberar_minutas_vencidas()
    qs = RegistroMinuta.objects.select_related(
        'ambiente',
        'responsable__usuario_id_usuario',
        'guarda_seguridad_usuario_id_usuario__usuario_id_usuario',
    ).order_by('-fecha_hora_recibo')

    # Filtros multicriterio
    q = request.GET.get('q', '').strip()
    estado = request.GET.get('estado', '').strip()
    fecha_desde = request.GET.get('fecha_desde', '').strip()
    fecha_hasta = request.GET.get('fecha_hasta', '').strip()
    ambiente_q = request.GET.get('ambiente', '').strip()

    if q:
        qs = qs.filter(
            Q(responsable__usuario_id_usuario__p_nombre__icontains=q) |
            Q(responsable__usuario_id_usuario__p_apellido__icontains=q) |
            Q(estado__icontains=q)
        )
    if estado:
        qs = qs.filter(estado__icontains=estado)
    if fecha_desde:
        qs = qs.filter(fecha_hora_recibo__date__gte=fecha_desde)
    if fecha_hasta:
        qs = qs.filter(fecha_hora_recibo__date__lte=fecha_hasta)
    if ambiente_q:
        try:
            qs = qs.filter(ambiente__num_ambiente=int(ambiente_q))
        except ValueError:
            pass

    minutas = list(qs)

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("minutas.pdf")
        cabeceras = ["ID", "Ambiente", "Guarda", "Instructor", "F. Recibo", "F. Entrega", "Estado"]
        filas = []
        for m in minutas:
            gu = m.guarda_seguridad_usuario_id_usuario.usuario_id_usuario
            ins_u = m.responsable.usuario_id_usuario
            filas.append([
                str(m.id_minuta),
                f"Amb. {m.ambiente.num_ambiente}" if m.ambiente else "—",
                f"{gu.p_nombre} {gu.p_apellido}",
                f"{ins_u.p_nombre} {ins_u.p_apellido}",
                m.fecha_hora_recibo.strftime("%d/%m/%Y %H:%M") if m.fecha_hora_recibo else "—",
                m.fecha_hora_entrega.strftime("%d/%m/%Y %H:%M") if m.fecha_hora_entrega else "—",
                m.estado or "—",
            ])
        construir_pdf(buffer, "Registro de Minutas", cabeceras, filas)
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("minutas.xlsx")
        cabeceras = ["ID", "Ambiente", "Guarda", "Instructor", "F. Recibo", "F. Entrega", "Estado"]
        filas = []
        for m in minutas:
            gu = m.guarda_seguridad_usuario_id_usuario.usuario_id_usuario
            ins_u = m.responsable.usuario_id_usuario
            filas.append([
                str(m.id_minuta),
                f"Amb. {m.ambiente.num_ambiente}" if m.ambiente else "—",
                f"{gu.p_nombre} {gu.p_apellido}",
                f"{ins_u.p_nombre} {ins_u.p_apellido}",
                m.fecha_hora_recibo.strftime("%d/%m/%Y %H:%M") if m.fecha_hora_recibo else "—",
                m.fecha_hora_entrega.strftime("%d/%m/%Y %H:%M") if m.fecha_hora_entrega else "—",
                m.estado or "—",
            ])
        estilizar_excel(ws, cabeceras, filas, "Registro de Minutas")
        return guardar_excel_en_response(response, wb)

    filtros = {
        "q": q, "estado": estado,
        "fecha_desde": fecha_desde, "fecha_hasta": fecha_hasta,
        "ambiente": ambiente_q,
    }
    return render(request, "consultarMinutas.html", {
        "minutas": minutas,
        "filtros": filtros,
    })


def consultar_minutas(request):
    return listar_minutas(request)


def exportar_minutas_pdf(request):
    # Delegado a listar_minutas con param forzado
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_minutas(request)


def exportar_minutas_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_minutas(request)


# ─────────────────────────────────────────────────────────────────
#  INCIDENTES
# ─────────────────────────────────────────────────────────────────

@never_cache
@rol_requerido(['instructor'])
def listar_incidentes(request):
    qs = RegistroIncidente.objects.select_related(
        "ambiente", "tipo_inc", "usuario_id_usuario"
    ).order_by("-fecha_incidente", "-hora_incidente")

    # Filtros multicriterio
    q = request.GET.get('q', '').strip()
    tipo_id = request.GET.get('tipo', '').strip()
    gravedad = request.GET.get('gravedad', '').strip()
    ambiente_id = request.GET.get('ambiente', '').strip()
    fecha_desde = request.GET.get('fecha_desde', '').strip()
    fecha_hasta = request.GET.get('fecha_hasta', '').strip()

    if q:
        qs = qs.filter(
            Q(descripcion__icontains=q) |
            Q(usuario_id_usuario__p_nombre__icontains=q) |
            Q(usuario_id_usuario__p_apellido__icontains=q)
        )
    if tipo_id:
        qs = qs.filter(tipo_inc_id=tipo_id)
    if gravedad:
        qs = qs.filter(nivel_gravedad__iexact=gravedad)
    if ambiente_id:
        qs = qs.filter(ambiente_id=ambiente_id)
    if fecha_desde:
        qs = qs.filter(fecha_incidente__gte=fecha_desde)
    if fecha_hasta:
        qs = qs.filter(fecha_incidente__lte=fecha_hasta)

    incidentes = list(qs)
    ambientes_list = Ambiente.objects.all().order_by("num_ambiente")
    tipos_list = TipoIncidente.objects.all().order_by("tipo_incidente")

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("incidentes.pdf")
        cabeceras = ["ID", "Descripción", "Fecha", "Hora", "Ambiente", "Tipo", "Gravedad", "Reportado por"]
        filas = [[
            str(i.id_incidente),
            (i.descripcion or "—")[:60],
            str(i.fecha_incidente),
            str(i.hora_incidente),
            f"Amb. {i.ambiente.num_ambiente}" if i.ambiente else "—",
            i.tipo_inc.tipo_incidente if i.tipo_inc else "—",
            i.nivel_gravedad or "—",
            f"{i.usuario_id_usuario.p_nombre} {i.usuario_id_usuario.p_apellido}",
        ] for i in incidentes]
        construir_pdf(buffer, "Registro de Incidentes", cabeceras, filas)
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("incidentes.xlsx")
        cabeceras = ["ID", "Descripción", "Fecha", "Hora", "Ambiente", "Tipo", "Gravedad", "Reportado por"]
        filas = [[
            str(i.id_incidente),
            i.descripcion or "—",
            str(i.fecha_incidente),
            str(i.hora_incidente),
            f"Amb. {i.ambiente.num_ambiente}" if i.ambiente else "—",
            i.tipo_inc.tipo_incidente if i.tipo_inc else "—",
            i.nivel_gravedad or "—",
            f"{i.usuario_id_usuario.p_nombre} {i.usuario_id_usuario.p_apellido}",
        ] for i in incidentes]
        estilizar_excel(ws, cabeceras, filas, "Registro de Incidentes")
        return guardar_excel_en_response(response, wb)

    filtros = {
        "q": q, "tipo": tipo_id, "ambiente": ambiente_id,
        "gravedad": gravedad, "fecha_desde": fecha_desde, "fecha_hasta": fecha_hasta,
    }
    return render(request, "listarIncidentes.html", {
        "incidentes": incidentes,
        "ambientes_list": ambientes_list,
        "tipos_list": tipos_list,
        "niveles_gravedad": NIVELES_GRAVEDAD_INCIDENTE,
        "filtros": filtros,
    })


def crear_incidente(request):
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    tipos = TipoIncidente.objects.all().order_by("tipo_incidente")
    uid = request.session.get('usuario_id')
    usuario = get_object_or_404(Usuario, pk=uid)

    if request.method == "POST":
        nivel_gravedad = (request.POST.get("nivel_gravedad") or "").strip().title()
        try:
            if nivel_gravedad not in NIVELES_GRAVEDAD_INCIDENTE:
                raise ValueError("nivel_gravedad")

            # Auto-rellenar fecha y hora actual (no editables desde el formulario)
            ahora = timezone.localtime()
            ambiente_id = int(request.POST.get("ambiente_id"))

            incidente = RegistroIncidente.objects.create(
                descripcion=(request.POST.get("descripcion") or "").strip() or None,
                fecha_incidente=ahora.date(),
                hora_incidente=ahora.time(),
                ambiente_id=ambiente_id,
                tipo_inc_id=int(request.POST.get("tipo_inc_id")),
                nivel_gravedad=nivel_gravedad,
                usuario_id_usuario=usuario,
                estado='Abierto',
            )

            # Notificar por email a la coordinacion del instructor asignado al ambiente
            try:
                from LoginApp.models import AsignacionAmbiente
                from django.core.mail import send_mail
                from django.conf import settings as django_settings

                asignacion = AsignacionAmbiente.objects.select_related(
                    'instructor__coordinacion_id_coordinacion',
                    'instructor__usuario_id_usuario',
                ).filter(
                    ambiente_id=ambiente_id,
                    estado='ACTIVO',
                ).first()

                if asignacion and asignacion.instructor.coordinacion_id_coordinacion:
                    coord = asignacion.instructor.coordinacion_id_coordinacion
                    correo_destino = coord.correo_coordinacion
                    instr = asignacion.instructor.usuario_id_usuario
                    asunto = f"[SENA] Nuevo incidente registrado – Ambiente {incidente.ambiente.num_ambiente}"
                    cuerpo = (
                        f"Se ha registrado un nuevo incidente:\n\n"
                        f"ID: {incidente.id_incidente}\n"
                        f"Fecha: {incidente.fecha_incidente.strftime('%d/%m/%Y')}\n"
                        f"Hora: {incidente.hora_incidente.strftime('%H:%M')}\n"
                        f"Ambiente: {incidente.ambiente.num_ambiente}\n"
                        f"Tipo: {incidente.tipo_inc.tipo_incidente}\n"
                        f"Gravedad: {incidente.nivel_gravedad}\n"
                        f"Instructor asignado: {instr.p_nombre} {instr.p_apellido}\n"
                        f"Registrado por: {usuario.p_nombre} {usuario.p_apellido}\n\n"
                        f"Descripción:\n{incidente.descripcion or 'Sin descripción'}\n"
                    )
                    send_mail(asunto, cuerpo,
                              getattr(django_settings, 'DEFAULT_FROM_EMAIL', 'no-reply@sena.edu.co'),
                              [correo_destino],
                              fail_silently=False)
            except Exception:
                pass  # El email no bloquea el flujo principal

            messages.success(request, "Incidente registrado correctamente.")
            return redirect('instructor:listar_incidentes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo crear el incidente. Revise los campos.")

    context = {
        "ambientes": ambientes,
        "tipos": tipos,
        "modo_edicion": False,
        "hoy": _fecha_hoy(),
        "hora_ahora": timezone.localtime().strftime("%H:%M"),
        "niveles_gravedad": NIVELES_GRAVEDAD_INCIDENTE,
        "user_logged_in": usuario,
    }
    return render(request, "formIncidente.html", context)



def form_incidente(request):
    return crear_incidente(request)


def editar_incidente(request, incidente_id):
    incidente = get_object_or_404(RegistroIncidente, pk=incidente_id)
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    tipos = TipoIncidente.objects.all().order_by("tipo_incidente")

    if request.method == "POST":
        nivel_gravedad = (request.POST.get("nivel_gravedad") or "").strip().title()
        try:
            if nivel_gravedad not in NIVELES_GRAVEDAD_INCIDENTE:
                raise ValueError("nivel_gravedad")
            incidente.descripcion = (request.POST.get("descripcion") or "").strip() or None
            incidente.fecha_incidente = request.POST.get("fecha_incidente")
            incidente.hora_incidente = request.POST.get("hora_incidente")
            incidente.ambiente_id = int(request.POST.get("ambiente_id"))
            incidente.tipo_inc_id = int(request.POST.get("tipo_inc_id"))
            incidente.nivel_gravedad = nivel_gravedad
            incidente.save()
            messages.success(request, "Incidente actualizado correctamente.")
            return redirect('instructor:listar_incidentes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo actualizar el incidente.")

    context = {"incidente": incidente, "ambientes": ambientes, "tipos": tipos, "modo_edicion": True, "hoy": _fecha_hoy(), "niveles_gravedad": NIVELES_GRAVEDAD_INCIDENTE}
    return render(request, "formIncidente.html", context)


@require_POST
def eliminar_incidente(request, incidente_id):
    incidente = get_object_or_404(RegistroIncidente, pk=incidente_id)
    # Eliminamos el historial relacionado para evitar IntegrityError (Constraint FK)
    HistoricoIncidentes.objects.filter(incidente=incidente).delete()
    incidente.delete()
    messages.success(request, "Incidente eliminado correctamente.")
    return redirect('instructor:listar_incidentes')


def exportar_incidentes_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_incidentes(request)


def exportar_incidentes_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_incidentes(request)


# ─────────────────────────────────────────────────────────────────
#  TRASLADOS
# ─────────────────────────────────────────────────────────────────

@never_cache
def listar_traslados(request):
    qs = TrasladoRecurso.objects.select_related(
        "recurso",
        "ambiente_origen",
        "instructor_origen__usuario_id_usuario",
        "instructor_destino__usuario_id_usuario",
    ).order_by("-fecha_traslado")

    # Filtros multicriterio
    q = request.GET.get('q', '').strip()
    fecha_desde = request.GET.get('fecha_desde', '').strip()
    fecha_hasta = request.GET.get('fecha_hasta', '').strip()
    amb_origen = request.GET.get('amb_origen', '').strip()

    if q:
        qs = qs.filter(
            Q(recurso__nombre_recurso__icontains=q) |
            Q(observacion__icontains=q)
        )
    if fecha_desde:
        qs = qs.filter(fecha_traslado__date__gte=fecha_desde)
    if fecha_hasta:
        qs = qs.filter(fecha_traslado__date__lte=fecha_hasta)
    if amb_origen:
        try:
            qs = qs.filter(ambiente_origen__num_ambiente=int(amb_origen))
        except ValueError:
            pass

    # Resolver ambiente_destino (IntegerField → num_ambiente)
    destinos_ids = list(set(t.ambiente_destino for t in qs if t.ambiente_destino))
    destinos_map = {
        a.id_ambiente: a.num_ambiente
        for a in Ambiente.objects.filter(id_ambiente__in=destinos_ids)
    }

    traslados = []
    for t in qs:
        instructor_origen = f"{t.instructor_origen.usuario_id_usuario.p_nombre} {t.instructor_origen.usuario_id_usuario.p_apellido}".strip() if t.instructor_origen else "—"
        instructor_destino = f"{t.instructor_destino.usuario_id_usuario.p_nombre} {t.instructor_destino.usuario_id_usuario.p_apellido}".strip() if t.instructor_destino else "—"
        traslados.append({
            "id_traslado": t.id_traslado,
            "recurso_nombre": t.recurso.nombre_recurso if t.recurso else "—",
            "recurso_serial": t.recurso.serial_recurso if t.recurso else "—",
            "ambiente_origen_num": t.ambiente_origen.num_ambiente if t.ambiente_origen else "—",
            "ambiente_destino_num": destinos_map.get(t.ambiente_destino, f"Amb. {t.ambiente_destino}"),
            "fecha_traslado": t.fecha_traslado,
            "observacion": t.observacion or "—",
            "instructor_origen_nombre": instructor_origen,
            "instructor_destino_nombre": instructor_destino,
            "tiempo_prestamo": t.tiempo_prestamo or "—",
        })

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("traslados.pdf")
        cabeceras = ["ID", "Recurso", "Origen", "Destino", "Fecha", "Instructor Presta", "Instructor Recibe", "Duración", "Observación"]
        filas = [[
            str(t["id_traslado"]),
            t["recurso_nombre"],
            str(t["ambiente_origen_num"]),
            str(t["ambiente_destino_num"]),
            t["fecha_traslado"].strftime("%d/%m/%Y") if t["fecha_traslado"] else "—",
            t["instructor_origen_nombre"],
            t["instructor_destino_nombre"],
            t["tiempo_prestamo"],
            (t["observacion"] or "—")[:80],
        ] for t in traslados]
        construir_pdf(buffer, "Traslados de Recursos", cabeceras, filas)
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("traslados.xlsx")
        cabeceras = ["ID", "Recurso", "Serial", "Ambiente Origen", "Ambiente Destino", "Fecha", "Instructor Presta", "Instructor Recibe", "Duración", "Observación"]
        filas = [[
            str(t["id_traslado"]),
            t["recurso_nombre"],
            t["recurso_serial"],
            str(t["ambiente_origen_num"]),
            str(t["ambiente_destino_num"]),
            t["fecha_traslado"].strftime("%d/%m/%Y %H:%M") if t["fecha_traslado"] else "—",
            t["instructor_origen_nombre"],
            t["instructor_destino_nombre"],
            t["tiempo_prestamo"],
            t["observacion"] or "—",
        ] for t in traslados]
        estilizar_excel(ws, cabeceras, filas, "Traslados de Recursos")
        return guardar_excel_en_response(response, wb)

    filtros = {
        "q": q,
        "fecha_desde": fecha_desde,
        "fecha_hasta": fecha_hasta,
        "amb_origen": amb_origen,
    }
    return render(request, "traslados/listarTraslados.html", {
        "traslados": traslados,
        "filtros": filtros,
        "modo_lectura": True,
    })


def form_traslado(request):
    return redirect('instructor:listar_traslados')


def editar_traslado(request, traslado_id):
    return redirect('instructor:listar_traslados')


def eliminar_traslado(request, traslado_id):
    return redirect('instructor:listar_traslados')


def exportar_traslados_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_traslados(request)


def exportar_traslados_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_traslados(request)


# ─────────────────────────────────────────────────────────────────
#  AMBIENTES
# ─────────────────────────────────────────────────────────────────

@never_cache
def listar_ambientes(request):
    from GuardaApp.views.utils import _liberar_minutas_vencidas
    _liberar_minutas_vencidas()
    qs = Ambiente.objects.all().order_by("num_ambiente")

    # Filtros multicriterio
    q = request.GET.get('q', '').strip()
    tipo = request.GET.get('tipo', '').strip()
    estado = request.GET.get('estado', '').strip()

    if q:
        try:
            qs = qs.filter(num_ambiente=int(q))
        except ValueError:
            qs = qs.filter(Q(tipo_ambiente__icontains=q) | Q(estado__icontains=q))
    if tipo:
        qs = qs.filter(tipo_ambiente__icontains=tipo)
    if estado:
        qs = qs.filter(estado__icontains=estado)

    from django.utils import timezone
    from LoginApp.models import AsignacionAmbiente, Ficha, Aprendiz, RegistroMinuta
    hoy = timezone.localdate()
    ahora = timezone.localtime()

    ambientes_obj = list(qs)
    for a in ambientes_obj:
        # Sincronizar estado dinámicamente según minutas activas
        esta_ocupado = RegistroMinuta.objects.filter(
            ambiente=a,
            fecha_hora_recibo__lte=ahora,
            fecha_hora_entrega__gte=ahora,
            estado="Ocupado"
        ).exists()
        nuevo_estado = "Ocupado" if esta_ocupado else "Disponible"
        if a.estado != nuevo_estado and a.estado in ("Ocupado", "Disponible"):
            a.estado = nuevo_estado
            a.save(update_fields=['estado'])

        asignaciones = AsignacionAmbiente.objects.filter(
            ambiente=a,
            fecha_inicio__lte=hoy,
            fecha_fin__gte=hoy,
            estado='ACTIVO'
        ).select_related('instructor__usuario_id_usuario')

        a.instructor_asignado = "Sin asignar"
        a.num_aprendices = 0
        if asignaciones.exists():
            nombres = []
            total_apren = 0
            for asignacion in asignaciones:
                instr_u = asignacion.instructor.usuario_id_usuario
                nombres.append(f"{instr_u.p_nombre} {instr_u.p_apellido}".strip())
                fichas = Ficha.objects.filter(instructores=asignacion.instructor)
                total_apren += Aprendiz.objects.filter(ficha_idficha__in=fichas).count()
            a.instructor_asignado = ", ".join(nombres)
            a.num_aprendices = total_apren

    total_ambientes = len(ambientes_obj)
    total_disponibles = sum(1 for a in ambientes_obj if (a.estado or "").lower() == "disponible")
    total_ocupados = sum(1 for a in ambientes_obj if (a.estado or "").lower() == "ocupado")

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("ambientes.pdf")
        cabeceras = ["ID", "Número", "Capacidad", "Tipo", "Estado", "Instructor", "Aprendices"]
        filas = [[
            str(a.id_ambiente),
            str(a.num_ambiente),
            str(a.capacidad),
            a.tipo_ambiente or "—",
            a.estado or "—",
            a.instructor_asignado,
            str(a.num_aprendices),
        ] for a in ambientes_obj]
        construir_pdf(buffer, "Ambientes de Formación", cabeceras, filas, "horizontal")
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("ambientes.xlsx")
        cabeceras = ["ID", "Número", "Capacidad", "Tipo", "Estado", "Instructor", "Aprendices"]
        filas = [[
            str(a.id_ambiente),
            str(a.num_ambiente),
            str(a.capacidad),
            a.tipo_ambiente or "—",
            a.estado or "—",
            a.instructor_asignado,
            str(a.num_aprendices),
        ] for a in ambientes_obj]
        estilizar_excel(ws, cabeceras, filas, "Ambientes de Formación")
        return guardar_excel_en_response(response, wb)


    filtros = {"q": q, "tipo": tipo, "estado": estado}
    context = {
        "ambientes": ambientes_obj,
        "total_disponibles": total_disponibles,
        "total_ocupados": total_ocupados,
        "total_ambientes": total_ambientes,
        "filtros": filtros,
    }
    return render(request, "ambientes.html", context)


def consultar_ambientes(request):
    return listar_ambientes(request)


def exportar_ambientes_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_ambientes(request)


def exportar_ambientes_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_ambientes(request)


# ─────────────────────────────────────────────────────────────────
#  PERFIL INSTRUCTOR
# ─────────────────────────────────────────────────────────────────

@never_cache
def perfil(request):
    uid = request.session.get('usuario_id')
    if not uid:
        return redirect('login')

    usuario = get_object_or_404(Usuario, pk=uid)
    usuario_perfil = {
        "id": usuario.id_usuario,
        "nombre_completo": f"{usuario.p_nombre} {usuario.s_nombre or ''} {usuario.p_apellido} {usuario.s_apellido or ''}".strip(),
        "p_nombre": usuario.p_nombre,
        "s_nombre": usuario.s_nombre,
        "p_apellido": usuario.p_apellido,
        "s_apellido": usuario.s_apellido,
        "tipo_documento": usuario.tipo_documento,
        "num_documento": usuario.num_documento,
        "correo": usuario.correo,
        "roles": "Instructor",
    }
    return render(request, "perfilInstructor.html", {"usuario_perfil": usuario_perfil})


@never_cache
def editar_perfil(request):
    uid = request.session.get('usuario_id')
    if not uid:
        messages.error(request, "Sesión no válida")
        return redirect('login')

    usuario = get_object_or_404(Usuario, pk=uid)

    if request.method == 'POST':
        usuario.p_nombre = request.POST.get('PNombre')
        usuario.s_nombre = request.POST.get('SNombre')
        usuario.p_apellido = request.POST.get('PApellido')
        usuario.s_apellido = request.POST.get('SApellido')
        usuario.tipo_documento = request.POST.get('tipoDocumento')
        usuario.correo = request.POST.get('correo')

        password = request.POST.get('pass')
        if password:
            usuario.set_password(password)

        usuario.save()
        messages.success(request, "Perfil actualizado correctamente.")
        return redirect('instructor:perfil')

    return render(request, 'editarPerfilInstructor.html', {'usuario': usuario})


# ─────────────────────────────────────────────────────────────────
#  JUSTIFICACIONES (flujo de aprobación para instructores)
# ─────────────────────────────────────────────────────────────────

@never_cache
@rol_requerido(['instructor'])
def listar_justificaciones(request):
    """
    Muestra todas las justificaciones pendientes de los aprendices
    asignados a las fichas del instructor logueado.
    """
    from LoginApp.models import Justificacion
    uid = request.session.get('usuario_id')
    instructor = get_object_or_404(Instructor, usuario_id_usuario_id=uid)

    # Fichas del instructor
    fichas = Ficha.objects.filter(instructores=instructor)
    # Aprendices de esas fichas
    aprendices_ids = Aprendiz.objects.filter(
        ficha_idficha__in=fichas
    ).values_list('usuario_id_usuario_id', flat=True)

    estado_filtro = request.GET.get('estado', '').strip()
    qs = Justificacion.objects.select_related(
        'inasistencia__aprendiz_usuario_id_usuario__usuario_id_usuario',
        'inasistencia',
    ).filter(
        inasistencia__aprendiz_usuario_id_usuario__usuario_id_usuario_id__in=aprendices_ids
    ).order_by('-fecha_creacion')

    if estado_filtro:
        qs = qs.filter(estado=estado_filtro)

    return render(request, 'justificaciones/listarJustificaciones.html', {
        'justificaciones': qs,
        'estado_filtro': estado_filtro,
    })


@never_cache
@rol_requerido(['instructor'])
def resolver_justificacion(request, justificacion_id):
    """
    Permite al instructor aprobar o rechazar una justificación.
    Si se aprueba, actualiza el tipo_inasistencia a 'Justificada'.
    """
    from LoginApp.models import Justificacion
    justificacion = get_object_or_404(Justificacion, pk=justificacion_id)

    if request.method == 'POST':
        accion = request.POST.get('accion', '').strip()
        observacion = (request.POST.get('observacion') or '').strip()

        if accion not in ('Aprobada', 'Rechazada'):
            messages.error(request, 'Acción no válida.')
            return redirect('instructor:listar_justificaciones')

        justificacion.estado = accion
        justificacion.observacion = observacion or None
        justificacion.save()

        if accion == 'Aprobada':
            # Actualizar la inasistencia
            inasistencia = justificacion.inasistencia
            inasistencia.tipo_inasistencia = 'Justificada'
            inasistencia.estado_inasistencia = 'J'  # Justificada
            inasistencia.save(update_fields=['tipo_inasistencia', 'estado_inasistencia'])

            # Notificar al aprendiz — APROBADA
            try:
                from django.core.mail import EmailMessage
                from django.conf import settings as dj_settings
                aprendiz_usuario = inasistencia.aprendiz_usuario_id_usuario.usuario_id_usuario
                nombre_aprendiz = f"{aprendiz_usuario.p_nombre} {aprendiz_usuario.p_apellido}".strip()
                fecha_str = inasistencia.fecha_inasistencia.strftime("%d/%m/%Y")
                cuerpo = (
                    f"Hola {nombre_aprendiz},\n\n"
                    f"Tu justificacion para la inasistencia del {fecha_str} "
                    f"ha sido APROBADA por tu instructor.\n\n"
                    f"Observacion: {observacion or 'Sin observaciones adicionales.'}\n\n"
                    f"- Sistema L-MACK SENA"
                )
                EmailMessage(
                    subject="[L-MACK] Tu justificacion fue aprobada",
                    body=cuerpo,
                    from_email=getattr(dj_settings, 'DEFAULT_FROM_EMAIL', 'no-reply@sena.edu.co'),
                    to=[aprendiz_usuario.correo],
                ).send(fail_silently=False)
            except Exception as e:
                import logging
                logging.getLogger(__name__).warning(f"No se pudo notificar aprobacion al aprendiz: {e}")

            messages.success(request, 'Justificacion aprobada e inasistencia actualizada.')
        else:
            # Notificar al aprendiz — RECHAZADA
            try:
                from django.core.mail import EmailMessage
                from django.conf import settings as dj_settings
                inasistencia = justificacion.inasistencia
                aprendiz_usuario = inasistencia.aprendiz_usuario_id_usuario.usuario_id_usuario
                nombre_aprendiz = f"{aprendiz_usuario.p_nombre} {aprendiz_usuario.p_apellido}".strip()
                fecha_str = inasistencia.fecha_inasistencia.strftime("%d/%m/%Y")
                cuerpo = (
                    f"Hola {nombre_aprendiz},\n\n"
                    f"Tu justificacion para la inasistencia del {fecha_str} "
                    f"ha sido RECHAZADA por tu instructor.\n\n"
                    f"Motivo: {observacion or 'No se especifico un motivo.'}\n\n"
                    f"Si tienes dudas, comunicate directamente con tu instructor.\n\n"
                    f"- Sistema L-MACK SENA"
                )
                EmailMessage(
                    subject="[L-MACK] Tu justificacion fue rechazada",
                    body=cuerpo,
                    from_email=getattr(dj_settings, 'DEFAULT_FROM_EMAIL', 'no-reply@sena.edu.co'),
                    to=[aprendiz_usuario.correo],
                ).send(fail_silently=False)
            except Exception as e:
                import logging
                logging.getLogger(__name__).warning(f"No se pudo notificar rechazo al aprendiz: {e}")

            messages.warning(request, 'Justificacion rechazada.')

        return redirect('instructor:listar_justificaciones')

    return render(request, 'justificaciones/resolverJustificacion.html', {
        'justificacion': justificacion,
    })

@never_cache
@rol_requerido(['instructor'])
def detalle_minuta(request, minuta_id):
    minuta = get_object_or_404(
        RegistroMinuta.objects.select_related(
            'ambiente',
            'responsable__usuario_id_usuario',
            'guarda_seguridad_usuario_id_usuario__usuario_id_usuario',
        ),
        pk=minuta_id
    )
    return render(request, "minuta_detalle.html", {"minuta": minuta})


@never_cache
@rol_requerido(['instructor'])
def detalle_incidente(request, incidente_id):
    incidente = get_object_or_404(
        RegistroIncidente.objects.select_related("ambiente", "tipo_inc", "usuario_id_usuario"),
        pk=incidente_id
    )
    historicos = HistoricoIncidentes.objects.filter(incidente=incidente).order_by('-fecha_registro')
    return render(request, "incidente_detalle.html", {"incidente": incidente, "historicos": historicos})


@never_cache
@rol_requerido(['instructor'])
def actualizar_estado_incidente(request, incidente_id):
    incidente = get_object_or_404(RegistroIncidente, pk=incidente_id)
    if request.method == "POST":
        nuevo_estado = request.POST.get("estado")
        comentario = request.POST.get("comentario", "").strip()

        if nuevo_estado in dict(RegistroIncidente.ESTADOS_INCIDENTE):
            old_estado = incidente.estado
            if old_estado != nuevo_estado:
                incidente.estado = nuevo_estado
                incidente.save()

                if comentario:
                    latest_hist = HistoricoIncidentes.objects.filter(incidente=incidente).order_by('-id_historico').first()
                    if latest_hist:
                        latest_hist.descripcion = f"Cambio de estado: '{old_estado}' → '{nuevo_estado}'. Nota: {comentario}"
                        latest_hist.save(update_fields=['descripcion'])

                messages.success(request, f"Estado del incidente actualizado a {nuevo_estado}.")
            else:
                if comentario:
                    from django.utils import timezone
                    HistoricoIncidentes.objects.create(
                        incidente=incidente,
                        ambiente=incidente.ambiente,
                        tipo_incidente=incidente.tipo_inc,
                        descripcion=f"Nota de seguimiento: {comentario}",
                        fecha_registro=timezone.now()
                    )
                    messages.success(request, "Nota de seguimiento agregada correctamente.")
                else:
                    messages.info(request, "No se realizaron cambios.")
        else:
            messages.error(request, "Estado no válido.")

    return redirect('instructor:detalle_incidente', incidente_id=incidente.id_incidente)
