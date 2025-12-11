@extends('layouts.aprendiz')

@section('content')
<link rel="stylesheet" href="{{ asset('css/dashboard.css') }}">

<div class="dashboard-container">
    <h2 class="welcome-title">👋 Bienvenido {{ Auth::user()->p_nombre }} {{ Auth::user()->p_apellido }}</h2>
    
    <h3 class="subtitle">Panel del Aprendiz</h3>
    <p class="intro">Aquí podrás consultar tu asistencia, tu programa, jornada y modalidad.</p>

    
    </div>
</div>
@endsection
