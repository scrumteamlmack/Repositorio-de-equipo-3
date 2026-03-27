@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/fichas.create.css') }}">
</head>
<div class="container">
    <h2>Editar Ficha</h2>
    <form action="{{ route('fichas.update', $ficha->idficha) }}" method="POST">
        @csrf
        @method('PUT')

        <div class="form-group">
            <label>Número de Ficha</label>
            <input type="number" name="Num_ficha" class="form-control" 
                   value="{{ old('Num_ficha', $ficha->Num_ficha) }}" required>
        </div>

        <div class="form-group">
            <label>Programa</label>
            <select name="programas_id_programas" class="form-control" required>
                @foreach($programas as $programa)
                    <option value="{{ $programa->id_programas }}" 
                        {{ $programa->id_programas == $ficha->programas_id_programas ? 'selected' : '' }}>
                        {{ $programa->nombre_programa }}
                    </option>
                @endforeach
            </select>
        </div>

       <div class="form-group">
    <label>Modalidad</label>
    <select name="modalidad_id" class="form-control" required>
        @foreach($modalidades as $modalidad)
            <option value="{{ $modalidad->id_modalidad }}"
                {{ $modalidad->id_modalidad == $ficha->modalidad_id ? 'selected' : '' }}>
                {{ $modalidad->nombre_modalidad }}
            </option>
        @endforeach
    </select>
</div>


        <div class="form-group">
            <label>Instructor</label>
            <select name="instructor_Usuario_id_usuario" class="form-control">
                <option value="">-- Sin Asignar --</option>
                @foreach($instructores as $instructor)
                    <option value="{{ $instructor->Usuario_id_usuario }}"
                        {{ $instructor->Usuario_id_usuario == $ficha->instructor_Usuario_id_usuario ? 'selected' : '' }}>
                        {{ $instructor->usuario->p_nombre }} {{ $instructor->usuario->p_apellido }}
                    </option>
                @endforeach
            </select>
        </div>

        <button type="submit" class="btn btn-success">Actualizar</button>
    </form>
</div>
@endsection
