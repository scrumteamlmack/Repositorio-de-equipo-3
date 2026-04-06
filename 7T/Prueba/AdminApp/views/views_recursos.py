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

def listar_recursos(request):
    q = (request.GET.get("q") or "").strip()
    estado_q = (request.GET.get("estado") or "").strip()
    tipo_q = (request.GET.get("tipo") or "").strip()
    amb_q = (request.GET.get("ambiente") or "").strip()

    filtro = Q()
    if q:
        filtro &= Q(nombre_recurso__icontains=q) | Q(serial_recurso__icontains=q)
    if estado_q:
        filtro &= Q(estado__icontains=estado_q)
    if tipo_q:
        filtro &= Q(tipo_recurso__recurso_tipo__icontains=tipo_q)
    if amb_q:
        try:
            filtro &= Q(ambiente__num_ambiente=int(amb_q))
        except ValueError:
            pass

    filas = []
    qs = (
        Recursos.objects.filter(filtro)
        .select_related("tipo_recurso", "ambiente")
        .order_by("id_recurso")
    )
    for r in qs:
        filas.append({
            "r_id": r.id_recurso,
            "r_nombre": (r.nombre_recurso or "").strip() or "—",
            "r_serial": (r.serial_recurso or "").strip() or "—",
            "r_numero": str(r.num_recurso),
            "r_tipo": (r.tipo_recurso.recurso_tipo or "").strip() or "—",
            "r_estado": (r.estado or "").strip() or "—",
            "r_observacion": (r.observacion or "").strip(),
            "r_ambiente": str(r.ambiente.num_ambiente),
        })

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("recursos.pdf")
        cabeceras = ["ID", "Nombre", "Serial", "Tipo", "Estado", "Ambiente"]
        rows = [[str(f["r_id"]), f["r_nombre"], f["r_serial"], f["r_tipo"], f["r_estado"], f["r_ambiente"]] for f in filas]
        construir_pdf(buffer, "Inventario de Recursos", cabeceras, rows)
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("recursos.xlsx")
        cabeceras = ["ID", "Nombre", "Serial", "Número", "Tipo", "Estado", "Observación", "Ambiente"]
        rows = [[str(f["r_id"]), f["r_nombre"], f["r_serial"], f["r_numero"], f["r_tipo"], f["r_estado"], f["r_observacion"], f["r_ambiente"]] for f in filas]
        estilizar_excel(ws, cabeceras, rows, "Inventario de Recursos")
        return guardar_excel_en_response(response, wb)

    filtros = {"q": q, "estado": estado_q, "tipo": tipo_q, "ambiente": amb_q}
    return _render_admin(request, "listarRecursos.html", {"recursos": filas, "filtros": filtros})


def exportar_recursos_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_recursos(request)


def exportar_recursos_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_recursos(request)


def crear_recurso(request):
    ctx = _context_form_recurso()
    if request.method == "POST":
        nombre = request.POST.get("nombre", "").strip()
        serial = request.POST.get("serial", "").strip()
        estado = request.POST.get("estado", "").strip()
        observacion = (request.POST.get("observacion") or "").strip()
        tipo_id = request.POST.get("idTipoRecurso")
        amb_id = request.POST.get("idAmbiente")
        try:
            numero = int(request.POST.get("numero"))
        except (TypeError, ValueError):
            messages.error(request, "El número del recurso debe ser entero.")
            ctx["recurso"].update(
                {
                    "nombre": nombre,
                    "serial": serial,
                    "numero": request.POST.get("numero") or "",
                    "idTipoRecurso": tipo_id or "",
                    "idAmbiente": amb_id or "",
                    "estado": estado,
                    "observacion": observacion,
                }
            )
            return _render_admin(request, "formRecurso.html", ctx)
        if not all([nombre, serial, tipo_id, amb_id, estado]):
            messages.error(request, "Complete los campos obligatorios.")
            ctx["recurso"].update(
                {
                    "nombre": nombre,
                    "serial": serial,
                    "numero": numero,
                    "idTipoRecurso": tipo_id or "",
                    "idAmbiente": amb_id or "",
                    "estado": estado,
                    "observacion": observacion,
                }
            )
            return _render_admin(request, "formRecurso.html", ctx)
        try:
            tipo_id, amb_id = int(tipo_id), int(amb_id)
        except ValueError:
            messages.error(request, "Tipo o ambiente no válido.")
            return _render_admin(request, "formRecurso.html", ctx)
        if not TipoRecurso.objects.filter(pk=tipo_id).exists() or not Ambiente.objects.filter(
            pk=amb_id
        ).exists():
            messages.error(request, "Tipo de recurso o ambiente no existe.")
            return _render_admin(request, "formRecurso.html", ctx)
        try:
            Recursos.objects.create(
                nombre_recurso=nombre,
                serial_recurso=serial,
                num_recurso=numero,
                tipo_recurso_id=tipo_id,
                ambiente_id=amb_id,
                estado=estado or None,
                observacion=observacion or None,
            )
        except IntegrityError:
            messages.error(request, "No se pudo crear el recurso.")
            return _render_admin(request, "formRecurso.html", ctx)
        messages.success(request, "Recurso creado correctamente.")
        return redirect('admin_panel:listar_recursos')
    return _render_admin(request, "formRecurso.html", ctx)


def editar_recurso(request, recurso_id):
    r = get_object_or_404(Recursos, pk=recurso_id)
    ctx = _context_form_recurso(_recurso_form_desde_modelo(r))
    ctx["idRecursoEditar"] = recurso_id
    if request.method == "POST":
        nombre = request.POST.get("nombre", "").strip()
        serial = request.POST.get("serial", "").strip()
        estado = request.POST.get("estado", "").strip()
        observacion = (request.POST.get("observacion") or "").strip()
        tipo_id = request.POST.get("idTipoRecurso")
        amb_id = request.POST.get("idAmbiente")
        try:
            numero = int(request.POST.get("numero"))
        except (TypeError, ValueError):
            messages.error(request, "El número del recurso debe ser entero.")
            return _render_admin(request, "formRecurso.html", ctx)
        if not all([nombre, serial, tipo_id, amb_id, estado]):
            messages.error(request, "Complete los campos obligatorios.")
            return _render_admin(request, "formRecurso.html", ctx)
        try:
            tipo_id, amb_id = int(tipo_id), int(amb_id)
        except ValueError:
            messages.error(request, "Tipo o ambiente no válido.")
            return _render_admin(request, "formRecurso.html", ctx)
        r.nombre_recurso = nombre
        r.serial_recurso = serial
        r.num_recurso = numero
        r.tipo_recurso_id = tipo_id
        r.ambiente_id = amb_id
        r.estado = estado or None
        r.observacion = observacion or None
        try:
            r.save()
        except IntegrityError:
            messages.error(request, "No se pudo guardar el recurso.")
            return _render_admin(request, "formRecurso.html", ctx)
        messages.success(request, "Recurso actualizado correctamente.")
        return redirect('admin_panel:listar_recursos')
    return _render_admin(request, "formRecurso.html", ctx)


@require_POST
def eliminar_recurso(request, recurso_id):
    r = Recursos.objects.filter(pk=recurso_id).first()
    if not r:
        messages.error(request, "Recurso no encontrado.")
        return redirect('admin_panel:listar_recursos')
    try:
        r.delete()
    except IntegrityError:
        messages.error(request, "No se puede eliminar: el recurso está referenciado en otros registros.")
        return redirect('admin_panel:listar_recursos')
    messages.success(request, "Recurso eliminado correctamente.")
    return redirect('admin_panel:listar_recursos')


