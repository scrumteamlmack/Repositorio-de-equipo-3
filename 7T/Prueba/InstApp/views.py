from types import SimpleNamespace

from django.contrib import messages
from django.shortcuts import redirect, render
from django.utils import timezone


def _fecha_hoy():
    return timezone.localdate().isoformat()


def _asistencia_vacia(asistencia_id=None):
    return SimpleNamespace(
        id=asistencia_id,
        aprendiz_id=None,
        instructor_id=None,
        jornada_id=None,
        estado="S",
        fecha=timezone.localdate(),
    )


def _traslado_vacio(traslado_id=None):
    return SimpleNamespace(
        id_traslado=traslado_id,
        recurso_id=None,
        ambiente_origen_id=None,
        ambiente_destino_id=None,
        fecha=timezone.localtime(),
        observacion="",
    )


def _incidente_vacio(incidente_id=None):
    return SimpleNamespace(
        id=incidente_id,
        descripcion="",
        fecha=timezone.localdate(),
        hora="08:00",
        ambiente=SimpleNamespace(id=None),
        tipo_incidente=SimpleNamespace(id=None),
    )


def _mensaje_pendiente(request, texto):
    messages.info(request, texto)


def instructor_index(request):
    return render(request, "instindex.html")


def inicio_instructor(request):
    return instructor_index(request)


def index_instructor(request):
    return instructor_index(request)


def mis_fichas(request):
    context = {"fichas": []}
    return render(request, "misFichas.html", context)


def ver_aprendices(request, ficha_id):
    context = {
        "ficha_id": ficha_id,
        "ficha": SimpleNamespace(id=ficha_id, num_ficha=ficha_id),
        "aprendices": [],
    }
    return render(request, "aprendicesFicha.html", context)


def listar_asistencias(request):
    return render(request, "asistencias/listarAsistencias.html", {"asistencias": []})


def listar_asistencia(request):
    return listar_asistencias(request)


def registrar_asistencia(request):
    if request.method == "POST":
        _mensaje_pendiente(
            request,
            "La logica para guardar asistencias se implementara despues.",
        )
        return redirect("listar_asistencias")

    context = {
        "aprendices": [],
        "instructores": [],
        "jornadas": [],
        "today": _fecha_hoy(),
    }
    return render(request, "asistencias/formAsistencia.html", context)


def editar_asistencia(request, asistencia_id):
    if request.method == "POST":
        _mensaje_pendiente(
            request,
            f"La actualizacion de la asistencia {asistencia_id} queda pendiente de implementar.",
        )
        return redirect("listar_asistencias")

    context = {
        "asistencia": _asistencia_vacia(asistencia_id),
        "aprendices": [],
        "instructores": [],
        "jornadas": [],
    }
    return render(request, "asistencias/editarAsistencia.html", context)


def eliminar_asistencia(request, asistencia_id):
    _mensaje_pendiente(
        request,
        f"La eliminacion de la asistencia {asistencia_id} se implementara luego.",
    )
    return redirect("listar_asistencias")


def exportar_pdf(request):
    _mensaje_pendiente(request, "La exportacion de asistencias a PDF aun no esta implementada.")
    return redirect("listar_asistencias")


def exportar_excel(request):
    _mensaje_pendiente(request, "La exportacion de asistencias a Excel aun no esta implementada.")
    return redirect("listar_asistencias")


def listar_minutas(request):
    return render(request, "consultarMinutas.html", {"minutas": []})


def consultar_minutas(request):
    return listar_minutas(request)


def exportar_minutas_pdf(request):
    _mensaje_pendiente(request, "La exportacion de minutas a PDF aun no esta implementada.")
    return redirect("listar_minutas")


def exportar_minutas_excel(request):
    _mensaje_pendiente(request, "La exportacion de minutas a Excel aun no esta implementada.")
    return redirect("listar_minutas")


def listar_incidentes(request):
    return render(request, "listarIncidentes.html", {"incidentes": []})


