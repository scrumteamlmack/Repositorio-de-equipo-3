<?php

namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class AdminController extends Controller
{
    public function index()
    {
        // Traer todos los usuarios de la tabla usuario
        $usuarios = DB::table('usuario')->get();

        // Retornar la vista con los datos
        return view('usuarios.index', compact('usuarios'));
    }
    // --- FORMULARIO COORDINADOR ---
    public function createCoordinador($id_usuario)
    {
        // Traemos el usuario
        $usuario = Usuario::findOrFail($id_usuario);

        // Traemos las coordinaciones disponibles
        $coordinaciones = DB::table('coordinacion')->get();

        return view('administrador.create', compact('usuario', 'coordinaciones'));
    }

    // --- GUARDAR COORDINADOR ---
    public function storeCoordinador(Request $request)
    {
        $request->validate([
            'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
            'coordinacion_id_coordinacion' => 'required|exists:coordinacion,id_coordinacion',
        ]);

        // Verificar si ya existe como coordinador
        $already = DB::table('coordinador')
            ->where('Usuario_id_usuario', $request->Usuario_id_usuario)
            ->exists();

        if ($already) {
            return redirect()->route('usuarios.index')
                ->with('error', 'Este usuario ya está registrado como coordinador.');
        }

        // Insertar registro
        DB::table('coordinador')->insert([
            'Usuario_id_usuario' => $request->Usuario_id_usuario,
            'coordinacion_id_coordinacion' => $request->coordinacion_id_coordinacion,
        ]);

        return redirect()->route('usuarios.index')
            ->with('success', 'Coordinador registrado correctamente.');
    }
}
