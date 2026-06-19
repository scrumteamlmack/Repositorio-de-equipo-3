from django.urls import path

from . import views


app_name = 'instructor'

urlpatterns = [
    path("instructor/", views.instructor_index, name="instructor_index"),
    path("inicio-instructor/", views.inicio_instructor, name="inicio_instructor"),
    path("index-instructor/", views.index_instructor, name="index_instructor"),
    path("instructor/perfil/", views.perfil, name="perfil"),
    path('perfil/editar/', views.editar_perfil, name='editar_perfil'),
    path("instructor/fichas/", views.mis_fichas, name="mis_fichas"),
    path("instructor/fichas/<int:ficha_id>/aprendices/", views.ver_aprendices, name="ver_aprendices"),
    path("instructor/asistencias/", views.listar_asistencias_instructor, name="listar_asistencias_instructor"),
    path("instructor/asistencia/", views.listar_asistencia, name="listar_asistencia"),
    path("instructor/asistencias/nueva/", views.registrar_asistencia, name="registrar_asistencia"),
    path("instructor/asistencias/<int:asistencia_id>/editar/", views.editar_asistencia, name="editar_asistencia"),
    path("instructor/asistencias/<int:asistencia_id>/eliminar/", views.eliminar_asistencia, name="eliminar_asistencia"),
    path("instructor/asistencias/exportar/pdf/", views.exportar_pdf, name="exportar_pdf"),
    path("instructor/asistencias/exportar/excel/", views.exportar_excel, name="exportar_excel"),
    path("instructor/minutas/", views.listar_minutas, name="listar_minutas"),
    path("instructor/minutas/consultar/", views.consultar_minutas, name="consultar_minutas"),
    path("instructor/minutas/<int:minuta_id>/detalle/", views.detalle_minuta, name="detalle_minuta"),
    path("instructor/minutas/exportar/pdf/", views.exportar_minutas_pdf, name="exportar_minutas_pdf"),
    path("instructor/minutas/exportar/excel/", views.exportar_minutas_excel, name="exportar_minutas_excel"),
    path("instructor/incidentes/", views.listar_incidentes, name="listar_incidentes"),
    path("instructor/incidentes/<int:incidente_id>/detalle/", views.detalle_incidente, name="detalle_incidente"),
    path("instructor/incidentes/nuevo/", views.crear_incidente, name="crear_incidente"),
    path("instructor/incidentes/formulario/", views.form_incidente, name="form_incidente"),
    path("instructor/incidentes/<int:incidente_id>/editar/", views.editar_incidente, name="editar_incidente"),
    path("instructor/incidentes/<int:incidente_id>/eliminar/", views.eliminar_incidente, name="eliminar_incidente"),
    path("instructor/incidentes/exportar/pdf/", views.exportar_incidentes_pdf, name="exportar_incidentes_pdf"),
    path("instructor/incidentes/exportar/excel/", views.exportar_incidentes_excel, name="exportar_incidentes_excel"),
    path("instructor/traslados/", views.listar_traslados, name="listar_traslados"),
    path("instructor/traslados/nuevo/", views.form_traslado, name="form_traslado"),
    path("instructor/traslados/<int:traslado_id>/editar/", views.editar_traslado, name="editar_traslado"),
    path("instructor/traslados/<int:traslado_id>/eliminar/", views.eliminar_traslado, name="eliminar_traslado"),
    path("instructor/traslados/exportar/pdf/", views.exportar_traslados_pdf, name="exportar_traslados_pdf"),
    path("instructor/traslados/exportar/excel/", views.exportar_traslados_excel, name="exportar_traslados_excel"),
    path("instructor/ambientes/", views.listar_ambientes, name="listar_ambientes"),
    path("instructor/ambientes/consultar/", views.consultar_ambientes, name="consultar_ambientes"),
    path("instructor/ambientes/exportar/pdf/", views.exportar_ambientes_pdf, name="exportar_ambientes_pdf"),
    path("instructor/ambientes/exportar/excel/", views.exportar_ambientes_excel, name="exportar_ambientes_excel"),
    # Justificaciones
    path("instructor/justificaciones/", views.listar_justificaciones, name="listar_justificaciones"),
    path("instructor/justificaciones/<int:justificacion_id>/resolver/", views.resolver_justificacion, name="resolver_justificacion"),
    # Seguimiento de Incidentes
    path("instructor/incidentes/<int:incidente_id>/actualizar-estado/", views.actualizar_estado_incidente, name="actualizar_estado_incidente"),
]
