from django.urls import path
from . import views

urlpatterns = [
    path("aprendiz/", views.aprendiz_index, name="aprendiz_index"),
    path("aprendiz/asistencias/", views.listar_asistencias, name="listar_asistencias"),
    path(
        "aprendiz/asistencias/listado/",
        views.listar_asistencias_tabla,
        name="listar_asistencias_tabla",
    ),
    path("aprendiz/registrar/", views.registrar_aprendiz, name="guardar_aprendiz"),
    path(
        "aprendiz/asistencias/exportar/pdf/",
        views.exportar_asistencias_pdf,
        name="exportar_asistencias_pdf",
    ),
    path(
        "aprendiz/asistencias/exportar/excel/",
        views.exportar_asistencias_excel,
        name="exportar_asistencias_excel",
    ),
]
