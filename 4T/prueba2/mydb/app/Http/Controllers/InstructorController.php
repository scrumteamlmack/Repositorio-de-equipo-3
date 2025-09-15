<?php

namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class InstructorController extends Controller
{
    public function create($id_usuario)
    {
        $usuario = Usuario::findOrFail($id_usuario);
        $coordinaciones = DB::table('coordinacion')->get(); // Traer coordinaciones disponibles

        return view('instructor.create', compact('usuario', 'coordinaciones'));
    }

    public function store(Request $request)
    {
        $request->validate([
            'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
            'telefono' => 'required|string|max:20',
            'coordinacion_id_coordinacion' => 'required|exists:coordinacion,id_coordinacion',
            'estado' => 'required|in:Activo,Inactivo',
        ]);

        // Obtener el email desde la tabla usuario
        $usuario = DB::table('usuario')->where('id_usuario', $request->Usuario_id_usuario)->first();

        DB::table('instructor')->insert([
            'Usuario_id_usuario' => $request->Usuario_id_usuario,
            'email' => $usuario->correo, // se guarda el email del usuario
            'telefono' => $request->telefono,
            'coordinacion_id_coordinacion' => $request->coordinacion_id_coordinacion, // FK en lugar de texto
            'estado' => $request->estado,
        ]);

        return redirect()->route('usuarios.index')->with('success', 'Instructor registrado correctamente.');
    }
}
