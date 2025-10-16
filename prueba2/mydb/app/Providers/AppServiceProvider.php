<?php

namespace App\Providers;

use Illuminate\Support\ServiceProvider;
use Illuminate\Support\Facades\View;
use Illuminate\Support\Facades\Auth;
use App\Models\Ficha;

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
}
