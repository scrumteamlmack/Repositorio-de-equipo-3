<?php

namespace App\Http\Controllers;

use App\Models\Recurso;
use App\Models\TipoRecurso;
use App\Models\Ambiente;
use Illuminate\Http\Request;

class RecursoController extends Controller
{
    public function index()
    {
        $recursos = Recurso::with(['tipoRecurso', 'ambiente'])->get();
        return view('recursos.index', compact('recursos'));
    }

    public function create()
    {
        $tipos = TipoRecurso::all();
        $ambientes = Ambiente::all();
        return view('recursos.create', compact('tipos', 'ambientes'));
    }

    public function store(Request $request)
    {
        $request->validate([
            'serial_recurso' => 'required|string|max:100',
            'num_recurso' => 'required|integer',
            'nombre_recurso' => 'required|string|max:255',
            'tipo_recurso' => 'required|exists:tipo_recurso,id_tipo_recurso',
            'estado' => 'required|string',
            'observacion' => 'nullable|string',
            'ambiente_id' => 'required|exists:ambiente,id_ambiente'
        ]);

        Recurso::create($request->all());
        return redirect()->route('recursos.index')->with('success', 'Recurso creado correctamente.');
    }

    public function show($id)
    {
        $recurso = Recurso::with(['tipoRecurso', 'ambiente'])->findOrFail($id);
        return view('recursos.show', compact('recurso'));
    }

    public function edit($id)
    {
        $recurso = Recurso::findOrFail($id);
        $tipos = TipoRecurso::all();
        $ambientes = Ambiente::all();
        return view('recursos.edit', compact('recurso', 'tipos', 'ambientes'));
    }

    public function update(Request $request, $id)
    {
        $request->validate([
            'serial_recurso' => 'required|string|max:100',
            'num_recurso' => 'required|integer',
            'nombre_recurso' => 'required|string|max:255',
            'tipo_recurso' => 'required|exists:tipo_recurso,id_tipo_recurso',
            'estado' => 'required|string',
            'observacion' => 'nullable|string',
            'ambiente_id' => 'required|exists:ambiente,id_ambiente'
        ]);

        $recurso = Recurso::findOrFail($id);
        $recurso->update($request->all());

        return redirect()->route('recursos.index')->with('success', 'Recurso actualizado correctamente.');
    }

    public function destroy($id)
    {
        $recurso = Recurso::findOrFail($id);
        $recurso->delete();

        return redirect()->route('recursos.index')->with('success', 'Recurso eliminado correctamente.');
    }
}
