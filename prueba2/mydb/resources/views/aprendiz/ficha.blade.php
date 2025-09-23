@extends('layouts.aprendiz')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/perfil_aprendiz.css') }}">
</head>
<div class="container perfil-container">
    <h2>Mi Ficha</h2>
    <div class="card">
        <p><strong>Número de Ficha:</strong> {{ $aprendiz->ficha->Num_ficha }}</p>        
        @if($aprendiz->ficha->instructor)
            <p><strong>Instructor:</strong> {{ $aprendiz->ficha->instructor->usuario->p_nombre}} {{ $aprendiz->ficha->instructor->usuario->p_apellido }}</p>
        @else
            <p><strong>Instructor:</strong> No asignado</p>
        @endif
    </div>
</div>
@endsection
