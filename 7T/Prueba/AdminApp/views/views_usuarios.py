import hashlib
import sys
import os
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
    Aprendiz,
    GuardaSeguridad,
    Coordinador,
    RegistroInasistencia,
    RegistroMinuta,
    RegistroIncidente,
    HistoricoIncidentes,
)
from LoginApp.forms import BaseUserForm, AprendizForm, InstructorForm, GuardaForm, CoordinadorForm


from .utils import *
from LoginApp.decorators import login_requerido, rol_requerido

def perfil(request):
    uid = request.session.get("usuario_id")
    if not uid:
        messages.warning(request, "Inicie sesión para ver su perfil.")
        login_url = reverse('login:login')
        return redirect(f"{login_url}?{urlencode({'next': reverse('admin_panel:perfil')})}")

    u = get_object_or_404(Usuario, pk=uid)
    roles_qs = UserRol.objects.filter(id_usuario=u).select_related("id_rol")
    roles_detalle = ", ".join(ur.id_rol.nombre_rol for ur in roles_qs) or "Sin rol asignado"
    es_admin = any(
        ur.id_rol.nombre_rol.lower() in ("admin", "administrador") for ur in roles_qs
    )
    nombre_completo = " ".join(
        filter(None, [u.p_nombre, u.s_nombre, u.p_apellido, u.s_apellido])
    ).strip() or u.correo

    return render(
        request,
        "perfil.html",
        {
            "usuario_perfil": {
                "id": u.id_usuario,
                "nombre_completo": nombre_completo,
                "p_nombre": u.p_nombre,
                "s_nombre": u.s_nombre,
                "p_apellido": u.p_apellido,
                "s_apellido": u.s_apellido,
                "tipo_documento": u.tipo_documento,
                "num_documento": u.num_documento,
                "correo": u.correo,
                "roles": roles_detalle,
            },
            "puede_editar_perfil": es_admin,
        },
    )


def listar_usuarios(request):
    roles_por_usuario = defaultdict(list)
    for ur in UserRol.objects.select_related("id_rol").all():
        roles_por_usuario[ur.id_usuario_id].append(ur.id_rol.nombre_rol)

    # Filtros multicriterio
    q = (request.GET.get("q") or "").strip()
    rol_filtro = (request.GET.get("rol") or "").strip().lower()

    qs = Usuario.objects.all().order_by("id_usuario")
    if q:
        qs = qs.filter(
            Q(p_nombre__icontains=q) | Q(s_nombre__icontains=q) |
            Q(p_apellido__icontains=q) | Q(s_apellido__icontains=q) |
            Q(correo__icontains=q) | Q(num_documento__icontains=q)
        )

    filas = []
    for u in qs:
        nombres = " ".join(filter(None, [u.p_nombre, u.s_nombre])).strip()
        apellidos = " ".join(filter(None, [u.p_apellido, u.s_apellido])).strip()
        roles = roles_por_usuario.get(u.id_usuario, [])
        rol_str = ", ".join(roles) if roles else "—"
        if rol_filtro and rol_filtro not in rol_str.lower():
            continue
        filas.append(
            {
                "id": u.id_usuario,
                "first_name": nombres or "—",
                "last_name": apellidos or "—",
                "email": u.correo,
                "rol": rol_str,
            }
        )

    # Exportación PDF
    if request.GET.get('export') == 'pdf':
        response, buffer = generar_pdf_response("usuarios.pdf")
        cabeceras = ["ID", "Nombre", "Apellido", "Correo", "Rol"]
        rows = [[str(f["id"]), f["first_name"], f["last_name"], f["email"], f["rol"]] for f in filas]
        construir_pdf(buffer, "Lista de Usuarios", cabeceras, rows)
        response.write(buffer.getvalue())
        return response

    # Exportación Excel
    if request.GET.get('export') == 'excel':
        response, wb, ws = generar_excel_response("usuarios.xlsx")
        cabeceras = ["ID", "Nombre", "Apellido", "Correo", "Rol"]
        rows = [[str(f["id"]), f["first_name"], f["last_name"], f["email"], f["rol"]] for f in filas]
        estilizar_excel(ws, cabeceras, rows, "Lista de Usuarios")
        return guardar_excel_en_response(response, wb)

    roles_disponibles = list(Rol.objects.all().order_by("nombre_rol"))
    filtros = {"q": q, "rol": rol_filtro}
    return _render_admin(request, "listarUsuarios.html", {
        "usuarios": filas,
        "filtros": filtros,
        "roles_disponibles": roles_disponibles,
    })


