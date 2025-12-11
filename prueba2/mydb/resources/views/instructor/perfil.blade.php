@extends('instructor.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/perfil_ins.css') }}">
</head>
<div class="perfil-container">
    <h2>👤 Mi Perfil</h2>

    <!-- Mensaje de éxito -->
    @if(session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif

    <div class="perfil-card">
        <p><strong>Nombre:</strong> {{ $usuario->p_nombre }} {{ $usuario->p_apellido }}</p>
        <p><strong>Correo:</strong> {{ $usuario->correo }}</p>
        <p><strong>Teléfono:</strong> {{ $usuario->instructor->telefono ?? 'No registrado' }}</p>
        <p><strong>Tipo Documento:</strong> {{ $usuario->tipo_documento }}</p>
        <p><strong>Número Documento:</strong> {{ $usuario->num_documento }}</p>
    </div>

    <a href="{{ route('instructor.edit', $usuario->id_usuario) }}" class="btn-edit">
        ✏️ Editar Perfil
    </a>
</div>
@endsection
