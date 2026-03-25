from collections import defaultdict

from django.contrib import messages
from django.contrib.auth import logout
from django.shortcuts import redirect, render
from django.views.generic import ListView, CreateView, UpdateView, DeleteView, View

from LoginApp.models import Usuario, UserRol, Ficha, Rol, Programas, Recursos, Ambiente


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
    roles_por_usuario = defaultdict(list)
    for ur in UserRol.objects.select_related("id_rol").all():
        roles_por_usuario[ur.id_usuario_id].append(ur.id_rol.nombre_rol)

    filas = []
    for u in Usuario.objects.all().order_by("id_usuario"):
        nombres = " ".join(filter(None, [u.p_nombre, u.s_nombre])).strip()
        apellidos = " ".join(filter(None, [u.p_apellido, u.s_apellido])).strip()
        roles = roles_por_usuario.get(u.id_usuario, [])
        filas.append(
            {
                "id": u.id_usuario,
                "first_name": nombres or "—",
                "last_name": apellidos or "—",
                "email": u.correo,
                "rol": ", ".join(roles) if roles else "—",
            }
        )
    return _render_admin(request, "listarUsuarios.html", {"usuarios": filas})


def form_usuario(request):
    roles = Rol.objects.all()
    return _render_admin(request, "formUsuario.html", {"roles" : roles})


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
    Listado_fichas = defaultdict(list)
    for fi in Ficha.objects.select_related("instructor_usuario_id_usuario").all():
        Listado_fichas[fi.idficha].append(fi.instructor_usuario_id_usuario.usuario_id_usuario.p_nombre)

    filas = []
    for f in Ficha.objects.all().order_by("num_ficha"):
        NumFicha = str(f.num_ficha).strip()
        Instructor = " ".join(filter(None, [f.instructor_usuario_id_usuario.usuario_id_usuario.p_nombre, f.instructor_usuario_id_usuario.usuario_id_usuario.p_apellido])).strip()
        filas.append(
            {
                "id": f.pk ,
                "Num_ficha": NumFicha or "—",
                "Instructor": Instructor or "—",
            }
        )
    return _render_admin(request, "listarFichas.html", {"fichas": filas})


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
    Listar_program = defaultdict(list)
    for pr in Programas.objects.select_related("jornada").all():
        Listar_program[pr.id_programas].append(pr.jornada.nombre_jornada)
    filas = []
    for p in Programas.objects.all().order_by("id_programas"):
        nombre_p = " ".join(filter(None, [p.nombre_programa])).strip()
        Nivel_p = " ".join(filter(None, [p.nivel_formacion])).strip()
        Duracion_p = " ".join(filter(None, [p.duracion])).strip()
        Jornada_p = " ".join(filter(None, [p.jornada.nombre_jornada])).strip()
        Modalidad_p = " ".join(filter(None, [p.modalidad.nombre_modalidad])).strip()
        Coordinacion_p = " ".join(filter(None, [p.coordinacion.nombre_coordinacion])).strip()
        filas.append(
            {
                "p_id": p.pk,
                "p_nombre": nombre_p or "—",
                "p_nivel": Nivel_p or "—",
                "p_duracion": Duracion_p or "—",
                "p_jornada": Jornada_p or "—",
                "p_modalidad": Modalidad_p or "—",
                "p_coordinacion": Coordinacion_p or "—",

            }
        )
    return _render_admin(request, "listarProgramas.html", {"programas": filas})


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
    Listar_recur = defaultdict(list)
    for re in Recursos.objects.select_related("tipo_recurso").all():
        Listar_recur[re.id_recurso].append(re.tipo_recurso.id_tipo_recurso)
    filas = []
    for r in Recursos.objects.all().order_by("id_recurso"):
        nombre_r = " ".join(filter(None, [r.nombre_recurso])).strip()
        serial_r = " ".join(filter(None, [r.serial_recurso])).strip()
        numero_r = str(r.num_recurso).strip()
        tipo_r = " ".join(filter(None, [r.tipo_recurso.recurso_tipo])).strip()
        estado_r = " ".join(filter(None, [r.estado])).strip()
        observacion_r = " ".join(filter(None, [r.observacion])).strip()
        ambiente_r = str(r.ambiente.num_ambiente).strip()

        filas.append(
            {
                "r_id": r.pk,
                "r_nombre": nombre_r or "—",
                "r_serial": serial_r or "—",
                "r_numero": numero_r or "—",
                "r_tipo": tipo_r or "—",
                "r_estado": estado_r or "—",
                "r_observacion": observacion_r or "—",
                "r_ambiente": ambiente_r or "—",
            }
        )
    return _render_admin(request, "listarRecursos.html", {"recursos": filas})


def crear_recurso(request):
    return _render_admin(request, "formRecurso.html")


def editar_recurso(request, recurso_id):
    return _render_admin(request, "editarRecurso.html")


def eliminar_recurso(request, recurso_id):
    messages.info(request, f"Eliminar recurso {recurso_id} aun no esta implementado.")
    return redirect("listar_recursos")


def listar_ambientes(request):

    filas = []
    for am in Ambiente.objects.all().order_by("id_ambiente"):
        num_am = str(am.num_ambiente).strip()
        capaci_am = str(am.capacidad).strip()
        tipo_am = " ".join(filter(None, [am.tipo_ambiente])).strip()
        estado_am = " ".join(filter(None, [am.estado])).strip()

        filas.append(
            {
                "am_id": am.pk,
                "am_num": num_am or "—",
                "am_capaci": capaci_am or "—",
                "am_tipo": tipo_am or "—",
                "am_estado": estado_am or "—",
            }
        )
    return _render_admin(request, "listarAmbientes.html", {"ambientes": filas})


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