@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/traslado_index.css') }}">
</head>

<div class="container">
    <h2 class="mb-4">Gestión de Traslados de Recursos</h2>

    <a href="{{ route('traslados.create') }}" class="btn btn-success mb-3">Registrar Traslado</a>

    @if(session('success'))
        <div class="alert alert-success">{{ session('success') }}</div>
    @endif

    <table class="table table-bordered table-striped">
        <thead>
            <tr>
                <th>ID Traslado</th>
                <th>Recurso</th>
                <th>Ambiente Origen</th>
                <th>Ambiente Destino</th>
                <th>Fecha Traslado</th>
                <th>Observación</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            @foreach($traslados as $traslado)
                <tr>
                    <td>{{ $traslado->id_traslado }}</td>
                    <td>{{ $traslado->recurso_id ?? 'N/A' }}</td>
                    <td>{{ $traslado->ambienteOrigen->num_ambiente ?? 'N/A' }}</td>
                    <td>{{ $traslado->ambienteDestino->num_ambiente ?? 'N/A' }}</td>
                    <td>{{ $traslado->fecha_traslado }}</td>
                    <td>{{ $traslado->observacion }}</td>
                    <td>
                        <a href="{{ route('traslados.edit', $traslado->id_traslado) }}" class="btn btn-warning btn-sm">Editar</a>
                        <form action="{{ route('traslados.destroy', $traslado->id_traslado) }}" method="POST" style="display:inline-block;">
                            @csrf
                            @method('DELETE')
                            <button class="btn btn-danger btn-sm" onclick="return confirm('¿Eliminar este traslado?')">Eliminar</button>
                        </form>
                    </td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>
@endsection
