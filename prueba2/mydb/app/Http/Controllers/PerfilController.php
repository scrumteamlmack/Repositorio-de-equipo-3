<?php
namespace App\Http\Controllers;

use App\Models\Usuario;
use Illuminate\Http\Request;

class PerfilController extends Controller
{
    public function show($id)
    {
        $usuario = Usuario::findOrFail($id);
        return view('perfil.show', compact('usuario'));
    }

    public function edit($id)
    {
        $usuario = Usuario::findOrFail($id);
        return view('perfil.edit', compact('usuario'));
    }

    public function update(Request $request, $id)
    {
        $usuario = Usuario::findOrFail($id);

        $request->validate([
            'nombre' => 'required|string|max:255',
            'correo' => 'required|email',
            'telefono' => 'nullable|string|max:20',
        ]);

        $usuario->update($request->all());

        return redirect()->route('perfil.show', $id)->with('success', 'Perfil actualizado con éxito.');
    }
}

