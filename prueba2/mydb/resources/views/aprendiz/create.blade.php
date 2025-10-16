@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/aprendiz_create.css') }}">
</head>
<div class="form-container">
    <div class="form-card">
        <h2 class="form-title">Completar datos de Aprendiz</h2>

        <form action="{{ route('aprendiz.store') }}" method="POST">
            @csrf

            <!-- Campo oculto -->
            <input type="hidden" name="Usuario_id_usuario" value="{{ $usuario->id_usuario }}">

            <!-- Ficha -->
            <div class="form-group">
                <label for="ficha_idficha" class="form-label">Ficha</label>
                <select class="form-input" name="ficha_idficha" id="ficha_idficha" required>
                    <option value="">-- Seleccione una ficha --</option>
                    @foreach($fichas as $ficha)
                        <option value="{{ $ficha->idficha }}" {{ old('ficha_idficha') == $ficha->idficha ? 'selected' : '' }}>
                            {{ $ficha->Num_ficha ?? $ficha->num_ficha ?? $ficha->codigo ?? $ficha->idficha }}
                        </option>
                    @endforeach
                </select>
            </div>

            <!-- Programa -->
            <div class="form-group">
                <label for="programas_id_programas" class="form-label">Programa</label>
                <select class="form-input" name="programas_id_programas" id="programas_id_programas" required>
                    <option value="">-- Seleccione un programa --</option>
                    @foreach($programas as $programa)
                        <option value="{{ $programa->id_programas }}" {{ old('programas_id_programas') == $programa->id_programas ? 'selected' : '' }}>
                            {{ $programa->nombre_programa ?? $programa->nombre ?? $programa->descripcion ?? $programa->id_programas }}
                        </option>
                    @endforeach
                </select>
            </div>

            <!-- Botón -->
            <button type="submit" class="btn-submit">Guardar</button>
        </form>
    </div>
</div>
@endsection
