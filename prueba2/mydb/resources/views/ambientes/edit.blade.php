@extends('layouts.admin')

@section('title', 'Editar Ambiente')
<head>
    <link rel="stylesheet" href="{{ asset('css/edit_ambiente.css') }}">
</head>

@section('content')
<div class="edit-ambiente-container">
    <h1>Editar Ambiente</h1>

    <form action="{{ route('ambientes.update', $ambiente->id_ambiente) }}" method="POST" class="edit-ambiente-form">
        @csrf
        @method('PUT')

        <div class="form-group">
            <label for="num_ambiente">Número de ambiente:</label>
            <input type="text" name="num_ambiente" value="{{ $ambiente->num_ambiente }}" required>
        </div>

        <div class="form-group">
            <label for="capacidad">Capacidad:</label>
            <input type="number" name="capacidad" value="{{ $ambiente->capacidad }}" required>
        </div>

        <div class="form-group">
            <label for="tipo_ambiente">Tipo de ambiente:</label>
            <input type="text" name="tipo_ambiente" value="{{ $ambiente->tipo_ambiente }}" required>
        </div>

        <div class="form-group">
            <label for="estado">Estado:</label>
            <input type="text" name="estado" value="{{ $ambiente->estado }}" required>
        </div>

        <div class="form-buttons">
            <button type="submit" class="btn-actualizar">✅ Actualizar</button>
            <a href="{{ route('ambientes.index') }}" class="btn-volver">⬅️ Volver</a>
        </div>
    </form>
</div>
@endsection
