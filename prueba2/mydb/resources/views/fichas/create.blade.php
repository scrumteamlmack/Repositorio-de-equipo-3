@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/fichas.create.css') }}">
</head>
<div class="container">
    <h2>Registrar Nueva Ficha</h2>

    <form action="{{ route('fichas.store') }}" method="POST">
        @csrf

        <!-- Número Ficha -->
        <div class="form-group">
            <label for="Num_ficha">Número de Ficha</label>
            <input type="number" name="Num_ficha" id="Num_ficha" class="form-control" required>
        </div>

 <!-- Programa -->
<div class="form-group">
    <label for="programas_id_programas">Programa</label>
    <select name="programas_id_programas" id="programas_id_programas" class="form-control" required>
        <option value="">-- Seleccione un programa --</option>
        @foreach($programas as $programa)
            <option value="{{ $programa->id_programas }}">{{ $programa->nombre_programa }}</option>
        @endforeach
    </select>
</div>


<!-- Modalidad -->
<div class="form-group">
    <label for="modalidad_id">Modalidad</label>
    <select name="modalidad_id" id="modalidad_id" class="form-control" required>
        <option value="">-- Seleccione una modalidad --</option>
        @foreach($modalidades as $modalidad)
            <option value="{{ $modalidad->id_modalidad }}">{{ $modalidad->nombre_modalidad }}</option>
        @endforeach
    </select>
</div>





        <!-- Instructor -->
        <div class="form-group">
            <label for="instructor_Usuario_id_usuario">Instructor</label>
            <select name="instructor_Usuario_id_usuario" id="instructor_Usuario_id_usuario" class="form-control">
                <option value="">-- Seleccione un instructor --</option>
                @foreach($instructores as $instructor)
                    <option value="{{ $instructor->Usuario_id_usuario }}">
                        {{ $instructor->usuario->p_nombre }} {{ $instructor->usuario->p_apellido }}
                    </option>
                @endforeach
            </select>
        </div>

        <button type="submit" class="btn btn-success">Guardar</button>
    </form>
</div>
@endsection
