@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/traslado_edit.css') }}">
</head>

<div class="container">
    <h2>Editar Traslado de Recurso</h2>

    @if ($errors->any())
        <div class="alert alert-danger">
            <ul>
                @foreach ($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
    @endif

    <form action="{{ route('traslados.update', $traslado->id_traslado) }}" method="POST">
        @csrf
        @method('PUT')

        <div class="form-group">
            <label for="recurso_id">Recurso</label>
            <select name="recurso_id" class="form-control" required>
                @foreach($recursos as $recurso)
                    <option value="{{ $recurso->id_recurso }}" {{ $traslado->recurso_id == $recurso->id_recurso ? 'selected' : '' }}>
                        {{ $recurso->serial_recurso }}
                    </option>
                @endforeach
            </select>
        </div>

        <div class="form-group">
            <label for="ambiente_origen">Ambiente Origen</label>
            <select name="ambiente_origen" class="form-control" required>
                @foreach($ambientes as $ambiente)
                    <option value="{{ $ambiente->id_ambiente }}" {{ $traslado->ambiente_origen == $ambiente->id_ambiente ? 'selected' : '' }}>
                        {{ $ambiente->num_ambiente }} - {{ $ambiente->nombre }}
                    </option>
                @endforeach
            </select>
        </div>

        <div class="form-group">
            <label for="ambiente_destino">Ambiente Destino</label>
            <select name="ambiente_destino" class="form-control" required>
                @foreach($ambientes as $ambiente)
                    <option value="{{ $ambiente->id_ambiente }}" {{ $traslado->ambiente_destino == $ambiente->id_ambiente ? 'selected' : '' }}>
                        {{ $ambiente->num_ambiente }} - {{ $ambiente->nombre }}
                    </option>
                @endforeach
            </select>
        </div>

        <div class="form-group">
            <label for="fecha_traslado">Fecha de Traslado</label>
            <input type="datetime-local" name="fecha_traslado" class="form-control"
                value="{{ \Carbon\Carbon::parse($traslado->fecha_traslado)->format('Y-m-d\TH:i') }}" required>
        </div>

        <div class="form-group">
            <label for="observacion">Observación</label>
            <textarea name="observacion" class="form-control" rows="3">{{ $traslado->observacion }}</textarea>
        </div>

        <button type="submit" class="btn btn-success">Actualizar Traslado</button><br><br>
        <a href="{{ route('traslados.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection
