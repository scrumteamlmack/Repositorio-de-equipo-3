@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/recursos.css') }}">
</head>
<div class="container">
    <h1 class="titulo-lista">💻 Lista de Recursos</h1>
    <a href="{{ route('recursos.create') }}" class="btn-nuevo">➕ Nuevo Recurso</a>

    <div class="table-container">
        <table class="tabla-estilizada">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Serial</th>
                    <th>Número</th>
                    <th>Nombre</th>
                    <th>Tipo</th>
                    <th>Estado</th>
                    <th>Observación</th>
                    <th>Ambiente</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                @foreach($recursos as $recurso)
                <tr>
                    <td>{{ $recurso->id_recurso }}</td>
                    <td>{{ $recurso->serial_recurso }}</td>
                    <td>{{ $recurso->num_recurso }}</td>
                    <td>{{ $recurso->nombre_recurso }}</td>
                    <td>{{ $recurso->tipoRecurso->recurso_tipo ?? 'N/A' }}</td>
                    <td>
                        @if($recurso->estado == 'Disponible')
                            <span class="badge badge-success">Disponible</span>
                        @elseif($recurso->estado == 'Dañado')
                            <span class="badge badge-danger">Dañado</span>
                        @elseif($recurso->estado == 'En mantenimiento')
                            <span class="badge badge-warning">En mantenimiento</span>
                        @else
                            <span class="badge badge-secondary">{{ $recurso->estado }}</span>
                        @endif
                    </td>
                    <td>{{ $recurso->observacion }}</td>
                    <td>{{ $recurso->ambiente->num_ambiente ?? 'N/A' }}</td>
                    <td class="acciones">
                        <a href="{{ route('recursos.show', $recurso->id_recurso) }}" class="btn btn-ver">👁 Ver</a>
                        <a href="{{ route('recursos.edit', $recurso->id_recurso) }}" class="btn btn-editar">✏ Editar</a>
                        <form action="{{ route('recursos.destroy', $recurso->id_recurso) }}" method="POST" style="display:inline;">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-eliminar" onclick="return confirm('¿Seguro que deseas eliminar este recurso?')">🗑 Eliminar</button>
                        </form>
                    </td>
                </tr>
                @endforeach
            </tbody>
        </table>
    </div>
</div>
@endsection

