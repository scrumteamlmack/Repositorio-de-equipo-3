<?php

namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Http\RedirectResponse;
use Illuminate\Support\Facades\DB;

class AprendizController extends Controller
{
public function create($id_usuario)
{
    $usuario = Usuario::findOrFail($id_usuario);
    $fichas = DB::table('ficha')->get();
    $programas = DB::table('programas')->get();

    // Pasamos las 3 variables con nombres coherentes
    return view('aprendiz.create', compact('usuario', 'fichas', 'programas'));
}




public function store(Request $request)
{
    $request->validate([
        'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
        'ficha_idficha' => 'required|exists:ficha,idficha',
        'programas_id_programas' => 'required|exists:programas,id_programas',
    ]);

    DB::table('aprendiz')->insert([
        'Usuario_id_usuario' => $request->Usuario_id_usuario,
        'ficha_idficha' => $request->ficha_idficha,
        'programas_id_programas' => $request->programas_id_programas,
    ]);

    return redirect()->route('usuarios.index')->with('success', 'Aprendiz registrado correctamente.');
}



}

