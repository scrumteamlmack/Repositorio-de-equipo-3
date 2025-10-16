<?php

namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class GuardaController extends Controller
{
    /**
     * Mostrar el perfil del guarda autenticado
     */
    public function perfil()
    {
        /** @var Usuario $usuario */
        $usuario = Auth::user();

        return view('guarda.perfil', compact('usuario'));
    }

    /**
     * Mostrar formulario para editar perfil
     */
    public function editarPerfil()
    {
        /** @var Usuario $usuario */
        $usuario = Auth::user();

        return view('guarda.editar', compact('usuario'));
    }

    /**
     * Actualizar perfil del guarda
     */
    public function actualizarPerfil(Request $request)
    {
        $usuario = Auth::user();

        $request->validate([
            'p_nombre' => 'required|string|max:50',
            'p_apellido' => 'required|string|max:50',
            'correo' => 'required|email|max:100',
            'turno' => 'nullable|string|max:20',
            'tipo_documento' => 'required|string|max:20',
            'num_documento' => 'required|string|max:20',
        ]);

        // Asignar atributos manualmente
        $usuario->p_nombre = $request->p_nombre;
        $usuario->p_apellido = $request->p_apellido;
        $usuario->correo = $request->correo;
        $usuario->tipo_documento = $request->tipo_documento;
        $usuario->num_documento = $request->num_documento;

        // Guardar cambios del usuario
        $usuario->save(); // debería funcionar ahora

        // Actualizar turno del guarda
        if ($usuario->guarda_seguridad) {
            $usuario->guarda_seguridad->turno = $request->turno ?? $usuario->guarda_seguridad->turno ?? '';
            $usuario->guarda_seguridad->save();
        }

        return redirect()->route('guarda.perfil')->with('success', 'Perfil actualizado correctamente.');
    }


    /**
     * Crear un nuevo registro de guarda (opcional para administrador)
     */
    public function create($id_usuario)
    {
        $usuario = Usuario::findOrFail($id_usuario);
        return view('guarda.create', compact('usuario'));
    }

    /**
     * Guardar nuevo registro de guarda
     */
    public function store(Request $request)
    {
        $request->validate([
            'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
            'turno' => 'required|string',
            'fecha_ingreso' => 'required|date',
            'estado' => 'required|string',
        ]);

        // Insertar en tabla guarda_seguridad
        DB::table('guarda_seguridad')->insert([
            'Usuario_id_usuario' => $request->Usuario_id_usuario,
            'turno' => $request->turno,
            'fecha_ingreso' => $request->fecha_ingreso,
            'estado' => $request->estado,
        ]);

        return redirect()->route('usuarios.index')->with('success', 'Guarda registrado correctamente.');
    }
}
