@extends('layouts.admin')

@section('content')
<div class="container">
    <h2>Editar Perfil</h2>

    <form method="POST" action="{{ route('perfil.update', $usuario->id_usuario) }}">
        @csrf
        @method('PUT')

        <div class="form-group">
            <label>Nombre</label>
            <input type="text" name="nombre" class="form-control" value="{{ $usuario->nombre }}" required>
        </div>

        <div class="form-group">
            <label>Correo</label>
            <input type="email" name="correo" class="form-control" value="{{ $usuario->correo }}" required>
        </div>

        <div class="form-group">
            <label>Teléfono</label>
            <input type="text" name="telefono" class="form-control" value="{{ $usuario->telefono }}">
        </div>

        <button type="submit" class="btn btn-success">Actualizar</button>
        <a href="{{ route('perfil.show', $usuario->id_usuario) }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection
