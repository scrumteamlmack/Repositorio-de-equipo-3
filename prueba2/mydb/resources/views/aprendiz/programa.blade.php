@extends('layouts.aprendiz')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/perfil_aprendiz.css') }}">
</head>
<div class="container perfil-container">
    <h2>Mi Programa</h2>
    <div class="card">
        <p><strong>Nombre del Programa:</strong> {{ $aprendiz->programa->nombre_programa }}</p>

        @if($aprendiz->programa->jornada)
            <p><strong>Jornada:</strong> {{ $aprendiz->programa->jornada->nombre_jornada }}</p>
        @else
            <p><strong>Jornada:</strong> No asignada</p>
        @endif

        @if($aprendiz->programa->modalidad)
            <p><strong>Modalidad:</strong> {{ $aprendiz->programa->modalidad->nombre_modalidad }}</p>
        @else
            <p><strong>Modalidad:</strong> No asignada</p>
        @endif

        @if($aprendiz->programa->coordinacion)
            <p><strong>Coordinación:</strong> {{ $aprendiz->programa->coordinacion->nombre_coordinacion }}</p>
        @else
            <p><strong>Coordinación:</strong> No asignada</p>
        @endif
    </div>
</div>
@endsection
