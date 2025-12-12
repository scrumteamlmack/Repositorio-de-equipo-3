@extends('layouts.app')

@section('content')
<div class="container">
    <h2>Completar datos de Instructor</h2>

    <form action="{{ route('instructor.store') }}" method="POST">
        @csrf
        <input type="hidden" name="Usuario_id_usuario" value="{{ $usuario->id_usuario }}">

        <div class="mb-3">
    <label for="email" class="form-label">Correo institucional</label>
    <input type="email" class="form-control" value="{{ $usuario->correo }}" readonly>

</div>


        <div class="mb-3">
            <label for="telefono" class="form-label">Teléfono</label>
            <input type="text" name="telefono" class="form-control" required>
        </div>

        <div class="mb-3">
    <label for="coordinacion" class="form-label">Coordinación</label>
    <select name="coordinacion_id_coordinacion" class="form-control" required>
        <option value="">-- Seleccione una coordinación --</option>
        @foreach($coordinaciones as $coordinacion)
            <option value="{{ $coordinacion->id_coordinacion }}">
                {{ $coordinacion->nombre_coordinacion }}
            </option>
        @endforeach
    </select>
</div>


        <div class="mb-3">
            <label for="estado" class="form-label">Estado</label>
            <select name="estado" class="form-control" required>
                <option value="Activo">Activo</option>
                <option value="Inactivo">Inactivo</option>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Guardar</button>
    </form>
</div>
@endsection
