@extends('layouts.admin')

@section('content')
<head>
        <!-- Estilos propios -->
  
    <link rel="stylesheet" href="{{ asset('css/usuarios.css') }}">
</head>
    <main class="usuario-container">
        <h2>Lista de Usuarios</h2>

        <!-- Botón Registrar -->
        <a href="{{ route('usuarios.create') }}" class="btn-registrar">Registrar Usuario</a>

        <!-- Mensaje de éxito -->
        @if(session('success'))
            <div class="alert-success">{{ session('success') }}</div>
        @endif

        <!-- Tabla de usuarios -->
        <div class="tabla-container">
            <table class="tabla-usuarios">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombres</th>
                        <th>Apellidos</th>
                        <th>Documento</th>
                        <th>Correo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                @foreach($usuarios as $usuario)
                    <tr>
                        <td>{{ $usuario->id_usuario }}</td>
                        <td>{{ $usuario->p_nombre }} {{ $usuario->s_nombre }}</td>
                        <td>{{ $usuario->p_apellido }} {{ $usuario->s_apellido }}</td>
                        <td>{{ $usuario->tipo_documento }} {{ $usuario->num_documento }}</td>
                        <td>{{ $usuario->correo }}</td>
                        <td class="acciones">
                            <a href="{{ route('usuarios.edit', $usuario->id_usuario) }}" class="btn-warning">Editar</a>
                            <form action="{{ route('usuarios.destroy', $usuario->id_usuario) }}" method="POST" class="inline-form">
                                @csrf
                                @method('DELETE')
                                <button type="submit" class="btn-danger" onclick="return confirm('¿Seguro que deseas eliminar este usuario?')">
                                    Eliminar
                                </button>
                            </form>
                        </td>
                    </tr>
                @endforeach
                </tbody>
            </table>
        </div>
    </main>


@endsection
