<?php

//use App\Http\Controllers\AuthController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\AprendizController;
use App\Http\Controllers\GuardaController;
use App\Http\Controllers\AdminController;

// Página de login
//Route::get('/login', [AuthController::class, 'showLoginForm'])->name('login');

// Procesar login
//Route::post('/login', [AuthController::class, 'login'])->name('login.submit');

// Logout
//Route::post('/logout', [AuthController::class, 'logout'])->name('logout');

// Dashboard (solo usuarios logueados)
//Route::get('/dashboard', function () {
    //return view('dashboard');
//})->middleware('auth')->name('dashboard');


// Página por defecto → redirigir al login
//Route::get('/', function () {
   // return redirect()->route('login');
//});

Route::resource('usuarios', UserController::class);

Route::get('/', fn()=>redirect()->route('usuarios.index'));

Route::get('aprendiz/create/{id_usuario}', [AprendizController::class, 'create'])->name('aprendiz.create');
Route::post('aprendiz/store', [AprendizController::class, 'store'])->name('aprendiz.store');

Route::get('/instructor/create/{id_usuario}', [App\Http\Controllers\InstructorController::class, 'create'])->name('instructor.create');
Route::post('/instructor/store', [App\Http\Controllers\InstructorController::class, 'store'])->name('instructor.store');

Route::get('/guarda/create/{id_usuario}', [App\Http\Controllers\GuardaController::class, 'create'])->name('guarda.create');
Route::post('/guarda/store', [App\Http\Controllers\GuardaController::class, 'store'])->name('guarda.store');

Route::get('/administrador/create/{id_usuario}', [AdminController::class, 'createCoordinador'])->name('administrador.create');
Route::post('/administrador/store', [AdminController::class, 'storeCoordinador'])->name('administrador.store');

