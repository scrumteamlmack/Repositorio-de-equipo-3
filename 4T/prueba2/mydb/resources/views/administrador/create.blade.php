@extends('layouts.app')

@section('content')
<div class="container">
    <h2>Completar datos de Coordinador</h2>

    <form action="{{ route('administrador.store') }}" method="POST">
        @csrf

        <!-- Usuario asociado -->
        <input type="hidden" name="Usuario_id_usuario" value="{{ $usuario->id_usuario }}">

        <div class="mb-3">
            <label for="coordinacion_id_coordinacion" class="form-label">Coordinación</label>
            <select name="coordinacion_id_coordinacion" id="coordinacion_id_coordinacion" class="form-control" required>
                <option value="">-- Seleccione una coordinación --</option>
                @foreach($coordinaciones as $coord)
                    <option value="{{ $coord->id_coordinacion }}">
                        {{ $coord->nombre ?? $coord->nombre_coordinacion ?? $coord->id_coordinacion }}
                    </option>
                @endforeach
            </select>
        </div>

        <button type="submit" class="btn btn-success">Guardar</button>
    </form>
</div>
@endsection
