<?php

namespace App\Providers;

use Illuminate\Support\ServiceProvider;
<<<<<<< HEAD
=======
use Illuminate\Support\Facades\View;
use Illuminate\Support\Facades\Auth;
use App\Models\Ficha;
>>>>>>> 6b4a9da6b570592154cd1b9ae2483bf24c9bd186

class AppServiceProvider extends ServiceProvider
{
    /**
     * Register any application services.
     */
    public function register(): void
    {
        //
    }

    /**
     * Bootstrap any application services.
     */
<<<<<<< HEAD
    public function boot(): void
    {
        //
    }
=======
    public function boot()
{
    // Compartir fichas del instructor con conteo de aprendices
    View::composer('instructor.dashboard', function ($view) {
        $fichas = collect();
        if (Auth::check()) {
            $fichas = Ficha::whereHas('instructor', function($q){
                $q->where('Usuario_id_usuario', Auth::id());
            })
            ->withCount('aprendices')
            ->get();
        }
        $view->with('fichas', $fichas);
    });
}
>>>>>>> 6b4a9da6b570592154cd1b9ae2483bf24c9bd186
}
