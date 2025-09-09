<?php

namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Http\RedirectResponse;
use Illuminate\Views\Views;
use Illuminate\Support\Facades\DB;

class UsuariosController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $usuarios = Usuario::all();
        return view("usuarios.index", compact("usuarios"));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view("usuarios.create");
    }

    /**
     * Store a newly created resource in storage.
     */
 public function store(Request $request)
    {
        $request->validate([
            'p_nombre' => 'required',
            'p_apellido' => 'required',
            'tipo_documento' => 'required',
            'num_documento' => 'required|unique:usuario,num_documento',
            'correo' => 'required|email|unique:usuario,correo',
            'contraseña' => 'required|min:6',
        ]);

        $usuario = Usuario::create([
            'p_nombre' => $request->p_nombre,
            's_nombre' => $request->s_nombre,
            'p_apellido' => $request->p_apellido,
            's_apellido' => $request->s_apellido,
            'tipo_documento' => $request->tipo_documento,
            'num_documento' => $request->num_documento,
            'correo' => $request->correo,
            'contraseña' => bcrypt($request->contraseña),
        ]);

        // Aquí decides si se asigna a Aprendiz u otro rol
        if ($request->id_rol == 1) {
            return redirect()->route('aprendiz.create', ['id_usuario' => $usuario->id_usuario]);
        }

        return redirect()->route('usuarios.index')
            ->with('success', 'Usuario registrado correctamente.');
    }




    /**
     * Display the specified resource.
     */
    public function show(Usuario $usuario)
    {
    
        return view('usuarios.show', compact('usuario'));
    }

    /**
     * Show the form for editing the specified resource.
     */
   public function edit($id)
{
    $usuario = Usuario::findOrFail($id);
    return view('usuarios.edit', compact('usuario'));
}

public function update(Request $request, $id)
{
    $usuario = Usuario::findOrFail($id);

    $usuario->update([
        'p_nombre' => $request->p_nombre,
        's_nombre' => $request->s_nombre,
        'p_apellido' => $request->p_apellido,
        's_apellido' => $request->s_apellido,
        'tipo_documento' => $request->tipo_documento,
        'num_documento' => $request->num_documento,
        'correo' => $request->correo,
        'contraseña' => $request->filled('contraseña') ? bcrypt($request->contraseña) : $usuario->contraseña,
    ]);

    return redirect()->route('usuarios.index')->with('success', 'Usuario actualizado correctamente.');
}


    /**
     * Remove the specified resource from storage.
     */
public function destroy($id): RedirectResponse
{
    $usuario = Usuario::findOrFail($id);

    // Eliminar relaciones en tablas hijas
    DB::table('aprendiz')->where('Usuario_id_usuario', $usuario->id_usuario)->delete();
    DB::table('instructor')->where('Usuario_id_usuario', $usuario->id_usuario)->delete();
    DB::table('coordinador')->where('Usuario_id_usuario', $usuario->id_usuario)->delete();
    DB::table('guarda_seguridad')->where('Usuario_id_usuario', $usuario->id_usuario)->delete();

    // Eliminar relación en la tabla pivote user_rol
    DB::table('user_rol')->where('id_usuario', $usuario->id_usuario)->delete();

    // Ahora sí eliminar el usuario
    $usuario->delete();

    return redirect()->route('usuarios.index')
        ->with('success', 'Usuario eliminado correctamente.');
}




}
