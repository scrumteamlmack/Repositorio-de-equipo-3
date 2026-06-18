import hashlib
import sys, os
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))))
from InstApp.export_utils import (
    generar_pdf_response, construir_pdf,
    generar_excel_response, estilizar_excel, guardar_excel_en_response,
)
from collections import defaultdict
from urllib.parse import urlencode
from django.contrib import messages
from django.contrib.auth import logout
from django.db import IntegrityError, transaction
from django.db.models import Max, Q
from django.shortcuts import get_object_or_404, redirect, render
from django.urls import reverse
from django.views.decorators.http import require_POST
from django.views.generic import ListView, CreateView, UpdateView, DeleteView, View

from LoginApp.models import (
    Usuario,
    UserRol,
    Ficha,
    Rol,
    Programas,
    Recursos,
    Ambiente,
    Instructor,
    Jornada,
    Modalidad,
    Coordinacion,
    TipoRecurso,
)


from .utils import *
from LoginApp.decorators import login_requerido, rol_requerido

def listar_ambientes(request):
    from GuardaApp.views.utils import _liberar_minutas_vencidas
    _liberar_minutas_vencidas()
    qs = Ambiente.objects.all().order_by("id_ambiente")

    # Filtros multicriterio
    q = (request.GET.get("q") or "").strip()
    tipo_q = (request.GET.get("tipo") or "").strip()
    estado_q = (request.GET.get("estado") or "").strip()
    if q:
        try:
            qs = qs.filter(num_ambiente=int(q))
        except ValueError:
            qs = qs.filter(Q(tipo_ambiente__icontains=q) | Q(estado__icontains=q))
    if tipo_q:
        qs = qs.filter(tipo_ambiente__icontains=tipo_q)
    if estado_q:
        qs = qs.filter(estado__icontains=estado_q)

    from django.utils import timezone
    from LoginApp.models import AsignacionAmbiente, Ficha, Aprendiz, RegistroMinuta
    hoy = timezone.localdate()
    ahora = timezone.localtime()

    filas = []
    for am in qs:
        # Sincronizar estado dinámicamente según minutas activas
        esta_ocupado = RegistroMinuta.objects.filter(
            ambiente=am,
            fecha_hora_recibo__lte=ahora,
            fecha_hora_entrega__gte=ahora,
            estado="Ocupado"
        ).exists()
        nuevo_estado = "Ocupado" if esta_ocupado else "Disponible"
        if am.estado != nuevo_estado and am.estado in ("Ocupado", "Disponible"):
            am.estado = nuevo_estado
            am.save(update_fields=['estado'])

        # Buscar todas las asignaciones activas de instructores para hoy
        asignaciones = AsignacionAmbiente.objects.filter(
            ambiente=am,
            fecha_inicio__lte=hoy,
            fecha_fin__gte=hoy,
            estado='ACTIVO'
        ).select_related('instructor__usuario_id_usuario')

        instructor_nombre = "Sin asignar"
        num_aprendices = 0
        if asignaciones.exists():
            instructor_nombre = ", ".join([
                f"{a.instructor.usuario_id_usuario.p_nombre} {a.instructor.usuario_id_usuario.p_apellido}".strip()
                for a in asignaciones
            ])
            # Obtener fichas de los instructores
            fichas = Ficha.objects.filter(
                instructores__in=[a.instructor for a in asignaciones]
            )
            # Obtener total de aprendices
            num_aprendices = Aprendiz.objects.filter(ficha_idficha__in=fichas).count()

        filas.append({
            "am_id": am.id_ambiente,
            "am_num": str(am.num_ambiente),
            "am_capaci": str(am.capacidad),
            "am_tipo": (am.tipo_ambiente or "").strip() or "—",
            "am_estado": (am.estado or "").strip() or "—",
            "instructor_asignado": instructor_nombre,
            "num_aprendices": num_aprendices,
        })

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("ambientes.pdf")
        cabeceras = ["ID", "Número", "Capacidad", "Tipo", "Estado", "Instructor", "Aprendices"]
        rows = [[
            str(f["am_id"]),
            f["am_num"],
            f["am_capaci"],
            f["am_tipo"],
            f["am_estado"],
            f["instructor_asignado"],
            str(f["num_aprendices"])
        ] for f in filas]
        construir_pdf(buffer, "Ambientes de Formación", cabeceras, rows, "horizontal")
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("ambientes.xlsx")
        cabeceras = ["ID", "Número", "Capacidad", "Tipo", "Estado", "Instructor", "Aprendices"]
        rows = [[
            str(f["am_id"]),
            f["am_num"],
            f["am_capaci"],
            f["am_tipo"],
            f["am_estado"],
            f["instructor_asignado"],
            str(f["num_aprendices"])
        ] for f in filas]
        estilizar_excel(ws, cabeceras, rows, "Ambientes de Formación")
        return guardar_excel_en_response(response, wb)

    filtros = {"q": q, "tipo": tipo_q, "estado": estado_q}
    return _render_admin(request, "listarAmbientes.html", {
        "ambientes": filas,
        "filtros": filtros,
    })


