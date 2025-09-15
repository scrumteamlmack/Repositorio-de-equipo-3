@extends('layouts.app')

@section('content')
<div class="container">
    <h2>Registrar Usuario</h2>

    @if ($errors->any())
        <div class="alert alert-danger">
            <ul>
                @foreach ($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
    @endif

    <form action="{{ route('usuarios.store') }}" method="POST">
        @csrf

        <div class="mb-3">
            <label for="p_nombre" class="form-label">Primer Nombre</label>
            <input type="text" class="form-control" name="p_nombre" required>
        </div>

        <div class="mb-3">
            <label for="s_nombre" class="form-label">Segundo Nombre</label>
            <input type="text" class="form-control" name="s_nombre">
        </div>

        <div class="mb-3">
            <label for="p_apellido" class="form-label">Primer Apellido</label>
            <input type="text" class="form-control" name="p_apellido" required>
        </div>

        <div class="mb-3">
            <label for="s_apellido" class="form-label">Segundo Apellido</label>
            <input type="text" class="form-control" name="s_apellido">
        </div>

        <div class="mb-3">
            <label for="tipo_documento" class="form-label">Tipo Documento</label>
            <select class="form-control" name="tipo_documento" required>
                <option value="CC">Cédula de Ciudadanía</option>
                <option value="TI">Tarjeta de Identidad</option>
                <option value="CE">Cédula de Extranjería</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="num_documento" class="form-label">Número Documento</label>
            <input type="text" class="form-control" name="num_documento" required>
        </div>

        <div class="mb-3">
            <label for="correo" class="form-label">Correo</label>
            <input type="email" class="form-control" name="correo" required>
        </div>

        <div class="mb-3">
            <label for="contraseña" class="form-label">Contraseña</label>
            <input type="password" class="form-control" name="contraseña" required>
        </div>

        <div class="mb-3">
            <label for="id_rol" class="form-label">Rol</label>
            <select class="form-control" name="rol_id" required>
                <option value="">-- Seleccione un rol --</option>
                <option value="1">Administrador</option>
                <option value="2">Instructor</option>
                <option value="3">Aprendiz</option>
                <option value="4">Guarda de Seguridad</option>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Registrar Usuario</button>
    </form>
</div>
@endsection
