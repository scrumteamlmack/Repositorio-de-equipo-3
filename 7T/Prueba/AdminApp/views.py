from django.contrib import messages
from django.contrib.auth import logout
from django.shortcuts import redirect, render


EMPTY_USER = {
    "PNombre": "",
    "SNombre": "",
    "PApellido": "",
    "SApellido": "",
    "tipoDocumento": "",
    "numDocumento": "",
    "correo": "",
    "rolSeleccionado": "",
}

EMPTY_RESOURCE = {
    "nombre": "",
    "serial": "",
    "numero": "",
    "idTipoRecurso": "",
    "idAmbiente": "",
    "estado": "",
    "observacion": "",
}

EMPTY_AMBIENTE = {
    "numero": "",
    "capacidad": "",
    "tipo": "",
    "estado": "",
}

EMPTY_PROGRAMA = {
    "nombrePrograma": "",
    "nivelFormacion": "",
    "duracion": "",
    "jornadaId": "",
    "modalidadId": "",
    "coordinacionId": "",
}

EMPTY_FICHA = {
    "numFicha": "",
}


def _render_admin(request, template_name, extra_context=None):
    context = {
        "roles": [],
        "usuarios": [],
        "fichas": [],
        "programas": [],
        "recursos": [],
        "ambientes": [],
        "tipos": [],
        "jornadas": [],
        "modalidades": [],
        "coordinaciones": [],
        "instructores": [],
        "turnos": ["Manana", "Tarde", "Noche"],
        "usuario": EMPTY_USER.copy(),
        "recurso": EMPTY_RESOURCE.copy(),
        "ambiente": EMPTY_AMBIENTE.copy(),
        "programa": EMPTY_PROGRAMA.copy(),
        "ficha": EMPTY_FICHA.copy(),
    }
    if extra_context:
        context.update(extra_context)
    return render(request, template_name, context)


def cerrar_sesion(request):
    if request.user.is_authenticated:
        logout(request)
        messages.success(request, "Te saliste primo")
    return redirect("siza")


def admin_index(request):
    return _render_admin(request, "admin_index.html")


def uadmin(request):
    return redirect("admin_index")


def index_admin(request):
    return _render_admin(request, "indexAdmin.html")


def siza(request):
    return render(request, "siza.html")


def perfil(request):
    messages.info(request, "La vista de perfil aun no tiene template propio saramanbiche.")
    return redirect("admin_index")


def listar_usuarios(request):
    return _render_admin(request, "listarUsuarios.html")


def form_usuario(request):
    return _render_admin(request, "formUsuario.html")


def crear_usuario(request):
    return form_usuario(request)


def editar_usuario(request, usuario_id):
    return _render_admin(
        request,
        "editarUsuario.html",
        {
            "idUsuarioEditar": usuario_id,
        },
    )


def eliminar_usuario(request, usuario_id):
    messages.info(request, f"Eliminar usuario {usuario_id} aun no esta implementado.")
    return redirect("listar_usuarios")


def form_instructor(request):
    return _render_admin(request, "formInstructor.html")


def form_coordinador(request):
    return _render_admin(request, "formCoordinador.html")


def form_guarda(request):
    return _render_admin(request, "formGuarda.html")


def listar_fichas(request):
    return _render_admin(request, "listarFichas.html")


def crear_ficha(request):
    return _render_admin(request, "formFicha.html")


def editar_ficha(request, ficha_id):
    return _render_admin(
        request,
        "formFicha.html",
        {
            "idFichaEditar": ficha_id,
        },
    )


def eliminar_ficha(request, ficha_id):
    messages.info(request, f"Eliminar ficha {ficha_id} aun no esta implementado.")
    return redirect("listar_fichas")


def listar_programas(request):
    return _render_admin(request, "listarProgramas.html")


def crear_programa(request):
    return _render_admin(request, "formPrograma.html")


def editar_programa(request, programa_id):
    return _render_admin(
        request,
        "formPrograma.html",
        {
            "idProgramaEditar": programa_id,
        },
    )


def eliminar_programa(request, programa_id):
    messages.info(request, f"Eliminar programa {programa_id} aun no esta implementado.")
    return redirect("listar_programas")


def listar_recursos(request):
    return _render_admin(request, "listarRecursos.html")


def crear_recurso(request):
    return _render_admin(request, "formRecurso.html")


def editar_recurso(request, recurso_id):
    return _render_admin(request, "editarRecurso.html")


def eliminar_recurso(request, recurso_id):
    messages.info(request, f"Eliminar recurso {recurso_id} aun no esta implementado.")
    return redirect("listar_recursos")


def listar_ambientes(request):
    return _render_admin(request, "listarAmbientes.html")


def crear_ambiente(request):
    return _render_admin(request, "formAmbiente.html")


def editar_ambiente(request, ambiente_id):
    return _render_admin(request, "editarAmbiente.html")


def eliminar_ambiente(request, ambiente_id):
    messages.info(request, f"Eliminar ambiente {ambiente_id} aun no esta implementado.")
    return redirect("listar_ambientes")
























































































































##⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠛⠻⣿⣿⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣦⠀⠀⠀⠀⠀⠀⠀⠀⢀⣤⣄⡀⠀⢻⣿⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠸⣿⣿⣿⠃⢰⣿⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⣿⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣿⣿⣿⡆⠀⠀⠀⠀⠀⠀⢶⣶⣶⣾⣿⣿⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⢠⡀⠐⠀⠀⠀⠻⢿⣿⣿⣿⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⢸⣷⡄⠀⠣⣄⡀⠀⠉⠛⢿⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠀⣿⣿⣦⠀⠹⣿⣷⣶⣦⣼⣿⣿⣿⣿
##⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣼⣿⣿⣿⣷⣄⣸⣿⣿⣿⣿⣿⣿⣿⣿ Mas bien siga revisando el codigo menor
## easter egg 1/10