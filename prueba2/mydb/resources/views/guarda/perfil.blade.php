@extends('guarda.dashboard')

@section('content')
<link rel="stylesheet" href="{{ asset('css/perfilGuarda.css') }}">

<div class="perfil-container">
    <h2>👤 Mi Perfil</h2>

    @if(session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif

    <div class="perfil-card">
        <p><strong>Nombre:</strong> {{ $usuario->p_nombre }} {{ $usuario->p_apellido }}</p>
        <p><strong>Correo:</strong> {{ $usuario->correo }}</p>
        <p><strong>Tipo Documento:</strong> {{ $usuario->tipo_documento }}</p>
        <p><strong>Número Documento:</strong> {{ $usuario->num_documento }}</p>
        <p><strong>Turno:</strong> {{ $usuario->guarda_seguridad->turno ?? 'No registrado' }}</p>
    </div>

    <a href="{{ route('guarda.editar') }}" class="btn-edit">✏️ Editar Perfil</a>
</div>
@endsection
