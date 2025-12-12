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
        // Login correcto
        return redirect()->intended('/dashboard');
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
