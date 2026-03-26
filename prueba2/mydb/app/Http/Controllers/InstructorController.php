<?php

namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Models\Instructor;
use App\Models\Ficha;
use Illuminate\Support\Facades\Auth;

class InstructorController extends Controller
{
    public function create($id_usuario)
    {
        $usuario = Usuario::findOrFail($id_usuario);
        $coordinaciones = DB::table('coordinacion')->get(); // Traer coordinaciones disponibles

        return view('instructor.create', compact('usuario', 'coordinaciones'));
    }

    public function store(Request $request)
    {
        $request->validate([
            'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
            'telefono' => 'required|string|max:20',
            'coordinacion_id_coordinacion' => 'required|exists:coordinacion,id_coordinacion',
            'estado' => 'required|in:Activo,Inactivo',
        ]);

        // Obtener el email desde la tabla usuario
        $usuario = DB::table('usuario')->where('id_usuario', $request->Usuario_id_usuario)->first();

        DB::table('instructor')->insert([
            'Usuario_id_usuario' => $request->Usuario_id_usuario,
            'email' => $usuario->correo, // se guarda el email del usuario
            'telefono' => $request->telefono,
            'coordinacion_id_coordinacion' => $request->coordinacion_id_coordinacion, // FK en lugar de texto
            'estado' => $request->estado,
        ]);

        return redirect()->route('usuarios.index')->with('success', 'Instructor registrado correctamente.');
    }

   public function dashboard()
<<<<<<< HEAD
    {
        return view('instructor.dashboard');
    }

=======
{
    $instructor = Instructor::where('Usuario_id_usuario', Auth::id())->first();

    if ($instructor) {
        $fichas = $instructor->fichas()->withCount('aprendices')->get();
    } else {
        $fichas = collect(); // colección vacía si no tiene fichas
    }

    return view('instructor.dashboard', compact('fichas'));
}


    // Perfil del instructor
>>>>>>> 6b4a9da6b570592154cd1b9ae2483bf24c9bd186
    public function perfil()
    {
        $usuario = Auth::user();
        return view('instructor.perfil', compact('usuario'));
    }

    public function editPerfil()
    {
        $usuario = Auth::user();
        return view('instructor.edit', compact('usuario'));
    }

    public function updatePerfil(Request $request, $id)
<<<<<<< HEAD
{
    // Validar datos
    $request->validate([
        'p_nombre' => 'required|string|max:50',
        'p_apellido' => 'required|string|max:50',
        'correo' => 'required|email|max:100|unique:usuario,correo,' . $id . ',id_usuario',
        'telefono' => 'nullable|string|max:20',
        'tipo_documento' => 'required|string|max:20',
        'num_documento' => 'required|string|max:20|unique:usuario,num_documento,' . $id . ',id_usuario',
    ]);

    // Buscar usuario
    $usuario = Usuario::findOrFail($id);

    // Actualizar datos del usuario
    $usuario->update($request->only([
        'p_nombre',
        'p_apellido',
        'correo',
        'tipo_documento',
        'num_documento'
    ]));

    // Actualizar datos del instructor (solo telefono por ahora)
    if ($usuario->instructor) {
        $usuario->instructor->update([
            'telefono' => $request->telefono,
        ]);
    }

    return redirect()->route('instructor.perfil')
                     ->with('success', 'Perfil actualizado correctamente.');
}



    public function fichas()
    {
        $fichas = Ficha::where('instructor_id', Auth::id())->get();
        return view('instructor.fichas', compact('fichas'));
    }

    public function aprendices()
    {
        $fichas = Ficha::where('instructor_id', Auth::id())->with('aprendices')->get();
        return view('instructor.aprendices', compact('fichas'));
    }
=======
    {
        $request->validate([
            'p_nombre' => 'required|string|max:50',
            'p_apellido' => 'required|string|max:50',
            'correo' => 'required|email|max:100|unique:usuario,correo,' . $id . ',id_usuario',
            'telefono' => 'nullable|string|max:20',
            'tipo_documento' => 'required|string|max:20',
            'num_documento' => 'required|string|max:20|unique:usuario,num_documento,' . $id . ',id_usuario',
        ]);

        $usuario = Usuario::findOrFail($id);

        $usuario->update($request->only([
            'p_nombre','p_apellido','correo','tipo_documento','num_documento'
        ]));

        if ($usuario->instructor) {
            $usuario->instructor->update(['telefono' => $request->telefono]);
        }

        return redirect()->route('instructor.perfil')->with('success', 'Perfil actualizado correctamente.');
    }

    // Mostrar fichas del instructor
    public function fichas()
    {
        $instructor = Instructor::where('Usuario_id_usuario', Auth::id())->first();

        if (!$instructor) {
            $fichas = collect(); // Sin fichas
        } else {
            $fichas = $instructor->fichas()->withCount('aprendices')->get();
        }

        return view('instructor.fichas', compact('fichas'));
    }

    // Mostrar aprendices de una ficha
    public function fichaAprendices($id)
{
    // Obtiene el instructor logueado
    $instructor = Instructor::where('Usuario_id_usuario', Auth::id())->firstOrFail();

    // Busca la ficha correcta usando su clave primaria `idficha`
    $ficha = Ficha::with('aprendices.usuario')->where('idficha', $id)->firstOrFail();

    // Seguridad: solo puede ver sus propias fichas
    if ($ficha->instructor_Usuario_id_usuario !== $instructor->Usuario_id_usuario) {
        abort(403, 'No tienes permiso para ver esta ficha.');
    }

    return view('instructor.aprendices', compact('ficha'));
}


    
>>>>>>> 6b4a9da6b570592154cd1b9ae2483bf24c9bd186
}
