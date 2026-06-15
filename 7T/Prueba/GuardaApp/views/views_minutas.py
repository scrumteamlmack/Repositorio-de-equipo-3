from datetime import datetime
from io import BytesIO
import json
import re

from django.contrib import messages
from django.db import IntegrityError, transaction
from django.db.models import Max, Q
from django.http import HttpResponse, JsonResponse
from django.shortcuts import get_object_or_404, redirect, render
from django.urls import reverse
from django.views.decorators.cache import never_cache
from django.views.decorators.http import require_POST

from openpyxl import Workbook
from openpyxl.styles import Alignment, Font

from LoginApp.models import (
    Ambiente,
    GuardaSeguridad,
    Instructor,
    Recursos,
    RegistroIncidente,
    RegistroMinuta,
    Rol,
    TipoIncidente,
    TrasladoRecurso,
    UserRol,
    Usuario,
    AsignacionAmbiente,
)

from .utils import *
from LoginApp.decorators import login_requerido, rol_requerido


@never_cache
def listar_minutas(request):
    _liberar_minutas_vencidas()
    filtros = _get_minutas_filters(request)
    minutas = (
        RegistroMinuta.objects.select_related(
            "ambiente",
            "guarda_seguridad_usuario_id_usuario__usuario_id_usuario",
            "responsable__usuario_id_usuario",
        )
        .all()
        .order_by("-fecha_hora_recibo")
    )
    minutas = _aplicar_filtros_minutas(minutas, filtros)
    return _render_guarda(
        request,
        "guarda/minutas_list.html",
        {"minutas": minutas, "filtros": filtros, "hay_filtros": any(filtros.values())},
    )


def _sincronizar_estado_ambiente(ambiente_id):
    from django.utils import timezone
    from LoginApp.models import RegistroMinuta, Ambiente
    ahora = timezone.localtime()
    
    esta_ocupado = RegistroMinuta.objects.filter(
        ambiente_id=ambiente_id,
        fecha_hora_recibo__lte=ahora,
        fecha_hora_entrega__gte=ahora,
        estado="Ocupado"
    ).exists()
    
    nuevo_estado = "Ocupado" if esta_ocupado else "Disponible"
    
    amb = Ambiente.objects.filter(pk=ambiente_id).first()
    if amb and amb.estado in ("Ocupado", "Disponible") and amb.estado != nuevo_estado:
        amb.estado = nuevo_estado
        amb.save(update_fields=['estado'])

@never_cache
def resolver_ambiente_ajax(request):
    """
    Endpoint AJAX para obtener los ambientes libres y asignados de un instructor en una fecha/hora específica.
    """
    from django.utils import timezone
    instructor_id = request.GET.get("instructor_id")
    if not instructor_id:
        return JsonResponse({"success": False, "error": "No se proporcionó el ID del responsable."})

    fecha_hora_str = request.GET.get("fecha_hora")
    minuta_id = request.GET.get("minuta_id")

    if fecha_hora_str:
        try:
            dt_eval = datetime.strptime(fecha_hora_str, "%Y-%m-%dT%H:%M")
            fecha_hora_eval = timezone.make_aware(dt_eval, timezone.get_current_timezone())
        except ValueError:
            fecha_hora_eval = timezone.localtime()
    else:
        fecha_hora_eval = timezone.localtime()

    hoy = fecha_hora_eval.date()

    asignaciones = AsignacionAmbiente.objects.filter(
        instructor_id=instructor_id,
        fecha_inicio__lte=hoy,
        fecha_fin__gte=hoy,
        estado='ACTIVO'
    ).select_related("ambiente")

    if asignaciones.exists():
        vistos = set()
        ambientes_data = []
        for asig in asignaciones:
            amb = asig.ambiente
            if amb.id_ambiente in vistos:
                continue
            vistos.add(amb.id_ambiente)

            # Excluir ambientes que están en mantenimiento
            if amb.estado == "En mantenimiento":
                continue

            # Verificar si el ambiente está ocupado por otra minuta en la fecha_hora_eval
            minutas_ocupadas = RegistroMinuta.objects.filter(
                ambiente=amb,
                fecha_hora_recibo__lte=fecha_hora_eval,
                fecha_hora_entrega__gte=fecha_hora_eval,
                estado="Ocupado"
            )
            if minuta_id:
                try:
                    minutas_ocupadas = minutas_ocupadas.exclude(pk=int(minuta_id))
                except ValueError:
                    pass

            if not minutas_ocupadas.exists():
                ambientes_data.append({
                    "id": amb.id_ambiente,
                    "num": amb.num_ambiente,
                    "tipo": amb.tipo_ambiente,
                })

        if ambientes_data:
            return JsonResponse({
                "success": True,
                "ambientes": ambientes_data
            })
        else:
            return JsonResponse({
                "success": False,
                "error": "Todos los ambientes asignados al responsable están ocupados en esta fecha/hora."
            })

    return JsonResponse({
        "success": False,
        "error": "El responsable seleccionado no tiene ambientes asignados para la fecha seleccionada."
    })


