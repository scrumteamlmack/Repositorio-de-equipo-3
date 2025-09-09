<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Aprendiz;
use App\Models\Usuario;
use App\Models\Programa;
use App\Models\Ficha;
use Illuminate\Http\RedirectResponse;
use Illuminate\Views\Views;
use Illuminate\Support\Facades\DB;

class AprendizController extends Controller
{
    public function index()
    {
        $aprendices = Aprendiz::with('usuario')->get();
        return view('aprendiz.index', compact('aprendices'));
    }

public function create(Request $request)
{
    $usuario_id = $request->get('usuario_id');  // viene de la redirección
    $programas = Programa::all();
    $fichas = Ficha::all();

    return view('aprendiz.create', compact('usuario_id', 'programas', 'fichas'));
}




public function store(Request $request)
{
    $validated = $request->validate([
        'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
        'programas_id_programas' => 'required|exists:programas,id_programas',
        'ficha_idficha' => 'required|exists:ficha,idficha',
    ]);

    Aprendiz::create($validated);

    return redirect()->route('aprendiz.index')
        ->with('success', 'Aprendiz creado correctamente.');
}




    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        //
    }
}
