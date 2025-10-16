<?php

namespace App\Http\Controllers;

use App\Models\RegistroIncidente;
use App\Models\Ambiente;
use App\Models\TipoIncidente;
use App\Models\Rol;
use App\Models\Usuario;
use Illuminate\Http\Request;
use Http\Controller\AuthController;
use Illuminate\Support\Facades\Auth;

class IncidenteController extends Controller
{
public function index()
{
    $incidentes = RegistroIncidente::with(['ambiente', 'tipo_incidente', 'usuario'])
        ->orderBy('fecha_incidente', 'desc')
        ->orderBy('hora_incidente', 'desc')
        ->get();

    return view('incidentes.index', compact('incidentes'));
}



public function create()
{
    $ambientes = Ambiente::all();
    $tipos = TipoIncidente::all();

    // Traemos solo los usuarios que tienen rol "Instructor"
    $usuarios = Usuario::whereHas('user_rols', function($query) {
        $query->whereHas('rol', function($q) {
            $q->where('nombre_rol', 'Instructor');
        });
    })->get();

    // Detectar el layout según el rol del usuario autenticado
    $user = Auth::user();
$roles = $user->user_rols->pluck('rol.nombre_rol');

if ($roles->contains('Administrador')) {
    $layout = 'layouts.admin';
} elseif ($roles->contains('Instructor')) {
    $layout = 'incidentes.create';
} 
 elseif ($roles->contains('Aprendiz')) {
    $layout = 'layouts.aprendiz';
}else if ($roles->contains('Aprendiz')){
    $layout = 'layouts.default'; // fallback por si acaso
} else{
    $layout = 'dashboard';
}

return view('incidentes.create', compact('ambientes', 'tipos', 'usuarios', 'layout'));
}


  public function store(Request $request)
{
    $request->validate([
        'descripcion' => 'nullable|string',
        'fecha_incidente' => 'required|date',
        'hora_incidente' => 'required|date_format:H:i',
        'ambiente_id' => 'required|integer|exists:ambiente,id_ambiente',
        'tipo_inc_id' => 'required|integer|exists:tipo_incidente,id_tipo_inc',
        'usuario_id_usuario' => 'required|integer|exists:usuario,id_usuario',
    ]);

    // Guardar manualmente
    $incidente = new RegistroIncidente();
    $incidente->descripcion = $request->descripcion;
    $incidente->fecha_incidente = $request->fecha_incidente;
    $incidente->hora_incidente = $request->hora_incidente;
    $incidente->ambiente_id = $request->ambiente_id;
    $incidente->tipo_inc_id = $request->tipo_inc_id;
    $incidente->usuario_id_usuario = $request->usuario_id_usuario;
    $incidente->save();

    return redirect()->route('incidentes.index')
                     ->with('success', 'Incidente registrado correctamente.');
}


    public function show(RegistroIncidente $incidente)
    {
        return view('incidentes.show', compact('incidente'));
    }

    public function edit(RegistroIncidente $incidente)
{
    $ambientes = Ambiente::all();
    $tipos = TipoIncidente::all();

    // Traer solo usuarios con rol "Instructor"
    $usuarios = Usuario::whereHas('user_rols', function($query) {
        $query->whereHas('rol', function($q){
            $q->where('nombre_rol', 'Instructor');
        });
    })->get();

    return view('incidentes.edit', compact('incidente', 'ambientes', 'tipos', 'usuarios'));
}


    public function update(Request $request, RegistroIncidente $incidente)
    {
        $request->validate([
            'descripcion' => 'nullable|string',
            'fecha_incidente' => 'required|date',
            'hora_incidente' => 'required|date_format:H:i',
            'ambiente_id' => 'required|integer',
            'tipo_inc_id' => 'required|integer',
            'usuario_id_usuario' => 'required|integer',
        ]);

        $incidente->update($request->all());

        return redirect()->route('incidentes.index')->with('success', 'Incidente actualizado correctamente.');
    }

    public function destroy(RegistroIncidente $incidente)
    {
        $incidente->delete();
        return redirect()->route('incidentes.index')->with('success', 'Incidente eliminado correctamente.');
    }

    
}
