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
            <label for="p_nombre">Primer Nombre</label>
            <input type="text" name="p_nombre" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="s_nombre">Segundo Nombre</label>
            <input type="text" name="s_nombre" class="form-control">
        </div>

        <div class="mb-3">
            <label for="p_apellido">Primer Apellido</label>
            <input type="text" name="p_apellido" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="s_apellido">Segundo Apellido</label>
            <input type="text" name="s_apellido" class="form-control">
        </div>

        <div class="mb-3">
            <label for="tipo_documento">Tipo Documento</label>
            <select name="tipo_documento" class="form-control" required>
                <option value="CC">Cédula de Ciudadanía</option>
                <option value="TI">Tarjeta de Identidad</option>
                <option value="CE">Cédula de Extranjería</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="num_documento">Número Documento</label>
            <input type="text" name="num_documento" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="correo">Correo</label>
            <input type="email" name="correo" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="contraseña">Contraseña</label>
            <input type="password" name="contraseña" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="id_rol">Rol</label>
            <select name="id_rol" class="form-control" required>
                <option value="1">Aprendiz</option>
                <option value="2">Instructor</option>
                <option value="3">Coordinador</option>
                <option value="4">Guarda de Seguridad</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Registrar</button>
    </form>
</div>
@endsection
