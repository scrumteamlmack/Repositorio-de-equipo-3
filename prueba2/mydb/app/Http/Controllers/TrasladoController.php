<?php

namespace App\Http\Controllers;

use App\Models\TrasladoRecurso;
use App\Models\Recurso;
use App\Models\Ambiente;
use Illuminate\Http\Request;

class TrasladoController extends Controller
{
    public function index()
    {
        $traslados = TrasladoRecurso::with(['recurso', 'ambienteOrigen', 'ambienteDestino'])->get();
        return view('traslados.index', compact('traslados'));
    }

    public function create()
    {
        $recursos = Recurso::all();
        $ambientes = Ambiente::all();
        return view('traslados.create', compact('recursos', 'ambientes'));
    }

    public function store(Request $request)
    {
        $data = $request->validate([
            'recurso_id' => 'required|exists:recursos,id_recurso',
            'ambiente_origen' => 'required|exists:ambiente,id_ambiente',
            'ambiente_destino' => 'required|exists:ambiente,id_ambiente',
            'fecha_traslado' => 'required|date',
            'observacion' => 'nullable|string',
        ]);

        // Guardar traslado
        $traslado = TrasladoRecurso::create($data);

        // Actualizar ambiente actual del recurso
        $recurso = Recurso::findOrFail($request->recurso_id);
        $recurso->ambiente_id = $request->ambiente_destino;
        $recurso->save();

        return redirect()->route('traslados.index')->with('success', 'Traslado registrado con éxito.');
    }

    public function show($id)
    {
        $traslado = TrasladoRecurso::with(['recurso', 'ambienteOrigen', 'ambienteDestino'])->findOrFail($id);
        return view('traslados.show', compact('traslado'));
    }

    public function edit($id)
    {
        $traslado = TrasladoRecurso::findOrFail($id);
        $recursos = Recurso::all();
        $ambientes = Ambiente::all();
        return view('traslados.edit', compact('traslado','recursos','ambientes'));
    }

    public function update(Request $request, $id)
    {
        $traslado = TrasladoRecurso::findOrFail($id);

        $data = $request->validate([
            'recurso_id' => 'required|exists:recursos,id_recurso',
            'ambiente_origen' => 'required|exists:ambiente,id_ambiente',
            'ambiente_destino' => 'required|exists:ambiente,id_ambiente',
            'fecha_traslado' => 'required|date',
            'observacion' => 'nullable|string',
        ]);

        $traslado->update($data);

        // Actualizar ambiente actual del recurso
        $traslado->recurso->update([
            'ambiente_id' => $request->ambiente_destino
        ]);

        return redirect()->route('traslados.index')->with('success', 'Traslado actualizado.');
    }

    public function destroy($id)
    {
        $traslado = TrasladoRecurso::findOrFail($id);
        $traslado->delete();
        return redirect()->route('traslados.index')->with('success', 'Traslado eliminado.');
    }
}
