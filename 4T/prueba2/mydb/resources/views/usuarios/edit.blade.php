@extends('layouts.app')

@section('content')
<div class="container">
    <h2>Editar Usuario</h2>

    @if ($errors->any())
        <div class="alert alert-danger">
            <ul>
                @foreach ($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
    @endif

    <form action="{{ route('usuarios.update', $usuario->id_usuario) }}" method="POST">
    @csrf
    @method('PUT')


        <table class="table table-bordered">
            <tr>
                <th>Primer Nombre</th>
                <td>
                    <input type="text" name="p_nombre" class="form-control"
                        value="{{ old('p_nombre', $usuario->p_nombre) }}" required>
                </td>
            </tr>

            <tr>
                <th>Segundo Nombre</th>
                <td>
                    <input type="text" name="s_nombre" class="form-control"
                        value="{{ old('s_nombre', $usuario->s_nombre) }}">
                </td>
            </tr>

            <tr>
                <th>Primer Apellido</th>
                <td>
                    <input type="text" name="p_apellido" class="form-control"
                        value="{{ old('p_apellido', $usuario->p_apellido) }}" required>
                </td>
            </tr>

            <tr>
                <th>Segundo Apellido</th>
                <td>
                    <input type="text" name="s_apellido" class="form-control"
                        value="{{ old('s_apellido', $usuario->s_apellido) }}">
                </td>
            </tr>

            <tr>
                <th>Tipo Documento</th>
                <td>
                    <select name="tipo_documento" class="form-control" required>
                        <option value="">-- Seleccione --</option>
                        <option value="CC" {{ $usuario->tipo_documento == 'CC' ? 'selected' : '' }}>Cédula de Ciudadanía</option>
                        <option value="TI" {{ $usuario->tipo_documento == 'TI' ? 'selected' : '' }}>Tarjeta de Identidad</option>
                        <option value="CE" {{ $usuario->tipo_documento == 'CE' ? 'selected' : '' }}>Cédula de Extranjería</option>
                        <option value="OTRO" {{ $usuario->tipo_documento == 'OTRO' ? 'selected' : '' }}>Otro</option>
                    </select>
                </td>
            </tr>

            <tr>
                <th>Número Documento</th>
                <td>
                    <input type="text" name="num_documento" class="form-control"
                        value="{{ old('num_documento', $usuario->num_documento) }}" required>
                </td>
            </tr>

            <tr>
                <th>Correo</th>
                <td>
                    <input type="email" name="correo" class="form-control"
                        value="{{ old('correo', $usuario->correo) }}" required>
                </td>
            </tr>

            <tr>
                <th>Contraseña</th>
                <td>
                    <input type="password" name="contraseña" class="form-control"
                        placeholder="Dejar en blanco si no desea cambiar">
                </td>
            </tr>

            <tr>
                <th>Confirmar Contraseña</th>
                <td>
                    <input type="password" name="contraseña_confirmation" class="form-control"
                        placeholder="Repite la nueva contraseña">
                </td>
            </tr>
        </table>

        <button type="submit" class="btn btn-primary">Actualizar</button>
        <a href="{{ route('usuarios.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection
