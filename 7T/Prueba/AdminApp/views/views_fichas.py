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
from LoginApp.forms import FichaForm
from LoginApp.decorators import login_requerido, rol_requerido

def listar_fichas(request):
    qs = Ficha.objects.prefetch_related(
        "instructores__usuario_id_usuario"
    ).all().order_by("num_ficha")

    # Filtros multicriterio
    q = (request.GET.get("q") or "").strip()
    instructor_q = (request.GET.get("instructor") or "").strip()
    if q:
        try:
            qs = qs.filter(num_ficha=int(q))
        except ValueError:
            pass
    if instructor_q:
        qs = qs.filter(
            Q(instructores__usuario_id_usuario__p_nombre__icontains=instructor_q) |
            Q(instructores__usuario_id_usuario__p_apellido__icontains=instructor_q)
        ).distinct()

    filas = []
    for f in qs:
        nombres_inst = []
        for inst in f.instructores.all():
            u = inst.usuario_id_usuario
            nombres_inst.append(" ".join(filter(None, [u.p_nombre, u.p_apellido])).strip() or f"ID {inst.pk}")
        
        nombre_inst = ", ".join(nombres_inst) if nombres_inst else "—"
            
        filas.append({
            "id": f.idficha,
            "num_ficha": f.num_ficha,
            "instructor": nombre_inst,
        })

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("fichas.pdf")
        cabeceras = ["ID", "Número de Ficha", "Instructor"]
        rows = [[str(f["id"]), str(f["num_ficha"]), f["instructor"]] for f in filas]
        construir_pdf(buffer, "Fichas de Formación", cabeceras, rows, "vertical")
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("fichas.xlsx")
        cabeceras = ["ID", "Número de Ficha", "Instructor"]
        rows = [[str(f["id"]), str(f["num_ficha"]), f["instructor"]] for f in filas]
        estilizar_excel(ws, cabeceras, rows, "Fichas de Formación")
        return guardar_excel_en_response(response, wb)

    filtros = {"q": q, "instructor": instructor_q}
    return _render_admin(request, "listarFichas.html", {"fichas": filas, "filtros": filtros})


def exportar_fichas_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_fichas(request)


def exportar_fichas_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_fichas(request)


def crear_ficha(request):
    if request.method == "POST":
        form = FichaForm(request.POST)
        if form.is_valid():
            try:
                form.save()
                messages.success(request, "Ficha creada correctamente con instructor asignado.")
                return redirect('admin_panel:listar_fichas')
            except Exception as e:
                messages.error(request, f"Error técnico al guardar: {str(e)}")
    else:
        form = FichaForm()

    return _render_admin(
        request, "formFicha.html",
        {"form": form}
    )


def editar_ficha(request, ficha_id):
    ficha_obj = get_object_or_404(Ficha, pk=ficha_id)
    if request.method == "POST":
        form = FichaForm(request.POST, instance=ficha_obj)
        if form.is_valid():
            try:
                form.save()
                messages.success(request, "Ficha actualizada correctamente.")
                return redirect('admin_panel:listar_fichas')
            except Exception as e:
                messages.error(request, f"Error técnico al actualizar: {str(e)}")
    else:
        form = FichaForm(instance=ficha_obj)

    return _render_admin(
        request, "formFicha.html",
        {"form": form, "idFichaEditar": ficha_id}
    )


@require_POST
def eliminar_ficha(request, ficha_id):
    f = Ficha.objects.filter(pk=ficha_id).first()
    if not f:
        messages.error(request, "Ficha no encontrada.")
        return redirect('admin_panel:listar_fichas')
    try:
        f.delete()
    except IntegrityError:
        messages.error(
            request,
            "No se puede eliminar: hay aprendices u otros registros asociados a esta ficha.",
        )
        return redirect('admin_panel:listar_fichas')
    messages.success(request, "Ficha eliminada correctamente.")
    return redirect('admin_panel:listar_fichas')


