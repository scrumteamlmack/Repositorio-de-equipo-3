from io import BytesIO

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
from django.views.generic import ListView, CreateView, UpdateView, DeleteView, View
from django.views.decorators.cache import never_cache
from LoginApp.models import RegistroInasistencia, UserRol, Usuario
from openpyxl import Workbook
from openpyxl.styles import Alignment, Font
from Prueba.report_utils import landscape_pdf_response, maybe_int, q_param, safe_q_part


def _redirigir_a_login(request, next_name: str):
    messages.warning(request, "Inicie sesión para continuar.")
    return redirect(f"{reverse('login')}?{urlencode({'next': reverse(next_name)})}")


def _asistencias_qs(uid: int, q: str):
    qs = RegistroInasistencia.objects.filter(aprendiz_usuario_id_usuario_id=uid).select_related(
        "jornada",
        "instructor_usuario_id_usuario__usuario_id_usuario",
    )
    if q:
        nu = maybe_int(q)
        cond = (
            Q(estado_inasistencia__icontains=q)
            | Q(jornada__nombre_jornada__icontains=q)
            | Q(instructor_usuario_id_usuario__usuario_id_usuario__p_nombre__icontains=q)
            | Q(instructor_usuario_id_usuario__usuario_id_usuario__p_apellido__icontains=q)
        )
        if nu is not None:
            cond |= Q(id_inasistencia=nu)
        qs = qs.filter(cond)
    return qs.order_by("-fecha_inasistencia", "-id_inasistencia")


def _asistencias(uid: int, q: str = ""):
    filas = []
    for a in _asistencias_qs(uid, q):
        iu = a.instructor_usuario_id_usuario.usuario_id_usuario
        filas.append(
            {
                "id_asistencia": a.id_inasistencia,
                "fecha": a.fecha_inasistencia,
                "estado": (a.estado_inasistencia or "").strip(),
                "instructor_nombre": " ".join(filter(None, [iu.p_nombre, iu.p_apellido])).strip(),
                "instructor_usuario_id": a.instructor_usuario_id_usuario_id,
                "jornada_nombre": (a.jornada.nombre_jornada or "").strip(),
                "jornada_id": a.jornada_id,
            }
        )
    return filas


def aprendiz_index(request):
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "aprendiz_index")
    u = get_object_or_404(Usuario, pk=uid)
    nombre = " ".join(filter(None, [u.p_nombre, u.p_apellido])).strip() or u.correo
    return render(request, "AprenApp/index.html", {"aprendiz_nombre": nombre})


def listar_asistencias(request):
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "listar_asistencias")
    q = q_param(request)
    return render(
        request,
        "AprenApp/asistencias/listarAsistencias.html",
        {"asistencias": _asistencias(int(uid), q), "q": q},
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
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "listar_asistencias")
    q = q_param(request)
    qs = _asistencias_qs(int(uid), q)
    headers = ["ID", "Fecha", "Estado", "Instructor", "Jornada"]
    rows = []
    for a in qs:
        iu = a.instructor_usuario_id_usuario.usuario_id_usuario
        inst = " ".join(filter(None, [iu.p_nombre, iu.p_apellido])).strip()
        est = (a.estado_inasistencia or "").strip()
        estado_txt = "Asistió" if est == "S" else ("Retardo" if est == "R" else "Faltó")
        rows.append(
            [
                str(a.id_inasistencia),
                a.fecha_inasistencia.isoformat() if a.fecha_inasistencia else "",
                estado_txt,
                inst,
                (a.jornada.nombre_jornada or "").strip(),
            ]
        )
    return landscape_pdf_response(
        "Mis inasistencias",
        q,
        headers,
        rows,
        [0.10, 0.18, 0.14, 0.28, 0.30],
        f"inasistencias_{safe_q_part(q)}.pdf",
    )


def exportar_asistencias_excel(request):
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "listar_asistencias")
    q = q_param(request)
    qs = _asistencias_qs(int(uid), q)
    wb = Workbook()
    ws = wb.active
    ws.title = "Inasistencias"
    headers = ["ID", "Fecha", "Estado código", "Instructor", "Jornada"]
    ws.append(headers)
    for col_idx in range(1, len(headers) + 1):
        c = ws.cell(row=1, column=col_idx)
        c.font = Font(bold=True)
        c.alignment = Alignment(horizontal="center", vertical="center", wrap_text=True)
    for a in qs:
        iu = a.instructor_usuario_id_usuario.usuario_id_usuario
        inst = " ".join(filter(None, [iu.p_nombre, iu.p_apellido])).strip()
        ws.append(
            [
                a.id_inasistencia,
                a.fecha_inasistencia.isoformat() if a.fecha_inasistencia else "",
                (a.estado_inasistencia or "").strip(),
                inst,
                (a.jornada.nombre_jornada or "").strip(),
            ]
        )
    buf = BytesIO()
    wb.save(buf)
    buf.seek(0)
    resp = HttpResponse(
        buf.getvalue(),
        content_type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    )
    resp["Content-Disposition"] = f'attachment; filename="inasistencias_{safe_q_part(q)}.xlsx"'
    return resp


def perfil_aprendiz(request):
    uid = request.session.get("usuario_id")
    if not uid:
        return _redirigir_a_login(request, "perfil_aprendiz")

    u = get_object_or_404(Usuario, pk=uid)
    roles_qs = UserRol.objects.filter(id_usuario=u).select_related("id_rol")
    roles_detalle = ", ".join(ur.id_rol.nombre_rol for ur in roles_qs) or "Sin rol asignado"
    nombre_completo = " ".join(
        filter(None, [u.p_nombre, u.s_nombre, u.p_apellido, u.s_apellido])
    ).strip() or u.correo

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
            },
        },
    )
