from django.urls import path

from AdminApp import views

app_name = 'admin_panel'

urlpatterns = [
    path("uadmin/", views.uadmin, name="uadmin"),
    path("panel/", views.admin_index, name="admin_index"),
    path("panel-alterno/", views.index_admin, name="index_admin"),
    path("logout/", views.cerrar_sesion, name="logout"),
    path("siza/", views.siza, name="siza"),
    path("perfil/", views.perfil, name="perfil"),
    # Usuarios
    path("usuarios/", views.listar_usuarios, name="listar_usuarios"),
    path("usuarios/nuevo/", views.crear_usuario, name="crear_usuario"),
    path("usuarios/formulario/", views.form_usuario, name="form_usuario"),
    path("usuarios/<int:usuario_id>/editar/", views.editar_usuario, name="editar_usuario"),
    path("usuarios/<int:usuario_id>/eliminar/", views.eliminar_usuario, name="eliminar_usuario"),
    path("usuarios/instructor/", views.form_instructor, name="form_instructor"),
    path("usuarios/coordinador/", views.form_coordinador, name="form_coordinador"),
    path("usuarios/guarda/", views.form_guarda, name="form_guarda"),
    path("usuarios/<int:usuario_id>/aprendiz/", views.crear_aprendiz_detalle, name="crear_aprendiz_detalle"),
    path("usuarios/<int:usuario_id>/instructor/", views.crear_instructor_detalle, name="crear_instructor_detalle"),
    path("usuarios/<int:usuario_id>/guarda_detalle/", views.crear_guarda_detalle, name="crear_guarda_detalle"),
    path("usuarios/<int:usuario_id>/coordinador/", views.crear_coordinador_detalle, name="crear_coordinador_detalle"),
    path("usuarios/exportar/pdf/", views.exportar_usuarios_pdf, name="exportar_usuarios_pdf"),
    path("usuarios/exportar/excel/", views.exportar_usuarios_excel, name="exportar_usuarios_excel"),
    path("usuarios/importar/csv/", views.importar_usuarios_csv, name="importar_usuarios_csv"),
    # Fichas
    path("fichas/", views.listar_fichas, name="listar_fichas"),
    path("fichas/nueva/", views.crear_ficha, name="crear_ficha"),
    path("fichas/<int:ficha_id>/editar/", views.editar_ficha, name="editar_ficha"),
    path("fichas/<int:ficha_id>/eliminar/", views.eliminar_ficha, name="eliminar_ficha"),
    path("fichas/exportar/pdf/", views.exportar_fichas_pdf, name="exportar_fichas_pdf"),
    path("fichas/exportar/excel/", views.exportar_fichas_excel, name="exportar_fichas_excel"),
    # Programas
    path("programas/", views.listar_programas, name="listar_programas"),
    path("programas/nuevo/", views.crear_programa, name="crear_programa"),
    path("programas/<int:programa_id>/editar/", views.editar_programa, name="editar_programa"),
    path("programas/<int:programa_id>/eliminar/", views.eliminar_programa, name="eliminar_programa"),
    path("programas/exportar/pdf/", views.exportar_programas_pdf, name="exportar_programas_pdf"),
    path("programas/exportar/excel/", views.exportar_programas_excel, name="exportar_programas_excel"),
    # Recursos
    path("recursos/", views.listar_recursos, name="listar_recursos"),
    path("recursos/nuevo/", views.crear_recurso, name="crear_recurso"),
    path("recursos/<int:recurso_id>/editar/", views.editar_recurso, name="editar_recurso"),
    path("recursos/<int:recurso_id>/eliminar/", views.eliminar_recurso, name="eliminar_recurso"),
    path("recursos/exportar/pdf/", views.exportar_recursos_pdf, name="exportar_recursos_pdf"),
    path("recursos/exportar/excel/", views.exportar_recursos_excel, name="exportar_recursos_excel"),
    # Ambientes
    path("ambientes/", views.listar_ambientes, name="listar_ambientes"),
    path("ambientes/nuevo/", views.crear_ambiente, name="crear_ambiente"),
    path("ambientes/<int:ambiente_id>/editar/", views.editar_ambiente, name="editar_ambiente"),
    path("ambientes/<int:ambiente_id>/eliminar/", views.eliminar_ambiente, name="eliminar_ambiente"),
    path("ambientes/exportar/pdf/", views.exportar_ambientes_pdf, name="exportar_ambientes_pdf"),
    path("ambientes/exportar/excel/", views.exportar_ambientes_excel, name="exportar_ambientes_excel"),

    # Asignaciones de Ambientes
    path("ambientes/asignaciones/", views.listar_asignaciones_ambientes, name="listar_asignaciones_ambientes"),
    path("ambientes/asignaciones/nueva/", views.crear_asignacion_ambiente, name="crear_asignacion_ambiente"),
    path("ambientes/asignaciones/<int:asignacion_id>/eliminar/", views.eliminar_asignacion_ambiente, name="eliminar_asignacion_ambiente"),
]
