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
    $request->validate([
        'fecha_inasistencia' => 'required|date',
        'estado_inasistencia' => 'required|in:S,N,R', // aquí metemos R también
        'jornada_id' => 'required|integer',
        'aprendiz_Usuario_id_usuario' => 'required|integer',
        'instructor_Usuario_id_usuario' => 'required|integer',
    ]);

    RegistroInasistencia::create([
        'fecha_inasistencia' => $request->fecha_inasistencia,
        'estado_inasistencia' => $request->estado_inasistencia, // ahora acepta R
        'jornada_id' => $request->jornada_id,
        'aprendiz_Usuario_id_usuario' => $request->aprendiz_Usuario_id_usuario,
        'instructor_Usuario_id_usuario' => $request->instructor_Usuario_id_usuario,
    ]);

    return redirect()->route('inasistencias.index')->with('success', 'Registro creado correctamente.');
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
    // Trae el registro junto con las relaciones
    $registro = RegistroInasistencia::with(['aprendiz.usuario', 'instructor.usuario', 'jornada'])
        ->findOrFail($id);

    // Trae los aprendices con su usuario, ordenados por nombre
    $aprendices = Aprendiz::with('usuario')
        ->get()
        ->sortBy(function ($a) {
            return $a->usuario->p_nombre ?? '';
        });

    // Trae los instructores con su usuario, ordenados por nombre
    $instructores = Instructor::with('usuario')
        ->get()
        ->sortBy(function ($i) {
            return $i->usuario->p_nombre ?? '';
        });

    // Trae todas las jornadas
    $jornadas = Jornada::orderBy('nombre_jornada')->get();

    return view('registro_inasistencia.edit', compact('registro', 'aprendices', 'instructores', 'jornadas'));
}


    /**
     * Update the specified resource in storage.
     */
  public function update(Request $request, $id)
{
    $validated = $request->validate([
        'aprendiz_Usuario_id_usuario' => 'required|exists:aprendiz,Usuario_id_usuario',
        'fecha_inasistencia' => 'required|date',
        'estado_inasistencia' => 'required|in:S,N,R',
        'jornada_id' => 'required|exists:jornada,id_jornada',
        'instructor_Usuario_id_usuario' => 'required|exists:instructor,Usuario_id_usuario',
    ],
    [], // mensajes personalizados (opcional)
    [
      'aprendiz_Usuario_id_usuario' => 'aprendiz id',
      'estado_inasistencia' => 'estado',
      'instructor_Usuario_id_usuario' => 'instructor id',
      'jornada_id' => 'jornada id',
    ]);

    $registro = RegistroInasistencia::findOrFail($id);

    $registro->update([
        'aprendiz_Usuario_id_usuario' => $validated['aprendiz_Usuario_id_usuario'],
        'fecha_inasistencia' => $validated['fecha_inasistencia'],
        'estado_inasistencia' => $validated['estado_inasistencia'],
        'jornada_id' => $validated['jornada_id'],
        'instructor_Usuario_id_usuario' => $validated['instructor_Usuario_id_usuario'],
    ]);

    return redirect()->route('registro_inasistencia.index')->with('success', 'Registro actualizado correctamente.');
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
