<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    public function showLoginForm()
    {
        return view('auth.login');
    }

    
public function login(Request $request)
{
    $request->validate([
        'num_documento' => 'required|numeric',
        'contraseña' => 'required'
    ]);

    $credentials = [
        'num_documento' => $request->num_documento,
        'password' => $request->contraseña  // Laravel automáticamente usará getAuthPassword()
    ];

    if (Auth::attempt($credentials)) {
    $user = Auth::user();

    // Obtenemos el primer rol del usuario desde la relación
    $rol = $user->user_rols->first()->rol->nombre_rol ?? null;

    switch ($rol) {
        case 'Administrador':
            return redirect()->route('admin.dashboard');
        case 'Aprendiz':
            return redirect()->route('aprendiz.dashboard');
        case 'Instructor':
            return redirect()->route('instructor.dashboard');
        case 'Guarda de Seguridad':
            return redirect()->route('guarda.dashboard');
        default:
            return redirect()->route('dashboard'); // fallback
    }
}

    return back()->withErrors([
        'num_documento' => 'Credenciales incorrectas'
    ])->withInput();
}



    public function logout(Request $request)
    {
        Auth::logout();
        $request->session()->invalidate();
        $request->session()->regenerateToken();

        return redirect()->route('login');
    }
}
