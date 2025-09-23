<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\Auth;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\AprendizController;
use App\Http\Controllers\GuardaController;
use App\Http\Controllers\AdminController;
use App\Http\Controllers\AmbienteController;
use App\Http\Controllers\IncidenteController;
use App\Http\Controllers\AsistenciasController;
use App\Http\Controllers\MinutaController;
use App\Http\Controllers\RecursoController;
use App\Http\Controllers\TipoRecursoController;
use App\Http\Controllers\InstructorController;
use App\Http\Controllers\TrasladoController;
use App\Http\Controllers\HistoricoIncidenteController;
use App\Http\Controllers\PerfilController;

/*
|--------------------------------------------------------------------------
| Rutas Públicas (sin autenticación)
|--------------------------------------------------------------------------
*/

// Página de inicio
Route::get('/', function () {
    return view('index'); 
})->name('home');

// Login
Route::get('/login', [AuthController::class, 'showLoginForm'])->name('login');
Route::post('/login', [AuthController::class, 'login'])->name('login.submit');

// Logout
Route::post('/logout', function () {
    Auth::logout();
    request()->session()->invalidate();
    request()->session()->regenerateToken();
    return redirect('/login'); 
})->name('logout');

/*
|--------------------------------------------------------------------------
| Rutas Protegidas (requieren autenticación)
|--------------------------------------------------------------------------
*/
Route::middleware(['auth'])->group(function () {

    /*
    |--------------------------------------------------------------------------
    | Dashboards
    |--------------------------------------------------------------------------
    */
    Route::get('/dashboard', fn() => view('dashboard'))->name('dashboard');
    Route::get('/admin/dashboard', fn() => view('administrador.dashboard'))->name('admin.dashboard');
    Route::get('/aprendiz/dashboard', fn() => view('aprendiz.dashboard'))->name('aprendiz.dashboard');
    Route::get('/instructor/dashboard', fn() => view('instructor.dashboard'))->name('instructor.dashboard');
    Route::get('/guarda/dashboard', fn() => view('guarda.dashboard'))->name('guarda.dashboard');

    /*
    |--------------------------------------------------------------------------
    | Usuarios
    |--------------------------------------------------------------------------
    */
    Route::resource('usuarios', UserController::class);

    // Aprendiz
    Route::get('aprendiz/create/{id_usuario}', [AprendizController::class, 'create'])->name('aprendiz.create');
    Route::post('aprendiz/store', [AprendizController::class, 'store'])->name('aprendiz.store');
    Route::put('/aprendiz/{id}', [AprendizController::class, 'update'])->name('aprendiz.update');
    Route::get('/aprendiz/{id}/asistencias', [AsistenciasController::class, 'misAsistencias'])->name('asistencias.mias');

    // Instructor
    Route::get('/instructor/create/{id_usuario}', [InstructorController::class, 'create'])->name('instructor.create');
    Route::post('/instructor/store', [InstructorController::class, 'store'])->name('instructor.store');

    // Guarda
    Route::get('/guarda/create/{id_usuario}', [GuardaController::class, 'create'])->name('guarda.create');
    Route::post('/guarda/store', [GuardaController::class, 'store'])->name('guarda.store');

    // Administrador
    Route::get('/administrador/create/{id_usuario}', [AdminController::class, 'createCoordinador'])->name('administrador.create');
    Route::post('/administrador/store', [AdminController::class, 'storeCoordinador'])->name('administrador.store');

    /*
    |--------------------------------------------------------------------------
    | Ambientes
    |--------------------------------------------------------------------------
    */
    Route::resource('ambientes', AmbienteController::class);

    /*
    |--------------------------------------------------------------------------
    | Incidentes
    |--------------------------------------------------------------------------
    */
    Route::get('/incidentes/filtrar', [IncidenteController::class, 'filtrar'])->name('incidentes.filtrar');
    Route::resource('incidentes', IncidenteController::class);

    /*
    |--------------------------------------------------------------------------
    | Asistencias
    |--------------------------------------------------------------------------
    */
    Route::resource('registro_inasistencia', AsistenciasController::class)->names('registro_inasistencia');
    Route::resource('inasistencias', AsistenciasController::class);

    /*
    |--------------------------------------------------------------------------
    | Minutas
    |--------------------------------------------------------------------------
    */
    
    Route::get('/minutas/historial', [App\Http\Controllers\MinutaController::class, 'historial'])
    ->name('minutas.historial');

    Route::resource('minutas', MinutaController::class);


    /*
    |--------------------------------------------------------------------------
    | Recursos
    |--------------------------------------------------------------------------
    */
    Route::prefix('recursos')->name('recursos.')->group(function () {
        Route::get('/', [RecursoController::class, 'index'])->name('index');
        Route::get('/create', [RecursoController::class, 'create'])->name('create');
        Route::post('/store', [RecursoController::class, 'store'])->name('store');
        Route::get('/{id}', [RecursoController::class, 'show'])->name('show');
        Route::get('/{id}/edit', [RecursoController::class, 'edit'])->name('edit');
        Route::put('/{id}', [RecursoController::class, 'update'])->name('update');
        Route::delete('/{id}', [RecursoController::class, 'destroy'])->name('destroy');
    });

});
/*
    |--------------------------------------------------------------------------
    | Traslado Recursos
    |--------------------------------------------------------------------------
    */


Route::resource('traslados', TrasladoController::class);

/*
    |--------------------------------------------------------------------------
    | Historico Incidentes
    |--------------------------------------------------------------------------
    */

Route::get('/historico-incidentes', [HistoricoIncidenteController::class, 'index'])
     ->name('historico_incidentes.index');
/*
    |--------------------------------------------------------------------------
    | Perfil aprendiz
    |--------------------------------------------------------------------------
    */

// Perfil
Route::get('/aprendiz/perfil', [AprendizController::class, 'show'])
    ->name('aprendiz.perfil');
Route::get('/aprendiz/editar', [AprendizController::class, 'edit'])
    ->name('aprendiz.editar');

// Programa
Route::get('/aprendiz/programa', [AprendizController::class, 'programa'])
    ->name('aprendiz.programa');

// Ficha
Route::get('/aprendiz/ficha', [AprendizController::class, 'ficha'])
    ->name('aprendiz.ficha');

// Asistencias

    Route::middleware(['auth'])->group(function () {
    Route::get('/aprendiz/perfil', [AprendizController::class, 'perfil'])->name('aprendiz.perfil');
    Route::get('/aprendiz/perfil/show', [AprendizController::class, 'show'])->name('aprendiz.show');
    Route::get('/aprendiz/perfil/edit', [AprendizController::class, 'editarPerfil'])->name('aprendiz.edit');
    Route::post('/aprendiz/perfil/actualizar', [AprendizController::class, 'actualizarPerfil'])->name('aprendiz.actualizar');
    Route::get('/aprendiz/asistencias', [AprendizController::class, 'asistencias'])->name('aprendiz.asistencias');
});