@never_cache
def crear_minuta(request):
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    responsables = Instructor.objects.select_related("usuario_id_usuario").all()
    usuario, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied

    from django.utils import timezone
    ahora = timezone.localtime()
    hoy = ahora.date()

    if request.method == "POST":
        from django.utils import timezone
        
        # Validar y parsear fechas
        recibo_str = request.POST.get("fecha_hora_recibo")
        entrega_str = request.POST.get("fecha_hora_entrega")
        
        try:
            responsable_id = int(request.POST.get("responsable_id"))
            estado = (request.POST.get("estado") or "Ocupado").strip()
            novedad = (request.POST.get("novedad") or "").strip() or None
            descripcion = (request.POST.get("descripcion_min") or "").strip() or None
            
            if recibo_str:
                dt_recibo = datetime.strptime(recibo_str, "%Y-%m-%dT%H:%M")
                fecha_hora_recibo = timezone.make_aware(dt_recibo, timezone.get_current_timezone())
            else:
                fecha_hora_recibo = ahora
                
            if entrega_str:
                dt_entrega = datetime.strptime(entrega_str, "%Y-%m-%dT%H:%M")
                fecha_hora_entrega = timezone.make_aware(dt_entrega, timezone.get_current_timezone())
            else:
                fecha_hora_entrega = ahora
        except (TypeError, ValueError) as e:
            messages.error(request, f"Datos o formato de fecha inválidos: {str(e)}")
            return _render_guarda(
                request,
                "guarda/minuta_form.html",
                {
                    "ambientes": ambientes,
                    "responsables": responsables,
                    "modo": "crear",
                    "hoy_str": hoy.isoformat(),
                    "fecha_hora_recibo_def": recibo_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                    "fecha_hora_entrega_def": entrega_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                },
            )

        guarda = GuardaSeguridad.objects.filter(usuario_id_usuario=usuario).first()
        if not guarda:
            messages.error(request, "Tu usuario no tiene registro activo como guarda.")
            return _render_guarda(
                request,
                "guarda/minuta_form.html",
                {
                    "ambientes": ambientes,
                    "responsables": responsables,
                    "modo": "crear",
                    "hoy_str": hoy.isoformat(),
                    "fecha_hora_recibo_def": recibo_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                    "fecha_hora_entrega_def": entrega_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                },
            )

        # Obtener y validar el ambiente seleccionado
        ambiente_id = request.POST.get("ambiente_id")
        if not ambiente_id:
            messages.error(request, "Error: Debe seleccionar un ambiente de formación.")
            return _render_guarda(
                request,
                "guarda/minuta_form.html",
                {
                    "ambientes": ambientes,
                    "responsables": responsables,
                    "modo": "crear",
                    "hoy_str": hoy.isoformat(),
                    "fecha_hora_recibo_def": recibo_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                    "fecha_hora_entrega_def": entrega_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                },
            )

        fecha_evaluar = fecha_hora_recibo.date()
        tiene_asignacion = AsignacionAmbiente.objects.filter(
            instructor_id=responsable_id,
            ambiente_id=ambiente_id,
            fecha_inicio__lte=fecha_evaluar,
            fecha_fin__gte=fecha_evaluar,
            estado='ACTIVO'
        ).exists()

        if not tiene_asignacion:
            messages.error(request, "Error: El ambiente seleccionado no está asignado al responsable en la fecha de recibido.")
            return _render_guarda(
                request,
                "guarda/minuta_form.html",
                {
                    "ambientes": ambientes,
                    "responsables": responsables,
                    "modo": "crear",
                    "hoy_str": hoy.isoformat(),
                    "fecha_hora_recibo_def": recibo_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                    "fecha_hora_entrega_def": entrega_str or ahora.strftime("%Y-%m-%dT%H:%M"),
                },
            )

        RegistroMinuta.objects.create(
            fecha_hora_recibo=fecha_hora_recibo,
            fecha_hora_entrega=fecha_hora_entrega,
            novedad=novedad,
            descripcion_min=descripcion,
            estado=estado,
            ambiente_id=int(ambiente_id),
            guarda_seguridad_usuario_id_usuario=guarda,
            responsable_id=responsable_id,
        )
        _sincronizar_estado_ambiente(int(ambiente_id))
        messages.success(request, "Minuta creada correctamente.")
        return _replace_redirect('guarda:guarda_minutas')

    return _render_guarda(
        request,
        "guarda/minuta_form.html",
        {
            "ambientes": ambientes,
            "responsables": responsables,
            "modo": "crear",
            "hoy_str": hoy.isoformat(),
            "fecha_hora_recibo_def": ahora.strftime("%Y-%m-%dT%H:%M"),
            "fecha_hora_entrega_def": ahora.strftime("%Y-%m-%dT%H:%M"),
        },
    )


