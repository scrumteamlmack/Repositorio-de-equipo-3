@extends('layouts.aprendiz')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/asistencia_aprendiz.css') }}">
</head>
<div class="perfil-container">
    <h2>Mis Asistencias</h2>

    <table class="table">
        <thead>
            <tr>
                <th>Fecha</th>
                <th>Estado</th>
                <th>Jornada</th>
            </tr>
        </thead>
        <tbody>
            @foreach($asistencias as $asistencia)
                <tr>
                    <td>{{ $asistencia->fecha_inasistencia->format('d-m-Y') }}</td>
                    <td>{{ $asistencia->estado_inasistencia }}</td>
                    <td>{{ $asistencia->jornada->nombre_jornada ?? 'N/A' }}</td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>
@endsection
