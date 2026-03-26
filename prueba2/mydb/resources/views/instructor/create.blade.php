@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/instructor.css') }}">
</head>
<div class="form-container">
    <div class="form-card">
        <h2 class="form-title">Completar datos del Instructor</h2>

        <form action="{{ route('instructor.store') }}" method="POST">
            @csrf
            <input type="hidden" name="Usuario_id_usuario" value="{{ $usuario->id_usuario }}">

            <!-- Correo -->
            <div class="form-group">
                <label for="email">Correo institucional</label>
                <input type="email" value="{{ $usuario->correo }}" readonly>
            </div>

            <!-- Teléfono -->
            <div class="form-group">
                <label for="telefono">Teléfono</label>
                <input type="text" name="telefono" required>
            </div>

            <!-- Coordinación -->
            <div class="form-group">
                <label for="coordinacion">Coordinación</label>
                <select name="coordinacion_id_coordinacion" required>
                    <option value="">-- Seleccione una coordinación --</option>
                    @foreach($coordinaciones as $coordinacion)
                        <option value="{{ $coordinacion->id_coordinacion }}">
                            {{ $coordinacion->nombre_coordinacion }}
                        </option>
                    @endforeach
                </select>
            </div>

            <!-- Estado -->
            <div class="form-group">
                <label for="estado">Estado</label>
                <select name="estado" required>
                    <option value="Activo">Activo</option>
                    <option value="Inactivo">Inactivo</option>
                </select>
            </div>

            <!-- Botón -->
            <div class="form-actions">
                <button type="submit" class="btn-submit">Guardar</button>
            </div>
        </form>
    </div>
</div>
@endsection
