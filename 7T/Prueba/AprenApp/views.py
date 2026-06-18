from django.http import HttpResponse
from django.shortcuts import render
import hashlib
from collections import defaultdict
from urllib.parse import urlencode
from django.contrib import messages
from django.contrib.auth import logout
from django.db import IntegrityError, transaction
from django.db.models import Max, Q
from django.shortcuts import get_object_or_404, redirect, render
from django.urls import reverse
from django.views.decorators.http import require_POST
from django.views.decorators.cache import never_cache
from django.views.generic import ListView, CreateView, UpdateView, DeleteView, View

from LoginApp.models import (
    Aprendiz, Ficha, Instructor, Justificacion,
    RegistroInasistencia, UserRol, Usuario
)


def _redirigir_a_login(request, next_name: str):
    messages.warning(request, "Inicie sesión para continuar.")
    return redirect(f"{reverse('login:login')}?{urlencode({'next': reverse('aprendiz:' + next_name)})}")


def _asistencias(uid: int):
    qs = (
        RegistroInasistencia.objects.filter(aprendiz_usuario_id_usuario_id=uid)
        .select_related(
            "jornada",
            "instructor_usuario_id_usuario__usuario_id_usuario",
            "justificacion",
        )
        .order_by("-fecha_inasistencia", "-id_inasistencia")
    )
    filas = []
    for a in qs:
        iu = a.instructor_usuario_id_usuario.usuario_id_usuario
        just_estado = None
        if hasattr(a, 'justificacion'):
            just_estado = a.justificacion.estado
        filas.append(
            {
                "id_asistencia": a.id_inasistencia,
                "fecha": a.fecha_inasistencia,
                "estado": (a.estado_inasistencia or "").strip(),
                "instructor_nombre": " ".join(filter(None, [iu.p_nombre, iu.p_apellido])).strip(),
                "instructor_usuario_id": a.instructor_usuario_id_usuario_id,
                "jornada_nombre": (a.jornada.nombre_jornada or "").strip(),
                "jornada_id": a.jornada_id,
                "justificacion_estado": just_estado,
            }
        )
    return filas


@never_cache
def aprendiz_index(request):
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "aprendiz_index")
    u = get_object_or_404(Usuario, pk=uid)
    nombre = " ".join(filter(None, [u.p_nombre, u.p_apellido])).strip() or u.correo
    return render(request, "AprenApp/index.html", {"aprendiz_nombre": nombre})


@never_cache
def listar_asistencias(request):
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "listar_asistencias")
    return render(
        request,
        "AprenApp/asistencias/listarAsistencias.html",
        {"asistencias": _asistencias(int(uid))},
    )


def listar_asistencias_tabla(request):
    return listar_asistencias(request)


def registrar_aprendiz(request):
    contexto = {
        "programas": [],
        "fichas": [],
        "aprendiz": None,
        "id_usuario": request.GET.get("id_usuario", ""),
    }
    return render(request, "AprenApp/formAprendiz.html", contexto)


def exportar_asistencias_pdf(request):
    return HttpResponse("Exportacion PDF pendiente de implementacion.")


def exportar_asistencias_excel(request):
    return HttpResponse("Exportacion Excel pendiente de implementacion.")


@never_cache
def perfil_aprendiz(request):
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "perfil_aprendiz")

    from LoginApp.models import Aprendiz
    u = get_object_or_404(Usuario, pk=uid)
    roles_qs = UserRol.objects.filter(id_usuario=u).select_related("id_rol")
    roles_detalle = ", ".join(ur.id_rol.nombre_rol for ur in roles_qs) or "Sin rol asignado"
    nombre_completo = " ".join(
        filter(None, [u.p_nombre, u.s_nombre, u.p_apellido, u.s_apellido])
    ).strip() or u.correo

    # Datos académicos del aprendiz
    programa_nombre = None
    modalidad_nombre = None
    num_ficha = None
    try:
        aprendiz = Aprendiz.objects.select_related(
            'programas_id_programas__modalidad',
            'ficha_idficha',
        ).get(usuario_id_usuario=u)
        if aprendiz.programas_id_programas:
            programa_nombre = aprendiz.programas_id_programas.nombre_programa
            if aprendiz.programas_id_programas.modalidad:
                modalidad_nombre = aprendiz.programas_id_programas.modalidad.nombre_modalidad
        if aprendiz.ficha_idficha:
            num_ficha = aprendiz.ficha_idficha.num_ficha
    except Aprendiz.DoesNotExist:
        pass

    return render(
        request,
        "AprenApp/perfil.html",
        {
            "usuario_perfil": {
                "id": u.id_usuario,
                "nombre_completo": nombre_completo,
                "p_nombre": u.p_nombre,
                "s_nombre": u.s_nombre,
                "p_apellido": u.p_apellido,
                "s_apellido": u.s_apellido,
                "tipo_documento": u.tipo_documento,
                "num_documento": u.num_documento,
                "correo": u.correo,
                "roles": roles_detalle,
                "programa": programa_nombre,
                "modalidad": modalidad_nombre,
                "num_ficha": num_ficha,
            },
        },
    )


