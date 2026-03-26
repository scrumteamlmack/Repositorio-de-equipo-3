from django.http import HttpResponse
from django.shortcuts import render


def aprendiz_index(request):
    return render(request, "AprenApp/index.html")


def listar_asistencias(request):
    return render(request, "AprenApp/misAsistencias.html", {"asistencias": []})


def listar_asistencias_tabla(request):
    return render(
        request,
        "AprenApp/asistencias/listarAsistencias.html",
        {"asistencias": []},
    )


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

