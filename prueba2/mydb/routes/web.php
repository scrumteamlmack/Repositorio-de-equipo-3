<?php

use App\Http\Controllers\AuthController;
use Illuminate\Support\Facades\Route;

// Página de login
Route::get('/login', [AuthController::class, 'showLoginForm'])->name('login');

// Procesar login
Route::post('/login', [AuthController::class, 'login'])->name('login.submit');

// Logout
Route::post('/logout', [AuthController::class, 'logout'])->name('logout');

// Dashboard (solo usuarios logueados)
Route::get('/dashboard', function () {
    return view('dashboard');
})->middleware('auth')->name('dashboard');


// Página por defecto → redirigir al login
Route::get('/', function () {
    return redirect()->route('login');
});

