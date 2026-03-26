@extends('layouts.admin')

@section('content')
<head>
        
    <link rel="stylesheet" href="{{ asset('css/form_amb.css') }}">
    </head>
<div class="ambiente-container">
    <h1>Crear nuevo ambiente</h1>

    {{-- Mostrar errores de validación --}}
    @if ($errors->any())
        <div class="alert-error">
            <ul>
                @foreach ($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
    @endif

    <form action="{{ route('ambientes.store') }}" method="POST" class="form-ambiente">
        @csrf

        <div class="form-group">
            <label for="num_ambiente">Número de Ambiente:</label>
            <input type="text" name="num_ambiente" id="num_ambiente" value="{{ old('num_ambiente') }}" required>
        </div>

        <div class="form-group">
            <label for="capacidad">Capacidad:</label>
            <input type="number" name="capacidad" id="capacidad" value="{{ old('capacidad') }}" required>
        </div>

        <div class="form-group">
            <label for="tipo_ambiente">Tipo de Ambiente:</label>
            <input type="text" name="tipo_ambiente" id="tipo_ambiente" value="{{ old('tipo_ambiente') }}" required>
        </div>

        <div class="form-group">
            <label for="estado">Estado:</label>
            <select name="estado" id="estado" required>
                <option value="">-- Selecciona --</option>
                <option value="Libre" {{ old('estado') == 'Libre' ? 'selected' : '' }}>Libre</option>
                <option value="Ocupado" {{ old('estado') == 'Ocupado' ? 'selected' : '' }}>Ocupado</option>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit">✅ Guardar</button>
            <a href="{{ route('ambientes.index') }}">⬅️ Volver</a>
        </div>
    </form>
</div>
@endsection