def exportar_ambientes_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_ambientes(request)


def exportar_ambientes_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_ambientes(request)


def crear_ambiente(request):
    ctx = _context_form_ambiente()
    if request.method == "POST":
        try:
            numero = int(request.POST.get("numero"))
            capacidad = int(request.POST.get("capacidad"))
        except (TypeError, ValueError):
            messages.error(request, "Número y capacidad deben ser enteros.")
            ctx["ambiente"].update(
                {
                    "numero": request.POST.get("numero") or "",
                    "capacidad": request.POST.get("capacidad") or "",
                    "tipo": request.POST.get("tipo", "").strip(),
                    "estado": request.POST.get("estado", "").strip(),
                }
            )
            return _render_admin(request, "formAmbiente.html", ctx)
        tipo = request.POST.get("tipo", "").strip()
        estado = request.POST.get("estado", "").strip()
        
        ctx["ambiente"].update(
            {"numero": numero, "capacidad": capacidad, "tipo": tipo, "estado": estado}
        )

        if not tipo or not estado:
            messages.error(request, "Tipo y estado son obligatorios.")
            return _render_admin(request, "formAmbiente.html", ctx)

        if numero < 100 or numero > 9999:
            messages.error(request, "El número de ambiente debe tener entre 3 y 4 dígitos (ej: 101, 2002).")
            return _render_admin(request, "formAmbiente.html", ctx)

        if capacidad <= 0:
            messages.error(request, "La capacidad debe ser un número positivo mayor que 0.")
            return _render_admin(request, "formAmbiente.html", ctx)

        if Ambiente.objects.filter(num_ambiente=numero).exists():
            messages.error(request, f"Ya existe un ambiente registrado con el número {numero}.")
            return _render_admin(request, "formAmbiente.html", ctx)

        siguiente = (Ambiente.objects.aggregate(m=Max("id_ambiente"))["m"] or 0) + 1
        try:
            Ambiente.objects.create(
                id_ambiente=siguiente,
                num_ambiente=numero,
                capacidad=capacidad,
                tipo_ambiente=tipo,
                estado=estado,
            )
        except IntegrityError:
            messages.error(request, "No se pudo crear el ambiente.")
            return _render_admin(request, "formAmbiente.html", ctx)
        messages.success(request, "Ambiente creado correctamente.")
        return redirect('admin_panel:listar_ambientes')
    return _render_admin(request, "formAmbiente.html", ctx)


def editar_ambiente(request, ambiente_id):
    a = get_object_or_404(Ambiente, pk=ambiente_id)
    ctx = _context_form_ambiente(_ambiente_form_desde_modelo(a))
    ctx["idAmbienteEditar"] = ambiente_id
    if request.method == "POST":
        try:
            numero = int(request.POST.get("numero"))
            capacidad = int(request.POST.get("capacidad"))
        except (TypeError, ValueError):
            messages.error(request, "Número y capacidad deben ser enteros.")
            return _render_admin(request, "formAmbiente.html", ctx)
        tipo = request.POST.get("tipo", "").strip()
        estado = request.POST.get("estado", "").strip()
        
        ctx["ambiente"].update(
            {"numero": numero, "capacidad": capacidad, "tipo": tipo, "estado": estado}
        )

        if not tipo or not estado:
            messages.error(request, "Tipo y estado son obligatorios.")
            return _render_admin(request, "formAmbiente.html", ctx)

        if numero < 100 or numero > 9999:
            messages.error(request, "El número de ambiente debe tener entre 3 y 4 dígitos (ej: 101, 2002).")
            return _render_admin(request, "formAmbiente.html", ctx)

        if capacidad <= 0:
            messages.error(request, "La capacidad debe ser un número positivo mayor que 0.")
            return _render_admin(request, "formAmbiente.html", ctx)

        if Ambiente.objects.filter(num_ambiente=numero).exclude(pk=ambiente_id).exists():
            messages.error(request, f"Ya existe un ambiente registrado con el número {numero}.")
            return _render_admin(request, "formAmbiente.html", ctx)

        a.num_ambiente = numero
        a.capacidad = capacidad
        a.tipo_ambiente = tipo
        a.estado = estado
        try:
            a.save()
        except IntegrityError:
            messages.error(request, "No se pudo guardar el ambiente.")
            return _render_admin(request, "formAmbiente.html", ctx)
        messages.success(request, "Ambiente actualizado correctamente.")
        return redirect('admin_panel:listar_ambientes')
    return _render_admin(request, "formAmbiente.html", ctx)


