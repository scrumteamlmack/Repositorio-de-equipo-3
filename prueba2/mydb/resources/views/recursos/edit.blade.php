@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/rec.edit.css') }}">
</head>

<div class="container">
    <h1>Editar Recurso</h1>

    @if ($errors->any())
        <div class="error-box">
            <ul>
                @foreach ($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
    @endif

    <form action="{{ route('recursos.update', $recurso->id_recurso) }}" method="POST">
        @csrf
        @method('PUT')

        <label>Serial:</label>
        <input type="text" name="serial_recurso" value="{{ old('serial_recurso', $recurso->serial_recurso) }}" required>

        <label>Número:</label>
        <input type="number" name="num_recurso" value="{{ old('num_recurso', $recurso->num_recurso) }}" required>

        <label>Nombre:</label>
        <input type="text" name="nombre_recurso" value="{{ old('nombre_recurso', $recurso->nombre_recurso) }}" required>

        <label>Tipo de Recurso:</label>
        <select name="tipo_recurso" required>
            @foreach($tipos as $tipo)
                <option value="{{ $tipo->id_tipo_recurso }}" {{ $recurso->tipo_recurso == $tipo->id_tipo_recurso ? 'selected' : '' }}>
                    {{ $tipo->recurso_tipo }}
                </option>
            @endforeach
        </select>

        <label>Estado:</label>
        <select name="estado" required>
            <option value="Disponible" {{ $recurso->estado == 'Disponible' ? 'selected' : '' }}>Disponible</option>
            <option value="En mantenimiento" {{ $recurso->estado == 'En mantenimiento' ? 'selected' : '' }}>En mantenimiento</option>
            <option value="Dañado" {{ $recurso->estado == 'Dañado' ? 'selected' : '' }}>Dañado</option>
        </select>

        <label>Observación:</label>
        <textarea name="observacion">{{ old('observacion', $recurso->observacion) }}</textarea>

        <label>Ambiente:</label>
        <select name="ambiente_id" required>
            @foreach($ambientes as $ambiente)
                <option value="{{ $ambiente->id_ambiente }}" {{ $recurso->ambiente_id == $ambiente->id_ambiente ? 'selected' : '' }}>
                    {{ $ambiente->num_ambiente }}
                </option>
            @endforeach
        </select>

        <button type="submit">🔄 Actualizar</button>
        <a href="{{ route('recursos.index') }}">❌ Cancelar</a>
    </form>
</div>
@endsection
