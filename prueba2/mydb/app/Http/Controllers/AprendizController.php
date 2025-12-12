<?php

namespace App\Http\Controllers;


use Illuminate\Http\RedirectResponse;
use Illuminate\Support\Facades\DB;
use App\Models\Usuario;
use App\Models\Aprendiz;
use App\Models\RegistroInasistencia;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;



class AprendizController extends Controller
{
public function create($id_usuario)
{
    $usuario = Usuario::findOrFail($id_usuario);
    $fichas = DB::table('ficha')->get();
    $programas = DB::table('programas')->get();

    // Pasamos las 3 variables con nombres coherentes
    return view('aprendiz.create', compact('usuario', 'fichas', 'programas'));
}




public function store(Request $request)
{
    $request->validate([
        'Usuario_id_usuario' => 'required|exists:usuario,id_usuario',
        'ficha_idficha' => 'required|exists:ficha,idficha',
        'programas_id_programas' => 'required|exists:programas,id_programas',
    ]);

    DB::table('aprendiz')->insert([
        'Usuario_id_usuario' => $request->Usuario_id_usuario,
        'ficha_idficha' => $request->ficha_idficha,
        'programas_id_programas' => $request->programas_id_programas,
    ]);

    return redirect()->route('usuarios.index')->with('success', 'Aprendiz registrado correctamente.');
}


    public function dashboard()
    {
        return view('aprendiz.dashboard');
    }
public function show()
{
    $id = Auth::id();
    $aprendiz = Aprendiz::with('usuario')->where('Usuario_id_usuario', $id)->firstOrFail();
    return view('aprendiz.perfil', compact('aprendiz'));
}

    // Ver perfil del aprendiz logueado
   public function perfil()
    {
        $id = Auth::id(); // ID del usuario autenticado
        $aprendiz = Aprendiz::with('usuario')->where('Usuario_id_usuario', $id)->firstOrFail();

        return view('aprendiz.perfil', compact('aprendiz'));
    }

    // Ver programa del aprendiz logueado
    public function programa()
{
    $id = Auth::id();
    $aprendiz = Aprendiz::with(['programa.jornada', 'programa.modalidad', 'programa.coordinacion'])
        ->where('Usuario_id_usuario', $id)
        ->firstOrFail();

    return view('aprendiz.programa', compact('aprendiz'));
}


    // Ver ficha del aprendiz logueado
    public function ficha()
    {
        $id = Auth::id();
        $aprendiz = Aprendiz::with(['ficha.instructor'])->where('Usuario_id_usuario', $id)->firstOrFail();

        return view('aprendiz.ficha', compact('aprendiz'));
    }

public function asistencias()
{
    $id = Auth::id();
    $asistencias = RegistroInasistencia::with('jornada')
        ->where('aprendiz_Usuario_id_usuario', $id)
        ->get();
    return view('aprendiz.asistencias', compact('asistencias'));
}


    // Editar perfil
    public function editarPerfil()
    {
        $aprendiz = Aprendiz::where('Usuario_id_usuario', Auth::id())->firstOrFail();
        return view('aprendiz.edit', compact('aprendiz'));
    }

    // Actualizar perfil
    public function actualizarPerfil(Request $request)
    {
        $aprendiz = Aprendiz::where('Usuario_id_usuario', Auth::id())->firstOrFail();

        $aprendiz->update($request->all());

        return redirect()->route('aprendiz.perfil')->with('success', 'Perfil actualizado correctamente.');
}

 public function update(Request $request)
{
    $id = Auth::id();

    // Validar datos
    $request->validate([
        'p_nombre' => 'required|string|max:50',
        's_nombre' => 'nullable|string|max:50',
        'p_apellido' => 'required|string|max:50',
        's_apellido' => 'nullable|string|max:50',
        'tipo_documento' => 'required|string|max:20',
        'num_documento' => 'required|numeric',
        'correo' => 'required|email'
    ]);

    // Buscar el aprendiz con su usuario
    $aprendiz = Aprendiz::with('usuario')->where('Usuario_id_usuario', $id)->firstOrFail();

    // Actualizar los datos del usuario asociado
    $aprendiz->usuario->update([
        'p_nombre'       => $request->p_nombre,
        's_nombre'       => $request->s_nombre,
        'p_apellido'     => $request->p_apellido,
        's_apellido'     => $request->s_apellido,
        'tipo_documento' => $request->tipo_documento,
        'num_documento'  => $request->num_documento,
        'correo'         => $request->correo,
    ]);

    return redirect()->route('aprendiz.perfil')
                     ->with('success', 'Perfil actualizado correctamente.');
}

   
}

