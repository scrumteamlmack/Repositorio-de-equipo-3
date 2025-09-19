<?php

namespace App\Http\Controllers;

use App\Models\Ambiente;
use Illuminate\Http\Request;

class AmbienteController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $ambientes = Ambiente::all();
        return view('ambientes.index', compact('ambientes'));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('ambientes.create');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $request->validate([
            'num_ambiente' => 'required|integer',
            'capacidad'    => 'required|integer',
            'tipo_ambiente'=> 'required|string|max:100',
            'estado'       => 'required|string|max:20',
        ]);

        Ambiente::create($request->all());

        return redirect()->route('ambientes.index')
                         ->with('success', 'Ambiente creado correctamente.');
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $ambiente = Ambiente::findOrFail($id);
        return view('ambientes.show', compact('ambiente'));
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $ambiente = Ambiente::findOrFail($id);
        return view('ambientes.edit', compact('ambiente'));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $request->validate([
            'num_ambiente' => 'required|integer',
            'capacidad'    => 'required|integer',
            'tipo_ambiente'=> 'required|string|max:100',
            'estado'       => 'required|string|max:20',
        ]);

        $ambiente = Ambiente::findOrFail($id);
        $ambiente->update($request->all());

        return redirect()->route('ambientes.index')
                         ->with('success', 'Ambiente actualizado correctamente.');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $ambiente = Ambiente::findOrFail($id);
        $ambiente->delete();

        return redirect()->route('ambientes.index')
                         ->with('success', 'Ambiente eliminado correctamente.');
    }
}