@never_cache
def editar_minuta(request, minuta_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    minuta = get_object_or_404(RegistroMinuta, pk=minuta_id)
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    responsables = Instructor.objects.select_related("usuario_id_usuario").all()

    from django.utils import timezone
    ahora = timezone.localtime()
    hoy = ahora.date()

    if request.method == "POST":
        from django.utils import timezone
        
        # Validar y parsear fecha de entrega
        entrega_str = request.POST.get("fecha_hora_entrega")
        
        try:
            responsable_id = int(request.POST.get("responsable_id"))
            minuta.estado = (request.POST.get("estado") or "Ocupado").strip()
            minuta.novedad = (request.POST.get("novedad") or "").strip() or None
            minuta.descripcion_min = (request.POST.get("descripcion_min") or "").strip() or None

            if entrega_str:
                dt_entrega = datetime.strptime(entrega_str, "%Y-%m-%dT%H:%M")
                minuta.fecha_hora_entrega = timezone.make_aware(dt_entrega, timezone.get_current_timezone())

            # Obtener y validar el ambiente seleccionado
            ambiente_id = request.POST.get("ambiente_id")
            if not ambiente_id:
                messages.error(request, "Error: Debe seleccionar un ambiente de formación.")
                return _render_guarda(
                    request,
                    "guarda/minuta_form.html",
                    {
                        "minuta": minuta,
                        "ambientes": ambientes,
                        "responsables": responsables,
                        "modo": "editar",
                        "hoy_str": hoy.isoformat(),
                    },
                )

            fecha_evaluar = minuta.fecha_hora_recibo.date()
            tiene_asignacion = AsignacionAmbiente.objects.filter(
                instructor_id=responsable_id,
                ambiente_id=ambiente_id,
                fecha_inicio__lte=fecha_evaluar,
                fecha_fin__gte=fecha_evaluar,
                estado='ACTIVO'
            ).exists()

            if not tiene_asignacion:
                messages.error(request, "Error: El ambiente seleccionado no está asignado al responsable en la fecha de recibido.")
                return _render_guarda(
                    request,
                    "guarda/minuta_form.html",
                    {
                        "minuta": minuta,
                        "ambientes": ambientes,
                        "responsables": responsables,
                        "modo": "editar",
                        "hoy_str": hoy.isoformat(),
                    },
                )

            old_ambiente_id = minuta.ambiente_id
            minuta.responsable_id = responsable_id
            minuta.ambiente_id = int(ambiente_id)
            minuta.save()
            _sincronizar_estado_ambiente(int(ambiente_id))
            if old_ambiente_id and old_ambiente_id != int(ambiente_id):
                _sincronizar_estado_ambiente(old_ambiente_id)
            messages.success(request, "Minuta actualizada correctamente.")
            return _replace_redirect('guarda:guarda_minutas')
        except (TypeError, ValueError) as e:
            messages.error(request, f"No se pudo actualizar la minuta, revisa los campos: {str(e)}")

    return _render_guarda(
        request,
        "guarda/minuta_form.html",
        {
            "minuta": minuta,
            "ambientes": ambientes,
            "responsables": responsables,
            "modo": "editar",
            "hoy_str": hoy.isoformat(),
        },
    )


@require_POST
@never_cache
def eliminar_minuta(request, minuta_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    minuta = get_object_or_404(RegistroMinuta, pk=minuta_id)
    ambiente_id = minuta.ambiente_id
    minuta.delete()
    if ambiente_id:
        _sincronizar_estado_ambiente(ambiente_id)
    messages.success(request, "Minuta eliminada correctamente.")
    return _replace_redirect('guarda:guarda_minutas')


@never_cache
def detalle_minuta(request, minuta_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    minuta = get_object_or_404(
        RegistroMinuta.objects.select_related(
            "ambiente",
            "guarda_seguridad_usuario_id_usuario__usuario_id_usuario",
            "responsable__usuario_id_usuario",
        ),
        pk=minuta_id
    )
    return _render_guarda(request, "guarda/minuta_detalle.html", {"minuta": minuta})
