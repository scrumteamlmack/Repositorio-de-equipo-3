<?php

namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Http\RedirectResponse;
use Illuminate\Support\Facades\DB;

class GuardaController extends Controller
{
    public function create($id_usuario)
    {
        $usuario = Usuario::findOrFail($id_usuario);
        return view('guarda.create', compact('usuario'));
    }

    public function store(Request $request)
    {
        $request->validate([
            'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
            'turno' => 'required|string',
            'fecha_ingreso' => 'required|date',
            'estado' => 'required|string',
        ]);

        DB::table('guarda_seguridad')->insert([
            'Usuario_id_usuario' => $request->Usuario_id_usuario,
            'turno' => $request->turno,
            'fecha_ingreso' => $request->fecha_ingreso,
            'estado' => $request->estado,
        ]);

        return redirect()->route('usuarios.index')->with('success', 'Guarda de seguridad registrado correctamente.');
    }
}