def crear_incidente(request):
    if request.method == "POST":
        _mensaje_pendiente(
            request,
            "La logica para guardar incidentes se implementara despues.",
        )
        return redirect("listar_incidentes")

    context = {
        "incidente": _incidente_vacio(),
        "ambientes": [],
        "tipos": [],
        "modo_edicion": False,
        "hoy": _fecha_hoy(),
    }
    return render(request, "formIncidente.html", context)


def form_incidente(request):
    return crear_incidente(request)


def editar_incidente(request, incidente_id):
    if request.method == "POST":
        _mensaje_pendiente(
            request,
            f"La actualizacion del incidente {incidente_id} queda pendiente de implementar.",
        )
        return redirect("listar_incidentes")

    context = {
        "incidente": _incidente_vacio(incidente_id),
        "ambientes": [],
        "tipos": [],
        "modo_edicion": True,
        "hoy": _fecha_hoy(),
    }
    return render(request, "formIncidente.html", context)


def eliminar_incidente(request, incidente_id):
    _mensaje_pendiente(
        request,
        f"La eliminacion del incidente {incidente_id} se implementara luego.",
    )
    return redirect("listar_incidentes")


def exportar_incidentes_pdf(request):
    _mensaje_pendiente(request, "La exportacion de incidentes a PDF aun no esta implementada.")
    return redirect("listar_incidentes")


def exportar_incidentes_excel(request):
    _mensaje_pendiente(request, "La exportacion de incidentes a Excel aun no esta implementada.")
    return redirect("listar_incidentes")


def listar_traslados(request):
    return render(request, "traslados/listarTraslados.html", {"traslados": []})


def form_traslado(request):
    if request.method == "POST":
        _mensaje_pendiente(
            request,
            "La logica para guardar traslados se implementara despues.",
        )
        return redirect("listar_traslados")

    context = {
        "traslado": _traslado_vacio(),
        "recursos": [],
        "ambientes": [],
        "modo_edicion": False,
    }
    return render(request, "traslados/formTraslado.html", context)


def editar_traslado(request, traslado_id):
    if request.method == "POST":
        _mensaje_pendiente(
            request,
            f"La actualizacion del traslado {traslado_id} queda pendiente de implementar.",
        )
        return redirect("listar_traslados")

    context = {
        "traslado": _traslado_vacio(traslado_id),
        "recursos": [],
        "ambientes": [],
        "modo_edicion": True,
    }
    return render(request, "traslados/formTraslado.html", context)


def eliminar_traslado(request, traslado_id):
    _mensaje_pendiente(
        request,
        f"La eliminacion del traslado {traslado_id} se implementara luego.",
    )
    return redirect("listar_traslados")


def exportar_traslados_pdf(request):
    _mensaje_pendiente(request, "La exportacion de traslados a PDF aun no esta implementada.")
    return redirect("listar_traslados")


def exportar_traslados_excel(request):
    _mensaje_pendiente(request, "La exportacion de traslados a Excel aun no esta implementada.")
    return redirect("listar_traslados")


def listar_ambientes(request):
    context = {
        "ambientes": [],
        "total_disponibles": 0,
        "total_ocupados": 0,
        "total_ambientes": 0,
    }
    return render(request, "ambientes.html", context)


def consultar_ambientes(request):
    return render(request, "consultarAmbientes.html", {"ambientes": []})


def exportar_ambientes_pdf(request):
    _mensaje_pendiente(request, "La exportacion de ambientes a PDF aun no esta implementada.")
    return redirect("consultar_ambientes")


def exportar_ambientes_excel(request):
    _mensaje_pendiente(request, "La exportacion de ambientes a Excel aun no esta implementada.")
    return redirect("consultar_ambientes")


def perfil(request):
    _mensaje_pendiente(request, "La vista de perfil del instructor queda pendiente de implementar.")
    return redirect("instructor_index")
