@extends('layouts.app')

@section('content')
<div class="container">
    <h2>Completar datos de Aprendiz</h2>

    <form action="{{ route('aprendiz.store') }}" method="POST">
        @csrf

        <!-- Usamos $usuario->id_usuario porque el controlador envía $usuario -->
        <input type="hidden" name="Usuario_id_usuario" value="{{ $usuario->id_usuario }}">

        <div class="mb-3">
            <label for="ficha_idficha" class="form-label">Ficha</label>
            <select class="form-control" name="ficha_idficha" id="ficha_idficha" required>
                <option value="">-- Seleccione una ficha --</option>
                @foreach($fichas as $ficha)
                    <option value="{{ $ficha->idficha }}" {{ old('ficha_idficha') == $ficha->idficha ? 'selected' : '' }}>
                        {{ $ficha->Num_ficha ?? $ficha->num_ficha ?? $ficha->codigo ?? $ficha->idficha }}
                    </option>
                @endforeach
            </select>
        </div>

        <div class="mb-3">
            <label for="programas_id_programas" class="form-label">Programa</label>
            <select class="form-control" name="programas_id_programas" id="programas_id_programas" required>
                <option value="">-- Seleccione un programa --</option>
                @foreach($programas as $programa)
                    <option value="{{ $programa->id_programas }}" {{ old('programas_id_programas') == $programa->id_programas ? 'selected' : '' }}>
                        {{ $programa->nombre_programa ?? $programa->nombre ?? $programa->descripcion ?? $programa->id_programas }}
                    </option>
                @endforeach
            </select>
        </div>

        <button type="submit" class="btn btn-success">Guardar</button>
    </form>
</div>
@endsection
