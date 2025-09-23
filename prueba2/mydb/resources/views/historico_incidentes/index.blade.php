@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/historico_incidentes.css') }}">
</head>

<div class="container">
    <h2 class="title">Histórico de Incidentes</h2>

    <table class="custom-table">
        <thead>
            <tr>
                <th>ID Histórico</th>
                <th>ID Incidente</th>
                <th>Ambiente</th>
                <th>Tipo Incidente</th>
                <th>Descripción</th>
                <th>Fecha Registro</th>
            </tr>
        </thead>
        <tbody>
            @forelse($historicos as $h)
                <tr>
                    <td>{{ $h->id_historico }}</td>
                    <td>{{ $h->registro_incidente->id_incidente ?? 'N/A' }}</td>
                    <td>{{ $h->ambiente->num_ambiente ?? 'N/A' }}</td>
                    <td>{{ $h->tipo_incidente->tipo_incidente ?? 'N/A' }}</td>
                    <td>{{ $h->descripcion }}</td>
                    <td>{{ $h->fecha_registro->format('d/m/Y H:i') }}</td>
                </tr>
            @empty
                <tr>
                    <td colspan="6">No hay registros en el historial.</td>
                </tr>
            @endforelse
        </tbody>
    </table>

    <div class="pagination">
        {{ $historicos->links() }}
    </div>
</div>
@endsection