@require_POST
def eliminar_ambiente(request, ambiente_id):
    a = Ambiente.objects.filter(pk=ambiente_id).first()
    if not a:
        messages.error(request, "Ambiente no encontrado.")
        return redirect('admin_panel:listar_ambientes')
    try:
        a.delete()
    except IntegrityError:
        messages.error(
            request,
            "No se puede eliminar: hay recursos, incidentes u otros registros en este ambiente.",
        )
        return redirect('admin_panel:listar_ambientes')
    messages.success(request, "Ambiente eliminado correctamente.")
    return redirect('admin_panel:listar_ambientes')


# ─────────────────────────────────────────────────────────────────
#  ASIGNACIONES DE AMBIENTES (Administrador)
# ─────────────────────────────────────────────────────────────────

def listar_asignaciones_ambientes(request):
    from LoginApp.models import AsignacionAmbiente
    asignaciones = AsignacionAmbiente.objects.all().select_related(
        'instructor__usuario_id_usuario',
        'ambiente',
        'jornada'
    ).order_by('-id_asignacion')
    
    return _render_admin(request, "listarAsignaciones.html", {
        "asignaciones": asignaciones
    })


def crear_asignacion_ambiente(request):
    from LoginApp.models import AsignacionAmbiente, Instructor, Ambiente, Jornada
    from django.utils import timezone
    from datetime import timedelta

    instructores = Instructor.objects.all().select_related('usuario_id_usuario')
    ambientes = Ambiente.objects.all().order_by('num_ambiente')
    jornadas = Jornada.objects.all()

    hoy = timezone.localdate()
    fecha_fin_def = hoy + timedelta(days=90)  # Rango por defecto de 3 meses (un trimestre)

    if request.method == "POST":
        instructor_id = request.POST.get("instructor_id")
        ambiente_id = request.POST.get("ambiente_id")
        jornada_id = request.POST.get("jornada_id")
        fecha_inicio = request.POST.get("fecha_inicio")
        fecha_fin = request.POST.get("fecha_fin")
        trimestre = request.POST.get("trimestre", "").strip()
        estado = request.POST.get("estado", "ACTIVO")

        if not instructor_id or not ambiente_id or not fecha_inicio or not fecha_fin or not trimestre:
            messages.error(request, "Todos los campos son obligatorios.")
        else:
            try:
                # El modelo valida el traslape en clean().
                # Para que se ejecute la validación, llamamos a save() o create()
                asignacion = AsignacionAmbiente(
                    instructor_id=int(instructor_id),
                    ambiente_id=int(ambiente_id),
                    jornada_id=int(jornada_id) if jornada_id else None,
                    fecha_inicio=fecha_inicio,
                    fecha_fin=fecha_fin,
                    trimestre=trimestre,
                    estado=estado
                )
                asignacion.save()  # Esto llamará a clean() y full_clean() antes de guardar
                messages.success(request, "Instructor asignado al ambiente correctamente.")
                return redirect('admin_panel:listar_asignaciones_ambientes')
            except Exception as e:
                messages.error(request, f"Error al guardar la asignación: {str(e)}")

    return _render_admin(request, "formAsignacion.html", {
        "instructores": instructores,
        "ambientes": ambientes,
        "jornadas": jornadas,
        "estados": ["ACTIVO", "INACTIVO"],
        "fecha_inicio_def": hoy.isoformat(),
        "fecha_fin_def": fecha_fin_def.isoformat(),
    })


@require_POST
def eliminar_asignacion_ambiente(request, asignacion_id):
    from LoginApp.models import AsignacionAmbiente
    asignacion = get_object_or_404(AsignacionAmbiente, pk=asignacion_id)
    try:
        asignacion.delete()
        messages.success(request, "Asignación eliminada correctamente.")
    except Exception as e:
        messages.error(request, f"No se pudo eliminar la asignación: {str(e)}")
    return redirect('admin_panel:listar_asignaciones_ambientes')






