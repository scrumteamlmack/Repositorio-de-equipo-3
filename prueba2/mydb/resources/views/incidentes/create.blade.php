@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/form_inc.css') }}">
</head>
<div class="container">
    <h1>Registrar Incidente</h1>

    <form action="{{ route('incidentes.store') }}" method="POST">
        @csrf
        <div class="mb-3">
            <label>Descripción</label>
            <textarea name="descripcion" class="form-control"></textarea>
        </div>
        <div class="mb-3">
            <label>Fecha</label>
            <input type="date" name="fecha_incidente" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>Hora</label>
            <input type="time" name="hora_incidente" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>Ambiente</label>
            <select name="ambiente_id" class="form-control" required>
                @foreach($ambientes as $ambiente)
                    <option value="{{ $ambiente->id_ambiente }}">
    {{ $ambiente->num_ambiente }}
</option>

                @endforeach
            </select>
        </div>
        <div class="mb-3">
            <label>Tipo de Incidente</label>
            <select name="tipo_inc_id" class="form-control" required>
                @foreach($tipos as $tipo)
                   <option value="{{ $tipo->id_tipo_inc }}">{{ $tipo->tipo_incidente }}</option>

                @endforeach
            </select>
        </div>
        <div class="mb-3">
            <label>Instructor</label>
            <select name="usuario_id_usuario" class="form-control" required>
                @foreach($usuarios as $usuario)
                    <option value="{{ $usuario->id_usuario }}">{{ $usuario->p_nombre }} {{ $usuario->p_apellido }}</option>
                @endforeach
            </select>
        </div>
        <button type="submit" class="btn btn-success">Guardar</button>
        <a href="{{ route('incidentes.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection
