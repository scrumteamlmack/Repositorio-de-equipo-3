@extends('layouts.aprendiz')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/perfil_aprendiz.css') }}">
</head>
<div class="container perfil-container">
    <h2>Mi Perfil</h2>
    <div class="card">
        <p><strong>Primer Nombre:</strong> {{ $aprendiz->usuario->p_nombre }}</p>
        <p><strong>Segundo Nombre:</strong> {{ $aprendiz->usuario->s_nombre }}</p>
        <p><strong>Primer Apellido:</strong> {{ $aprendiz->usuario->p_apellido }}</p>
        <p><strong>Segundo Apellido:</strong> {{ $aprendiz->usuario->s_apellido }}</p>
        <p><strong>Documento:</strong> {{ $aprendiz->usuario->num_documento }}</p>
        <p><strong>Email:</strong> {{ $aprendiz->usuario->correo }}</p>
    </div>
    <a href="{{ ('aprendiz.edit') }}" class="btn btn-warning">Editar Perfil</a>
    <a href="{{ ('aprendiz.asistencias') }}" class="btn btn-info">Ver Asistencias</a>
</div>
@endsection
