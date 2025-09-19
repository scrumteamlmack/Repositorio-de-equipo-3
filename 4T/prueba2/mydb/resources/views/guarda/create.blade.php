@extends('layouts.app')

@section('content')
<div class="container">
    <h2>Completar datos del Guarda de Seguridad</h2>

    <form action="{{ route('guarda.store') }}" method="POST">
        @csrf
        <input type="hidden" name="Usuario_id_usuario" value="{{ $usuario->id_usuario }}">

        <div class="mb-3">
            <label for="turno" class="form-label">Turno</label>
            <select class="form-control" name="turno" required>
                <option value="">-- Seleccione un turno --</option>
                <option value="Mañana">Mañana</option>
                <option value="Tarde">Tarde</option>
                <option value="Noche">Noche</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="fecha_ingreso" class="form-label">Fecha de Ingreso</label>
            <input type="date" class="form-control" name="fecha_ingreso" required>
        </div>

        <div class="mb-3">
            <label for="estado" class="form-label">Estado</label>
            <select class="form-control" name="estado" required>
                <option value="">-- Seleccione estado --</option>
                <option value="Activo">Activo</option>
                <option value="Inactivo">Inactivo</option>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Guardar</button>
    </form>
</div>
@endsection
