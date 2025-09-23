@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/incidentes.css') }}">
</head>
<div class="incidente-container">
    <h2>📋 Lista de Incidentes</h2>

    <a href="{{ route('incidentes.create') }}" class="btn-registrar">➕ Nuevo Incidente</a>

    @if(session('success'))
        <div class="alert-success">
            {{ session('success') }}
        </div>
    @endif

    <div class="tabla-container">
        <table class="tabla-incidentes">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Descripción</th>
                    <th>Fecha</th>
                    <th>Hora</th>
                    <th>Ambiente</th>
                    <th>Tipo</th>
                    <th>Instructor</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                @forelse($incidentes as $incidente)
                    <tr>
                        <td>{{ $incidente->id_incidente }}</td>
                        <td>{{ $incidente->descripcion }}</td>
                        <td>{{ \Carbon\Carbon::parse($incidente->fecha_incidente)->format('Y-m-d') }}</td>
                        <td>{{ \Carbon\Carbon::parse($incidente->hora_incidente)->format('H:i') }}</td>
                        <td>{{ $incidente->ambiente->num_ambiente ?? 'N/A' }}</td>
                        <td>{{ $incidente->tipo_incidente->tipo_incidente ?? 'N/A' }}</td>
                        <td>{{ $incidente->usuario->p_nombre ?? 'N/A' }} {{ $incidente->usuario->p_apellido ?? '' }}</td>
                        <td class="acciones">
                            <a href="{{ route('incidentes.show', $incidente) }}" class="btn-show">👁 Ver</a>
                            <a href="{{ route('incidentes.edit', $incidente) }}" class="btn-warning">✏️ Editar</a>
                            <form action="{{ route('incidentes.destroy', $incidente) }}" method="POST" class="inline-form">
                                @csrf @method('DELETE')
                                <button type="submit" class="btn-danger" onclick="return confirm('¿Eliminar este incidente?')">🗑 Eliminar</button>
                            </form>
                        </td>
                    </tr>
                @empty
                    <tr>
                        <td colspan="8">⚠️ No hay incidentes registrados.</td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>
</div>
@endsection
