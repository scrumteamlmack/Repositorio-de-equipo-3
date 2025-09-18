<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\RegistroInasistencia;
use App\Models\Aprendiz;
use App\Models\jornada;
use App\Models\Instructor;
use App\Models\Ambiente;



class AsistenciasController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $registros = RegistroInasistencia::with(['aprendiz', 'jornada'])->orderBy('fecha_inasistencia','desc')->get();
        return view('registro_inasistencia.index', compact('registros'));
        
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
       $aprendices = Aprendiz::with('usuario')
        ->get()
        ->sortBy(function ($a) {
            return $a->usuario->p_nombre ?? '';
        });

    $instructores = Instructor::with('usuario')
        ->get()
        ->sortBy(function ($i) {
            return $i->usuario->p_nombre ?? '';
        });

    $jornadas = Jornada::orderBy('nombre_jornada')->get();

    return view('registro_inasistencia.create', compact('aprendices', 'instructores', 'jornadas'));
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'aprendiz_id' => 'required|exists:aprendiz,id_aprendiz',
            'fecha_inasistencia' => 'required|date',
            'motivo' => 'nullable|string|max:255',
            'estado' => 'required|in:Presente,Ausente']);

            RegistroInasistencia::create([
    'aprendiz_Usuario_id_usuario' => $validated['aprendiz_Usuario_id_usuario'],
    'fecha_inasistencia' => $validated['fecha_inasistencia'],
    'motivo' => $validated['motivo'] ?? null,
    'estado_inasistencia' => $validated['estado'],
    'jornada_id' => $validated['jornada_id'] ?? 1, 
    'instructor_Usuario_id_usuario' => auth()->user()->id_usuario, 
]);


            return redirect()->route('registro_inasistencia.index')->with('success', 'registro creado correctamente');
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $aprendices = Aprendiz::findOrfail($id);
        return view('registro_asistencia.show', compact('aprendices'));
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $registro = RegistroInasistencia::findOrFail($id);
        $aprendices = Aprendiz:: with('usuario')->get()->sortBy(function($a){
            return $a->usuario->p_nombre ?? '';
        });
        return view('registro_inasistencia.edit',compact('registro', 'aprendices'));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $validated = $request->validate([
              'aprendiz_id' => 'required|exists:aprendiz,id_aprendiz',
            'fecha_inasistencia' => 'required|date',
            'motivo' => 'nullable|string|max:255',
            'estado' => 'required|in:Presente,Ausente'
        ]);
         $registro = RegistroInasistencia::findOrFail($id);

        $registro->update([
              'aprendiz_Usuario_id_usuario' => $validated['aprendiz_id'],
            'fecha_inasistencia' => $validated['fecha_inasistencia'],
            'motivo' => $validated['motivo'] ?? null,
            'estado' => $validated['estado'],
        ]);

        return redirect() ->route('registro_inasistencia.index')->with('success','Registro actualizado correctamente');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
         $registro = RegistroInasistencia::findOrFail($id);
        $registro->delete();
        return redirect()->route('registro_inasistencia.index')->with('success', 'Registro eliminado correctamente');
    }
}
