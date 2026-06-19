from django.urls import path

from . import views


app_name = 'guarda'

urlpatterns = [
    path("guarda/panel/", views.guarda_index, name="guarda_index"),
    path("guarda/logout/", views.cerrar_sesion_guarda, name="guarda_logout"),
    path("guarda/perfil/", views.mi_perfil, name="guarda_perfil"),
    path("guarda/minutas/", views.listar_minutas, name="guarda_minutas"),
    path("guarda/minutas/descargar/excel/", views.exportar_minutas_excel, name="guarda_minutas_excel"),
    path("guarda/minutas/descargar/pdf/", views.exportar_minutas_pdf, name="guarda_minutas_pdf"),
    path("guarda/minutas/nueva/", views.crear_minuta, name="guarda_minuta_crear"),
    path("guarda/minutas/resolver-ambiente/", views.resolver_ambiente_ajax, name="guarda_minuta_resolver_ambiente"),
    path("guarda/minutas/<int:minuta_id>/editar/", views.editar_minuta, name="guarda_minuta_editar"),
    path("guarda/minutas/<int:minuta_id>/eliminar/", views.eliminar_minuta, name="guarda_minuta_eliminar"),
    path("guarda/minutas/<int:minuta_id>/detalle/", views.detalle_minuta, name="guarda_minuta_detalle"),
    path("guarda/incidentes/", views.listar_incidentes, name="guarda_incidentes"),
    path("guarda/incidentes/descargar/excel/", views.exportar_incidentes_excel, name="guarda_incidentes_excel"),
    path("guarda/incidentes/descargar/pdf/", views.exportar_incidentes_pdf, name="guarda_incidentes_pdf"),
    path("guarda/incidentes/nuevo/", views.crear_incidente, name="guarda_incidente_crear"),
    path("guarda/incidentes/<int:incidente_id>/editar/", views.editar_incidente, name="guarda_incidente_editar"),
    path("guarda/incidentes/<int:incidente_id>/eliminar/", views.eliminar_incidente, name="guarda_incidente_eliminar"),
    path("guarda/incidentes/<int:incidente_id>/detalle/", views.detalle_incidente, name="guarda_incidente_detalle"),
    path("guarda/incidentes/<int:incidente_id>/actualizar-estado/", views.actualizar_estado_incidente, name="guarda_incidente_actualizar_estado"),
    path("guarda/traslados/", views.listar_traslados, name="guarda_traslados"),
    path("guarda/traslados/descargar/excel/", views.exportar_traslados_excel, name="guarda_traslados_excel"),
    path("guarda/traslados/descargar/pdf/", views.exportar_traslados_pdf, name="guarda_traslados_pdf"),
    path("guarda/traslados/resolver-instructor/", views.resolver_instructor_origen_ajax, name="guarda_traslado_resolver_instructor"),
    path("guarda/traslados/nuevo/", views.crear_traslado, name="guarda_traslado_crear"),
    path("guarda/traslados/<int:traslado_id>/editar/", views.editar_traslado, name="guarda_traslado_editar"),
    path("guarda/traslados/<int:traslado_id>/eliminar/", views.eliminar_traslado, name="guarda_traslado_eliminar"),
    path("guarda/ambientes/", views.listar_ambientes, name="guarda_ambientes"),
    path("guarda/ambientes/descargar/excel/", views.exportar_ambientes_excel, name="guarda_ambientes_excel"),
    path("guarda/ambientes/descargar/pdf/", views.exportar_ambientes_pdf, name="guarda_ambientes_pdf"),
    path("guarda/ambientes/nuevo/", views.crear_ambiente, name="guarda_ambiente_crear"),
    path("guarda/ambientes/<int:ambiente_id>/editar/", views.editar_ambiente, name="guarda_ambiente_editar"),
    path("guarda/ambientes/<int:ambiente_id>/eliminar/", views.eliminar_ambiente, name="guarda_ambiente_eliminar"),
]