# ─────────────────────────────────────────────────────────────────
#  JUSTIFICACIONES (flujo aprendiz)
# ─────────────────────────────────────────────────────────────────

@never_cache
def subir_justificacion(request, asistencia_id):
    """
    Permite al aprendiz subir un PDF como justificación para una inasistencia
    con estado 'N'. Valida: solo PDF, máximo 2MB.
    """
    uid = request.session.get('usuario_id')
    if not uid:
        return _redirigir_a_login(request, 'listar_asistencias')

    inasistencia = get_object_or_404(
        RegistroInasistencia,
        pk=asistencia_id,
        aprendiz_usuario_id_usuario__usuario_id_usuario_id=uid,
        estado_inasistencia='N',
    )

    # Evitar doble justificación
    if hasattr(inasistencia, 'justificacion'):
        messages.warning(request, "Ya existe una justificación registrada para esta inasistencia.")
        return redirect('aprendiz:listar_asistencias')

    if request.method == 'POST':
        archivo = request.FILES.get('pdf_file')

        if not archivo:
            messages.error(request, "Debes seleccionar un archivo PDF.")
        elif not archivo.name.lower().endswith('.pdf'):
            messages.error(request, "Solo se aceptan archivos en formato PDF.")
        elif archivo.size > 2 * 1024 * 1024:  # 2 MB
            messages.error(request, "El archivo no puede superar los 2 MB.")
        else:
            justificacion = Justificacion.objects.create(
                inasistencia=inasistencia,
                pdf_file=archivo,
                estado='Pendiente',
            )

            # Notificar al instructor a cargo
            try:
                from django.core.mail import EmailMessage
                from django.conf import settings as django_settings

                instructor = inasistencia.instructor_usuario_id_usuario
                instructor_usuario = instructor.usuario_id_usuario
                aprendiz_usuario = inasistencia.aprendiz_usuario_id_usuario.usuario_id_usuario

                nombre_aprendiz = f"{aprendiz_usuario.p_nombre} {aprendiz_usuario.p_apellido}".strip()
                nombre_instructor = f"{instructor_usuario.p_nombre} {instructor_usuario.p_apellido}".strip()
                fecha_str = inasistencia.fecha_inasistencia.strftime("%d/%m/%Y")

                asunto = f"[L-MACK] Nueva justificación de {nombre_aprendiz}"
                cuerpo = (
                    f"Estimado/a {nombre_instructor},\n\n"
                    f"El aprendiz {nombre_aprendiz} ha subido una justificación "
                    f"para su inasistencia del día {fecha_str}.\n\n"
                    f"Por favor ingrese al sistema L-MACK y revise la sección "
                    f"\"Justificaciones\" para aprobarla o rechazarla.\n\n"
                    f"— Sistema L-MACK SENA"
                )

                email = EmailMessage(
                    subject=asunto,
                    body=cuerpo,
                    from_email=getattr(django_settings, 'DEFAULT_FROM_EMAIL', 'no-reply@sena.edu.co'),
                    to=[instructor_usuario.correo],
                )
                email.send(fail_silently=False)
            except Exception as e:
                import logging
                logging.getLogger(__name__).warning(f"No se pudo enviar correo al instructor: {e}")

            messages.success(request, "Justificación enviada correctamente. Queda pendiente de revisión.")
            return redirect('aprendiz:listar_asistencias')

    return render(request, 'AprenApp/subirJustificacion.html', {
        'inasistencia': inasistencia,
    })

