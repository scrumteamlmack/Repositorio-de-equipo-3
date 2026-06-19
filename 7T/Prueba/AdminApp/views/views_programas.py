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

def listar_programas(request):
    qs = Programas.objects.select_related("jornada", "modalidad", "coordinacion").order_by("id_programas")

    # Filtros multicriterio
    q = (request.GET.get("q") or "").strip()
    modalidad_q = (request.GET.get("modalidad") or "").strip()
    jornada_q = (request.GET.get("jornada") or "").strip()
    if q:
        qs = qs.filter(Q(nombre_programa__icontains=q) | Q(nivel_formacion__icontains=q))
    if modalidad_q:
        qs = qs.filter(modalidad__nombre_modalidad__icontains=modalidad_q)
    if jornada_q:
        qs = qs.filter(jornada__nombre_jornada__icontains=jornada_q)

    filas = []
    for p in qs:
        filas.append({
            "p_id": p.id_programas,
            "p_nombre": (p.nombre_programa or "").strip() or "—",
            "p_nivel": (p.nivel_formacion or "").strip() or "—",
            "p_duracion": (p.duracion or "").strip() or "—",
            "p_jornada": (p.jornada.nombre_jornada or "").strip() or "—",
            "p_modalidad": (p.modalidad.nombre_modalidad or "").strip() or "—",
            "p_coordinacion": (p.coordinacion.nombre_coordinacion or "").strip() or "—",
        })

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("programas.pdf")
        cabeceras = ["ID", "Programa", "Nivel", "Duración", "Jornada", "Modalidad"]
        rows = [[str(f["p_id"]), f["p_nombre"], f["p_nivel"], f["p_duracion"], f["p_jornada"], f["p_modalidad"]] for f in filas]
        construir_pdf(buffer, "Programas de Formación", cabeceras, rows)
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("programas.xlsx")
        cabeceras = ["ID", "Programa", "Nivel", "Duración", "Jornada", "Modalidad", "Coordinación"]
        rows = [[str(f["p_id"]), f["p_nombre"], f["p_nivel"], f["p_duracion"], f["p_jornada"], f["p_modalidad"], f["p_coordinacion"]] for f in filas]
        estilizar_excel(ws, cabeceras, rows, "Programas de Formación")
        return guardar_excel_en_response(response, wb)

    filtros = {"q": q, "modalidad": modalidad_q, "jornada": jornada_q}
    return _render_admin(request, "listarProgramas.html", {"programas": filas, "filtros": filtros})


def exportar_programas_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_programas(request)


def exportar_programas_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_programas(request)


def crear_programa(request):
    ctx = _context_form_programa()
    if request.method == "POST":
        nombre = request.POST.get("nombrePrograma", "").strip()
        nivel = request.POST.get("nivelFormacion", "").strip()
        duracion = request.POST.get("duracion", "").strip()
        jid = request.POST.get("jornadaId")
        mid = request.POST.get("modalidadId")
        cid = request.POST.get("coordinacionId")
        if not all([nombre, nivel, duracion, jid, mid, cid]):
            messages.error(request, "Complete todos los campos.")
            ctx["programa"].update(
                {
                    "nombrePrograma": nombre,
                    "nivelFormacion": nivel,
                    "duracion": duracion,
                    "jornadaId": jid or "",
                    "modalidadId": mid or "",
                    "coordinacionId": cid or "",
                }
            )
            return _render_admin(request, "formPrograma.html", ctx)
        try:
            jid, mid, cid = int(jid), int(mid), int(cid)
        except ValueError:
            messages.error(request, "Valores de jornada, modalidad o coordinación inválidos.")
            return _render_admin(request, "formPrograma.html", ctx)
        if not (
            Jornada.objects.filter(pk=jid).exists()
            and Modalidad.objects.filter(pk=mid).exists()
            and Coordinacion.objects.filter(pk=cid).exists()
        ):
            messages.error(request, "La jornada, modalidad o coordinación no existe.")
            return _render_admin(request, "formPrograma.html", ctx)
        siguiente = (Programas.objects.aggregate(m=Max("id_programas"))["m"] or 0) + 1
        try:
            Programas.objects.create(
                id_programas=siguiente,
                nombre_programa=nombre,
                nivel_formacion=nivel,
                duracion=duracion,
                jornada_id=jid,
                modalidad_id=mid,
                coordinacion_id=cid,
            )
        except IntegrityError:
            messages.error(request, "No se pudo crear el programa.")
            return _render_admin(request, "formPrograma.html", ctx)
        messages.success(request, "Programa creado correctamente.")
        return redirect('admin_panel:listar_programas')
    return _render_admin(request, "formPrograma.html", ctx)


def editar_programa(request, programa_id):
    p = get_object_or_404(Programas, pk=programa_id)
    ctx = _context_form_programa(_programa_form_desde_modelo(p))
    ctx["idProgramaEditar"] = programa_id
    if request.method == "POST":
        nombre = request.POST.get("nombrePrograma", "").strip()
        nivel = request.POST.get("nivelFormacion", "").strip()
        duracion = request.POST.get("duracion", "").strip()
        jid = request.POST.get("jornadaId")
        mid = request.POST.get("modalidadId")
        cid = request.POST.get("coordinacionId")
        if not all([nombre, nivel, duracion, jid, mid, cid]):
            messages.error(request, "Complete todos los campos.")
            ctx["programa"].update(
                {
                    "nombrePrograma": nombre,
                    "nivelFormacion": nivel,
                    "duracion": duracion,
                    "jornadaId": jid or "",
                    "modalidadId": mid or "",
                    "coordinacionId": cid or "",
                }
            )
            return _render_admin(request, "formPrograma.html", ctx)
        try:
            jid, mid, cid = int(jid), int(mid), int(cid)
        except ValueError:
            messages.error(request, "Valores inválidos.")
            return _render_admin(request, "formPrograma.html", ctx)
        p.nombre_programa = nombre
        p.nivel_formacion = nivel
        p.duracion = duracion
        p.jornada_id = jid
        p.modalidad_id = mid
        p.coordinacion_id = cid
        try:
            p.save()
        except IntegrityError:
            messages.error(request, "No se pudo guardar el programa.")
            return _render_admin(request, "formPrograma.html", ctx)
        messages.success(request, "Programa actualizado correctamente.")
        return redirect('admin_panel:listar_programas')
    return _render_admin(request, "formPrograma.html", ctx)


@require_POST
def eliminar_programa(request, programa_id):
    p = Programas.objects.filter(pk=programa_id).first()
    if not p:
        messages.error(request, "Programa no encontrado.")
        return redirect('admin_panel:listar_programas')
    try:
        p.delete()
    except IntegrityError:
        messages.error(
            request,
            "No se puede eliminar: hay aprendices u otros registros que usan este programa.",
        )
        return redirect('admin_panel:listar_programas')
    messages.success(request, "Programa eliminado correctamente.")
    return redirect('admin_panel:listar_programas')


