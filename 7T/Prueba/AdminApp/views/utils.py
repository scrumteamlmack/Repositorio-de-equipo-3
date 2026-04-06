
__all__ = ['MAX_MEDIUMINT', '_hash_nueva_contrasena', '_usuario_a_formulario', 'EMPTY_USER', 'EMPTY_RESOURCE', 'EMPTY_AMBIENTE', 'EMPTY_PROGRAMA', 'EMPTY_FICHA', '_hash_nueva_contrasena', '_usuario_a_formulario', '_render_admin', '_instructores_para_select', '_programa_form_desde_modelo', '_recurso_form_desde_modelo', '_ambiente_form_desde_modelo', '_context_form_programa', '_context_form_recurso', '_context_form_ambiente']
import hashlib
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
)


# Utils and constants

MAX_MEDIUMINT = 8_388_607

def _hash_nueva_contrasena(plain: str) -> str:
    """Mismo criterio que registros recientes en BD: SHA-256 en hexadecimal."""
    return hashlib.sha256(plain.encode("utf-8")).hexdigest()


def _usuario_a_formulario(u: Usuario, rol_id=None):
    if rol_id is None:
        ur = UserRol.objects.filter(id_usuario=u).first()
        rol_id = ur.id_rol_id if ur else ""
    return {
        "PNombre": u.p_nombre,
        "SNombre": u.s_nombre or "",
        "PApellido": u.p_apellido,
        "SApellido": u.s_apellido or "",
        "tipoDocumento": u.tipo_documento,
        "numDocumento": u.num_documento,
        "correo": u.correo,
        "rolSeleccionado": str(rol_id) if rol_id != "" else "",
    }


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



def _hash_nueva_contrasena(plain: str) -> str:
    """Mismo criterio que registros recientes en BD: SHA-256 en hexadecimal."""
    return hashlib.sha256(plain.encode("utf-8")).hexdigest()


def _usuario_a_formulario(u: Usuario, rol_id=None):
    if rol_id is None:
        ur = UserRol.objects.filter(id_usuario=u).first()
        rol_id = ur.id_rol_id if ur else ""
    return {
        "PNombre": u.p_nombre,
        "SNombre": u.s_nombre or "",
        "PApellido": u.p_apellido,
        "SApellido": u.s_apellido or "",
        "tipoDocumento": u.tipo_documento,
        "numDocumento": u.num_documento,
        "correo": u.correo,
        "rolSeleccionado": str(rol_id) if rol_id != "" else "",
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


def _instructores_para_select():
    filas = []
    qs = Instructor.objects.select_related("usuario_id_usuario").order_by(
        "usuario_id_usuario__p_apellido",
        "usuario_id_usuario__p_nombre",
    )
    for inst in qs:
        u = inst.usuario_id_usuario
        nombre = " ".join(filter(None, [u.p_nombre, u.p_apellido])).strip() or f"ID {inst.pk}"
        filas.append({"id": inst.pk, "nombre": nombre})
    return filas


def _programa_form_desde_modelo(p: Programas):
    return {
        "nombrePrograma": p.nombre_programa,
        "nivelFormacion": p.nivel_formacion,
        "duracion": p.duracion,
        "jornadaId": str(p.jornada_id),
        "modalidadId": str(p.modalidad_id),
        "coordinacionId": str(p.coordinacion_id),
    }


def _recurso_form_desde_modelo(r: Recursos):
    return {
        "nombre": r.nombre_recurso,
        "serial": r.serial_recurso,
        "numero": r.num_recurso,
        "idTipoRecurso": str(r.tipo_recurso_id),
        "idAmbiente": str(r.ambiente_id),
        "estado": (r.estado or "").strip(),
        "observacion": r.observacion or "",
    }


def _ambiente_form_desde_modelo(a: Ambiente):
    return {
        "numero": a.num_ambiente,
        "capacidad": a.capacidad,
        "tipo": a.tipo_ambiente,
        "estado": a.estado,
    }


def _context_form_programa(programa_dict=None):
    prog = EMPTY_PROGRAMA.copy()
    if programa_dict:
        prog.update(programa_dict)
    return {
        "programa": prog,
        "jornadas": list(Jornada.objects.all().order_by("id_jornada")),
        "modalidades": list(Modalidad.objects.all().order_by("id_modalidad")),
        "coordinaciones": list(Coordinacion.objects.all().order_by("id_coordinacion")),
    }


def _context_form_recurso(recurso_dict=None):
    rec = EMPTY_RESOURCE.copy()
    if recurso_dict:
        rec.update(recurso_dict)
    return {
        "recurso": rec,
        "tipos": list(TipoRecurso.objects.all().order_by("id_tipo_recurso")),
        "ambientes": list(Ambiente.objects.all().order_by("id_ambiente")),
    }


def _context_form_ambiente(ambiente_dict=None):
    amb = EMPTY_AMBIENTE.copy()
    if ambiente_dict:
        amb.update(ambiente_dict)
    return {"ambiente": amb}


