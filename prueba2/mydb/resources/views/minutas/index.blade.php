@extends('instructor.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/minuta.css') }}">
</head>

<div class="container">
    <h1>📑 Listado de Minutas</h1>

    @if(session('success'))
        <div class="alert success">
            {{ session('success') }}
        </div>
    @endif

    <a href="{{ route('minutas.create') }}" class="btn-create">➕ Nueva Minuta</a>

    <table class="styled-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Fecha Recibo</th>
                <th>Fecha Entrega</th>
                <th>Novedad</th>
                <th>Descripción</th>
                <th>Estado</th>
                <th>Ambiente</th>
                <th>Guarda Seguridad</th>
                <th>Responsable</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            @foreach($minutas as $minuta)
            <tr>
                <td>{{ $minuta->id }}</td>
                <td>{{ $minuta->fecha_hora_recibo }}</td>
                <td>{{ $minuta->fecha_hora_entrega }}</td>
                <td>{{ $minuta->novedad ?? 'N/A' }}</td>
                <td>{{ $minuta->descripcion_min ?? 'N/A' }}</td>
                <td>{{ $minuta->estado }}</td>
                <td>{{ $minuta->ambiente->num_ambiente ?? 'N/A' }}</td>
                <td>{{ $minuta->guarda_seguridad->usuario->p_nombre ?? 'N/A' }} {{ $minuta->guarda_seguridad->usuario->p_apellido ?? 'N/A' }}</td>
                <td>{{ $minuta->instructor->usuario->p_nombre ?? 'N/A' }} {{ $minuta->instructor->usuario->p_apellido ?? 'N/A' }}</td>
                <td class="actions">
                    <a href="{{ route('minutas.show', $minuta) }}" class="btn-view">👁 Ver</a>
                    <a href="{{ route('minutas.edit', $minuta) }}" class="btn-edit">✏️ Editar</a>
                    <form action="{{ route('minutas.destroy', $minuta) }}" method="POST" style="display:inline;">
                        @csrf
                        @method('DELETE')
                        <button type="submit" class="btn-delete" onclick="return confirm('¿Seguro que deseas eliminar esta minuta?')">🗑 Eliminar</button>
                    </form>
                </td>
            </tr>
            @endforeach
        </tbody>
    </table>
</div>
@endsection
