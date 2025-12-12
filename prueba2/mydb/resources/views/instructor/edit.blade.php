@extends('instructor.dashboard')

@section('content')
<div class="perfil-container">
    <h2>✏️ Editar Perfil</h2>

  <form action="{{ route('instructor.perfil.update', $usuario->id_usuario) }}" method="POST">
    @csrf
    @method('POST')

        <label>Nombre:</label>
        <input type="text" name="p_nombre" value="{{ old('p_nombre', $usuario->p_nombre) }}" required>

        <label>Apellido:</label>
        <input type="text" name="p_apellido" value="{{ old('p_apellido', $usuario->p_apellido) }}" required>

        <label>Correo:</label>
        <input type="email" name="correo" value="{{ old('correo', $usuario->correo) }}" required>

        <label for="telefono">Teléfono:</label>
    <input type="text" name="telefono" value="{{ old('telefono', $usuario->instructor->telefono) }}">

        <label>Tipo Documento:</label>
        <input type="text" name="tipo_documento" value="{{ old('tipo_documento', $usuario->tipo_documento) }}" required>

        <label>Número Documento:</label>
        <input type="text" name="num_documento" value="{{ old('num_documento', $usuario->num_documento) }}" required>

        <button type="submit" class="btn-update">Actualizar</button>
        <a href="{{ route('instructor.perfil') }}" class="btn-cancel">Cancelar</a>
    </form>
</div>
@endsection
