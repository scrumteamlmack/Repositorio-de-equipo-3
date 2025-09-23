@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/incidentes-edit.css') }}">
</head>
<div class="container">
    <h1>Editar Incidente</h1>

    <form action="{{ route('incidentes.update', $incidente->id_incidente) }}" method="POST">
        @csrf @method('PUT')

        <div class="mb-3">
            <label>Descripción</label>
            <textarea name="descripcion" class="form-control">{{ $incidente->descripcion }}</textarea>
        </div>

        <div class="mb-3">
            <label>Fecha</label>
            <input 
                type="date" 
                name="fecha_incidente" 
                class="form-control" 
                value="{{ $incidente->fecha_incidente ? $incidente->fecha_incidente->format('Y-m-d') : '' }}" 
                required>
        </div>

        <div class="mb-3">
            <label>Hora</label>
            <input 
                type="time" 
                name="hora_incidente" 
                class="form-control" 
                value="{{ $incidente->hora_incidente ? $incidente->hora_incidente->format('H:i') : '' }}" 
                required>
        </div>

        <div class="mb-3">
            <label>Ambiente</label>
            <select name="ambiente_id" class="form-control" required>
                @foreach($ambientes as $ambiente)
                    <option value="{{ $ambiente->id_ambiente }}" 
                        @if($ambiente->id_ambiente == $incidente->ambiente_id) selected @endif>
                        {{ $ambiente->num_ambiente }}
                    </option>
                @endforeach
            </select>
        </div>

        <div class="mb-3">
            <label>Tipo de Incidente</label>
            <select name="tipo_inc_id" class="form-control" required>
                @foreach($tipos as $tipo)
                    <option value="{{ $tipo->id_tipo_inc }}" 
                        @if($tipo->id_tipo_inc == $incidente->tipo_inc_id) selected @endif>
                        {{ $tipo->tipo_incidente }}
                    </option>
                @endforeach
            </select>
        </div>

        <div class="mb-3">
            <label>Instructor</label>
            <select name="usuario_id_usuario" class="form-control" required>
                @foreach($usuarios as $usuario)
                    <option value="{{ $usuario->id_usuario }}" 
                        @if($usuario->id_usuario == $incidente->usuario_id_usuario) selected @endif>
                        {{ $usuario->p_nombre }} {{ $usuario->p_apellido }}
                    </option>
                @endforeach
            </select>
        </div>

        <button type="submit" class="btn btn-success">Actualizar</button>
        <a href="{{ route('incidentes.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection
