<?php

namespace App\Http\Controllers;

use App\Models\Ficha;
use App\Models\Aprendiz;
use App\Models\Instructor;
use App\Models\Programa;
use Illuminate\Http\Request;
use App\Models\Modalidad;



class FichaController extends Controller
{
    public function index()
    {
        // Trae fichas con instructor->usuario, programa y modalidad para evitar consultas N+1
        $fichas = Ficha::with(['instructor.usuario', 'programa', 'modalidad'])->get();

        // retorna la vista y pasa $fichas
        return view('fichas.index', compact('fichas'));
    }

    public function create()
{
    $programas = \App\Models\Programa::all();
    $modalidades = \App\Models\Modalidad::all();
    $instructores = \App\Models\Instructor::with('usuario')->get();

    return view('fichas.create', compact('programas', 'modalidades', 'instructores'));
}


    public function store(Request $request)
{ $request->validate([
    'Num_ficha' => 'required|unique:ficha,Num_ficha',
    'programas_id_programas' => 'required|exists:programas,id_programas',
    'modalidad_id' => 'required|exists:modalidad,id_modalidad',
    'instructor_Usuario_id_usuario' => 'nullable|exists:instructor,Usuario_id_usuario',
]);

Ficha::create([
    'Num_ficha' => $request->Num_ficha,
    'programas_id_programas' => $request->programas_id_programas,
    'modalidad_id' => $request->modalidad_id,
    'instructor_Usuario_id_usuario' => $request->instructor_Usuario_id_usuario,
]);


    return redirect()->route('fichas.index')->with('success', 'Ficha creada correctamente');
}


    public function edit(Ficha $ficha)
    {
        $programas = Programa::all();
        $instructores = Instructor::with('usuario')->get();
        $modalidades = Modalidad::all();
        return view('fichas.edit', compact('ficha','programas','instructores','modalidades'));
    }

    public function update(Request $request, Ficha $ficha)
    {
        $request->validate([
            'Num_ficha' => 'required|numeric|unique:ficha,Num_ficha,' . $ficha->idficha . ',idficha',
            'programas_id_programas' => 'required|exists:programas,id_programas',
            'modalidad_id' => 'required|exists:modalidad,id_modalidad',
            'instructor_Usuario_id_usuario' => 'required|exists:instructor,Usuario_id_usuario',
        ]);

        $ficha->update($request->only([
            'Num_ficha',
            'programas_id_programas',
            'modalidad_id',
            'instructor_Usuario_id_usuario'
        ]));

        return redirect()->route('fichas.index')->with('success','Ficha actualizada correctamente.');
    }

   public function destroy($id)
{
    
    // Buscar la ficha
    $ficha = Ficha::findOrFail($id);

    // Eliminar primero todos los aprendices que pertenecen a esta ficha
    Aprendiz::where('ficha_idficha', $ficha->idficha)->delete();

    // Si hay otras relaciones que bloquean la eliminación, eliminar aquí también
    // Ejemplo: registro_inasistencia, minutas, etc.
    // RegistroInasistencia::where('ficha_id', $ficha->idficha)->delete();

    // Finalmente eliminar la ficha
    $ficha->delete();

    return redirect()->route('fichas.index')
                     ->with('success', 'Ficha y aprendices relacionados eliminados correctamente.');
}


}
