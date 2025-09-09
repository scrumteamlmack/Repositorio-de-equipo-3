<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UsuariosController;
use App\Http\Controllers\AprendizController;

Route::get('/', fn()=>redirect()->route('usuarios.index'));

Route::resource('usuarios', UsuariosController::class);

