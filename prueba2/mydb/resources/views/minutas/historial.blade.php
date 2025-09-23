@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/minuta.css') }}">
</head>

<div class="container">
    <h1>📜 Historial de Minutas</h1>

    <a href="{{ route('minutas.index') }}" class="btn-create">⬅ Volver al Dashboard</a>

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
                <td>{{ $minuta->guarda_seguridad->usuario->p_nombre ?? 'N/A' }}</td>
                <td>{{ $minuta->instructor->usuario->p_nombre ?? 'N/A' }}</td>
                <td class="actions">
                    <a href="{{ route('minutas.show', $minuta) }}" class="btn-view">👁 Ver</a>
                    <a href="{{ route('minutas.edit', $minuta) }}" class="btn-edit">✏️ Editar</a>
                </td>
            </tr>
            @endforeach
        </tbody>
    </table>
</div>
@endsection
