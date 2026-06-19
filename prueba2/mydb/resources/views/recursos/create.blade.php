@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/rec.create.css') }}">
</head>

<div class="container">
    <h1>Crear Recurso</h1>

    @if ($errors->any())
        <div class="error-box">
            <ul>
                @foreach ($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
    @endif

    <form action="{{ route('recursos.store') }}" method="POST">
        @csrf

        <label>Serial:</label>
        <input type="text" name="serial_recurso" value="{{ old('serial_recurso') }}" required>

        <label>Número:</label>
        <input type="number" name="num_recurso" value="{{ old('num_recurso') }}" required>

        <label>Nombre:</label>
        <input type="text" name="nombre_recurso" value="{{ old('nombre_recurso') }}" required>

        <label>Tipo de Recurso:</label>
        <select name="tipo_recurso" required>
            <option value="">-- Seleccione --</option>
            @foreach($tipos as $tipo)
                <option value="{{ $tipo->id_tipo_recurso }}" {{ old('tipo_recurso') == $tipo->id_tipo_recurso ? 'selected' : '' }}>
                    {{ $tipo->recurso_tipo }}
                </option>
            @endforeach
        </select>

        <label>Estado:</label>
        <select name="estado" required>
            <option value="Disponible">Disponible</option>
            <option value="En mantenimiento">En mantenimiento</option>
            <option value="Dañado">Dañado</option>
        </select>

        <label>Observación:</label>
        <textarea name="observacion">{{ old('observacion') }}</textarea>

        <label>Ambiente:</label>
        <select name="ambiente_id" required>
            <option value="">-- Seleccione --</option>
            @foreach($ambientes as $ambiente)
                <option value="{{ $ambiente->id_ambiente }}" {{ old('ambiente_id') == $ambiente->id_ambiente ? 'selected' : '' }}>
                    {{ $ambiente->num_ambiente }}
                </option>
            @endforeach
        </select>

        <button type="submit">💾 Guardar</button>
        <a href="{{ route('recursos.index') }}">❌ Cancelar</a>
    </form>
</div>
@endsection