def exportar_usuarios_pdf(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'pdf'
    return listar_usuarios(request)


def exportar_usuarios_excel(request):
    request.GET = request.GET.copy()
    request.GET['export'] = 'excel'
    return listar_usuarios(request)


def form_usuario(request):
    if request.method == "POST":
        form = BaseUserForm(request.POST)
        if form.is_valid():
            try:
                with transaction.atomic():
                    # Hasheamos la contraseña
                    usuario = form.save(commit=False)
                    usuario.contrasena = _hash_nueva_contrasena(form.cleaned_data['contrasena'])
                    usuario.save()
                    
                    rol = form.cleaned_data['rol']
                    UserRol.objects.create(id_usuario=usuario, id_rol=rol)
                    
                    # Redirección según rol para completar datos
                    nombre_rol = rol.nombre_rol.lower()
                    if "aprendiz" in nombre_rol:
                        return redirect('admin_panel:crear_aprendiz_detalle', usuario_id=usuario.id_usuario)
                    elif "instructor" in nombre_rol:
                        return redirect('admin_panel:crear_instructor_detalle', usuario_id=usuario.id_usuario)
                    elif "guarda" in nombre_rol:
                        return redirect('admin_panel:crear_guarda_detalle', usuario_id=usuario.id_usuario)
                    elif "admin" in nombre_rol or "coordinador" in nombre_rol:
                        return redirect('admin_panel:crear_coordinador_detalle', usuario_id=usuario.id_usuario)
                    
                    messages.success(request, "Usuario registrado correctamente.")
                    return redirect('admin_panel:listar_usuarios')
            except IntegrityError:
                messages.error(request, "Error de integridad: El documento o correo ya existen.")
        else:
            for field, errors in form.errors.items():
                for error in errors:
                    messages.error(request, f"{field}: {error}")
    else:
        form = BaseUserForm()
    
    return _render_admin(request, "formUsuario.html", {"form": form})

def crear_aprendiz_detalle(request, usuario_id):
    usuario = get_object_or_404(Usuario, pk=usuario_id)
    if request.method == "POST":
        form = AprendizForm(request.POST)
        if form.is_valid():
            aprendiz = form.save(commit=False)
            aprendiz.usuario_id_usuario = usuario
            aprendiz.save()
            messages.success(request, "Datos de Aprendiz guardados correctamente.")
            return redirect('admin_panel:listar_usuarios')
    else:
        form = AprendizForm()
    return _render_admin(request, "formAprendiz.html", {"form": form, "usuario": usuario})

def crear_instructor_detalle(request, usuario_id):
    usuario = get_object_or_404(Usuario, pk=usuario_id)
    if request.method == "POST":
        form = InstructorForm(request.POST)
        if form.is_valid():
            instructor = form.save(commit=False)
            instructor.usuario_id_usuario = usuario
            instructor.save()
            messages.success(request, "Datos de Instructor guardados correctamente.")
            return redirect('admin_panel:listar_usuarios')
    else:
        form = InstructorForm()
    return _render_admin(request, "formInstructorDetalle.html", {"form": form, "usuario": usuario})

def crear_guarda_detalle(request, usuario_id):
    usuario = get_object_or_404(Usuario, pk=usuario_id)
    if request.method == "POST":
        form = GuardaForm(request.POST)
        if form.is_valid():
            guarda = form.save(commit=False)
            guarda.usuario_id_usuario = usuario
            guarda.save()
            messages.success(request, "Datos de Guarda guardados correctamente.")
            return redirect('admin_panel:listar_usuarios')
    else:
        form = GuardaForm()
    return _render_admin(request, "formGuardaDetalle.html", {"form": form, "usuario": usuario})

def crear_coordinador_detalle(request, usuario_id):
    usuario = get_object_or_404(Usuario, pk=usuario_id)
    if request.method == "POST":
        form = CoordinadorForm(request.POST)
        if form.is_valid():
            coordinador = form.save(commit=False)
            coordinador.usuario_id_usuario = usuario
            coordinador.save()
            messages.success(request, "Datos de Coordinador guardados correctamente.")
            return redirect('admin_panel:listar_usuarios')
    else:
        form = CoordinadorForm()
    return _render_admin(request, "formCoordinadorDetalle.html", {"form": form, "usuario": usuario})


def crear_usuario(request):
    return form_usuario(request)


def editar_usuario(request, usuario_id):
    u = get_object_or_404(Usuario, pk=usuario_id)
    roles = list(Rol.objects.all().order_by("id_rol"))
    ur = UserRol.objects.filter(id_usuario=u).first()
    rol_actual = ur.id_rol_id if ur else ""

    if request.method == "POST":
        p_nombre = request.POST.get("PNombre", "").strip()
        s_nombre = request.POST.get("SNombre", "").strip()
        p_apellido = request.POST.get("PApellido", "").strip()
        s_apellido = request.POST.get("SApellido", "").strip()
        tipo_doc = request.POST.get("tipoDocumento", "").strip()
        correo = request.POST.get("correo", "").strip()
        password = (request.POST.get("pass") or "").strip()
        rol_raw = request.POST.get("rolSeleccionado")

        if not p_nombre or not p_apellido or not tipo_doc or not correo:
            messages.error(request, "Complete los campos obligatorios.")
            return _render_admin(
                request,
                "editarUsuario.html",
                {
                    "roles": roles,
                    "usuario": _usuario_a_formulario(u, rol_actual),
                    "idUsuarioEditar": usuario_id,
                },
            )
        if not rol_raw:
            messages.error(request, "Seleccione un rol.")
            return _render_admin(
                request,
                "editarUsuario.html",
                {
                    "roles": roles,
                    "usuario": _usuario_a_formulario(u, rol_actual),
                    "idUsuarioEditar": usuario_id,
                },
            )
        try:
            rol_id = int(rol_raw)
        except ValueError:
            messages.error(request, "Rol no válido.")
            return _render_admin(
                request,
                "editarUsuario.html",
                {
                    "roles": roles,
                    "usuario": _usuario_a_formulario(u, rol_actual),
                    "idUsuarioEditar": usuario_id,
                },
            )
        if not Rol.objects.filter(pk=rol_id).exists():
            messages.error(request, "El rol indicado no existe.")
            return _render_admin(
                request,
                "editarUsuario.html",
                {
                    "roles": roles,
                    "usuario": _usuario_a_formulario(u, rol_actual),
                    "idUsuarioEditar": usuario_id,
                },
            )

        u.p_nombre = p_nombre
        u.s_nombre = s_nombre or None
        u.p_apellido = p_apellido
        u.s_apellido = s_apellido or None
        u.tipo_documento = tipo_doc
        u.correo = correo
        if password:
            u.contrasena = _hash_nueva_contrasena(password)

        try:
            with transaction.atomic():
                u.save()
                UserRol.objects.filter(id_usuario=u).delete()
                UserRol.objects.create(id_usuario=u, id_rol_id=rol_id)
        except IntegrityError:
            messages.error(request, "No se pudo actualizar el usuario.")
            return _render_admin(
                request,
                "editarUsuario.html",
                {
                    "roles": roles,
                    "usuario": _usuario_a_formulario(u, rol_actual),
                    "idUsuarioEditar": usuario_id,
                },
            )

        messages.success(request, "Usuario actualizado correctamente.")
        return redirect('admin_panel:listar_usuarios')

    return _render_admin(
        request,
        "editarUsuario.html",
        {
            "roles": roles,
            "usuario": _usuario_a_formulario(u, rol_actual),
            "idUsuarioEditar": usuario_id,
        },
    )


@require_POST
def eliminar_usuario(request, usuario_id):
    u = Usuario.objects.filter(pk=usuario_id).first()
    if not u:
        messages.error(request, "Usuario no encontrado.")
        return redirect('admin_panel:listar_usuarios')
    
    try:
        with transaction.atomic():
            # 1. Identificar registros de rol
            aprendiz = Aprendiz.objects.filter(usuario_id_usuario=u).first()
            instructor = Instructor.objects.filter(usuario_id_usuario=u).first()
            guarda = GuardaSeguridad.objects.filter(usuario_id_usuario=u).first()
            
            # 2. Eliminar dependencias de inasistencia (Aprendiz o Instructor)
            if aprendiz:
                RegistroInasistencia.objects.filter(aprendiz_usuario_id_usuario=aprendiz).delete()
            if instructor:
                RegistroInasistencia.objects.filter(instructor_usuario_id_usuario=instructor).delete()
            
            # 3. Eliminar dependencias de minuta (Guarda o Responsable/Instructor)
            if guarda:
                RegistroMinuta.objects.filter(guarda_seguridad_usuario_id_usuario=guarda).delete()
            if instructor:
                RegistroMinuta.objects.filter(responsable=instructor).delete()
            
            # 4. Eliminar incidentes (Usuario) e histórico (Incidentes del usuario)
            incidentes = RegistroIncidente.objects.filter(usuario_id_usuario=u)
            for inc in incidentes:
                HistoricoIncidentes.objects.filter(incidente=inc).delete()
            incidentes.delete()
            
            # 5. Eliminar roles específicos
            if aprendiz: aprendiz.delete()
            if instructor: instructor.delete()
            if guarda: guarda.delete()
            Coordinador.objects.filter(usuario_id_usuario=u).delete()
            
            # 6. Eliminar relación de roles y el usuario
            UserRol.objects.filter(id_usuario=u).delete()
            u.delete()
            
        messages.success(request, "Usuario y todos sus registros relacionados eliminados correctamente.")
    except Exception as e:
        messages.error(
            request,
            f"No se pudo eliminar el usuario debido a un error técnico: {str(e)}"
        )
    return redirect('admin_panel:listar_usuarios')


