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

    filas = []
    for am in qs:
        filas.append({
            "am_id": am.id_ambiente,
            "am_num": str(am.num_ambiente),
            "am_capaci": str(am.capacidad),
            "am_tipo": (am.tipo_ambiente or "").strip() or "—",
            "am_estado": (am.estado or "").strip() or "—",
        })

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("ambientes.pdf")
        cabeceras = ["ID", "Número", "Capacidad", "Tipo", "Estado"]
        rows = [[str(f["am_id"]), f["am_num"], f["am_capaci"], f["am_tipo"], f["am_estado"]] for f in filas]
        construir_pdf(buffer, "Ambientes de Formación", cabeceras, rows, "vertical")
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("ambientes.xlsx")
        cabeceras = ["ID", "Número", "Capacidad", "Tipo", "Estado"]
        rows = [[str(f["am_id"]), f["am_num"], f["am_capaci"], f["am_tipo"], f["am_estado"]] for f in filas]
        estilizar_excel(ws, cabeceras, rows, "Ambientes de Formación")
        return guardar_excel_en_response(response, wb)

    filtros = {"q": q, "tipo": tipo_q, "estado": estado_q}
    return _render_admin(request, "listarAmbientes.html", {"ambientes": filas, "filtros": filtros})


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
        if not tipo or not estado:
            messages.error(request, "Tipo y estado son obligatorios.")
            ctx["ambiente"].update(
                {"numero": numero, "capacidad": capacidad, "tipo": tipo, "estado": estado}
            )
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
        if not tipo or not estado:
            messages.error(request, "Tipo y estado son obligatorios.")
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


