@extends('layouts.admin')

@section('content')
<div class="container">
    <h2>Mis Asistencias</h2>

    <table class="table table-striped">
        <thead>
            <tr>
                <th>Fecha</th>
                <th>Ambiente</th>
                <th>Programa</th>
                <th>Estado</th>
            </tr>
        </thead>
        <tbody>
            @foreach($asistencias as $asistencia)
                <tr>
                    <td>{{ $asistencia->fecha }}</td>
                    <td>{{ $asistencia->ambiente->num_ambiente ?? 'N/A' }}</td>
                    <td>{{ $asistencia->programa->nombre ?? 'N/A' }}</td>
                    <td>{{ $asistencia->estado }}</td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>
@endsection
