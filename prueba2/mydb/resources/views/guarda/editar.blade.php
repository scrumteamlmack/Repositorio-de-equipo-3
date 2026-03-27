@extends('guarda.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/edit_guarda.css') }}">
</head>
<div class="perfil-container">
    <h2>✏️ Editar Perfil</h2>

    @if(session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif

    <form action="{{ route('guarda.actualizar') }}" method="POST">
        @csrf

        <label>Nombre:</label>
        <input type="text" name="p_nombre" value="{{ old('p_nombre', $usuario->p_nombre) }}" required>

        <label>Apellido:</label>
        <input type="text" name="p_apellido" value="{{ old('p_apellido', $usuario->p_apellido) }}" required>

        <label>Correo:</label>
        <input type="email" name="correo" value="{{ old('correo', $usuario->correo) }}" required>

        <label>Turno:</label>
        <input type="text" name="turno" value="{{ old('turno', $usuario->guarda_seguridad->turno ?? '') }}" required>

        <label>Tipo Documento:</label>
        <input type="text" name="tipo_documento" value="{{ old('tipo_documento', $usuario->tipo_documento) }}" required>

        <label>Número Documento:</label>
        <input type="text" name="num_documento" value="{{ old('num_documento', $usuario->num_documento) }}" required>

        <button type="submit" class="btn-update">Actualizar</button>
        <a href="{{ route('guarda.perfil') }}" class="btn-cancel">Cancelar</a>
    </form>
</div>
@endsection
