<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use App\Models\RegistroMinutum;
use Illuminate\Http\Request;
use App\Models\Ambiente;
use App\Models\Instructor;
use App\Models\GuardaSeguridad;


class MinutaController extends Controller
{
    /**
     * Display a listing of the resource.
     */
     public function index()
{
    $minutas = RegistroMinutum::with(['ambiente', 'guarda_seguridad', 'instructor'])
                ->orderBy('fecha_hora_recibo', 'desc')
                ->get();

    return view('minutas.index', compact('minutas'));
}


public function historial()
{
    // Historial → todas las minutas
    $minutas = RegistroMinutum::with(['ambiente', 'guarda_seguridad', 'instructor'])
                ->orderBy('fecha_hora_recibo', 'desc')
                ->get();

    return view('minutas.historial', compact('minutas'));
}

    public function create()
    {
        // Lista de guardas de seguridad
        $guardas = GuardaSeguridad::all();
        $ambientes = Ambiente::all();
        $responsables = Instructor::all();

        // Enviar todas al blade
        return view('minutas.create', compact('guardas', 'ambientes', 'responsables'));
    }

  public function store(Request $request)
{
    $data = $request->validate([
        'fecha_hora_recibo' => 'required|date',
        'fecha_hora_entrega' => 'required|date|after_or_equal:fecha_hora_recibo',
        'novedad' => 'nullable|string',
        'descripcion_min' => 'nullable|string',
        'estado' => 'required|string|max:100',
        'ambiente_id' => 'required|integer',
        'guarda_seguridad_Usuario_id_usuario' => 'required|integer',
        'responsable_id' => 'required|integer',
        'registro_minutacol' => 'nullable|string|max:45',
    ]);

    RegistroMinutum::create($data);

    return redirect()->route('minutas.index')->with('success', 'Minuta registrada con éxito.');
}


    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
         $minuta = \App\Models\RegistroMinutum::findOrFail($id);
    return view('minutas.show', compact('minuta'));
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $minuta = RegistroMinutum::findOrFail($id);
        $guardas = GuardaSeguridad::all();
        $ambientes = Ambiente::all();
        $responsables = Instructor::all();
return view('minutas.edit', compact('minuta','guardas','ambientes','responsables'));   
 }

    /**
     * Update the specified resource in storage.
     */
public function update(Request $request, string $id)
{
    $minuta = RegistroMinutum::findOrFail($id);

    $data = $request->validate([
        'fecha_hora_recibo' => 'required|date',
        'fecha_hora_entrega' => 'required|date|after_or_equal:fecha_hora_recibo',
        'novedad' => 'nullable|string',
        'descripcion_min' => 'nullable|string',
        'estado' => 'required|string|max:100',
        'ambiente_id' => 'required|integer',
        'guarda_seguridad_Usuario_id_usuario' => 'required|integer',
        'responsable_id' => 'required|integer',
        'registro_minutacol' => 'nullable|string|max:45',
    ]);

    $minuta->update($data);

    return redirect()->route('minutas.index')->with('success', 'Minuta actualizada correctamente.');
}

// destroy()
public function destroy(string $id)
{
    $minuta = RegistroMinutum::findOrFail($id);
    $minuta->delete();

    return redirect()->route('minutas.index')->with('success', 'Minuta eliminada');
}
}
