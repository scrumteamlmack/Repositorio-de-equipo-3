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
    Route::get('/instructor/dashboard', [InstructorController::class, 'dashboard'])->name('instructor.dashboard');
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

    /*
    |--------------------------------------------------------------------------
    | Perfil Instructor
    |--------------------------------------------------------------------------
    */
  Route::prefix('instructor')->name('instructor.')->middleware(['auth'])->group(function () {
    Route::get('/perfil', [InstructorController::class, 'perfil'])->name('perfil');
    Route::get('/perfil/editar', [InstructorController::class, 'editPerfil'])->name('perfil.edit');
    Route::post('/perfil/actualizar', [InstructorController::class, 'updatePerfil'])->name('perfil.update');
    Route::get('/fichas', [InstructorController::class, 'fichas'])->name('fichas');
    Route::get('/aprendices', [InstructorController::class, 'aprendices'])->name('aprendices');
});


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
    Route::get('/minutas/historial', [MinutaController::class, 'historial'])->name('minutas.historial');
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
| Perfil Aprendiz
|--------------------------------------------------------------------------
*/
Route::prefix('aprendiz')->name('aprendiz.')->middleware(['auth'])->group(function () {
    Route::get('/perfil', [AprendizController::class, 'perfil'])->name('perfil'); // aprendiz.perfil
    Route::get('/perfil/show', [AprendizController::class, 'show'])->name('show'); // aprendiz.show
    Route::get('/perfil/edit', [AprendizController::class, 'editarPerfil'])->name('edit'); // aprendiz.edit
    Route::post('/perfil/actualizar', [AprendizController::class, 'actualizarPerfil'])->name('actualizar'); // aprendiz.actualizar

    // Programa
    Route::get('/programa', [AprendizController::class, 'programa'])->name('programa');

    // Ficha
    Route::get('/ficha', [AprendizController::class, 'ficha'])->name('ficha');

    // Asistencias
    Route::get('/asistencias', [AprendizController::class, 'asistencias'])->name('asistencias');
});


/*
|--------------------------------------------------------------------------
| Perfil Instructor
|--------------------------------------------------------------------------
*/
Route::prefix('instructor')->name('instructor.')->group(function () {
    // Perfil
    Route::get('/perfil', [InstructorController::class, 'perfil'])->name('perfil'); // instructor.perfil
    Route::get('/perfil/editar', [InstructorController::class, 'editPerfil'])->name('edit');// instructor.editarPerfil
    Route::post('/perfil/{id}/actualizar', [InstructorController::class, 'updatePerfil'])->name('perfil.update');
    // Fichas y aprendices
    Route::get('/fichas', [InstructorController::class, 'fichas'])->name('fichas');
    Route::get('/aprendices', [InstructorController::class, 'aprendices'])->name('aprendices');
});

