@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/incidentes-show.css') }}">
</head>
<div class="container">
    <h1>Detalle del Incidente</h1>

    <p><strong>ID:</strong> {{ $incidente->id_incidente }}</p>
    <p><strong>Descripción:</strong> {{ $incidente->descripcion }}</p>
    <p><strong>Fecha:</strong> {{ $incidente->fecha_incidente->format('Y-m-d') }}</p>
    <p><strong>Hora:</strong> {{ $incidente->hora_incidente->format('H:i') }}</p>
    <p><strong>Ambiente:</strong> {{ $incidente->ambiente->num_ambiente ?? 'N/A' }}</p>
    <p><strong>Tipo:</strong> {{ $incidente->tipo_incidente->tipo_incidente ?? 'N/A' }}</p>
    <p><strong>Usuario:</strong> {{ $incidente->usuario->p_nombre ?? 'N/A' }} {{ $incidente->usuario->p_apellido ?? 'N/A' }}</p>

    <a href="{{ route('incidentes.index') }}" class="btn btn-primary">Volver</a>
</div>
@endsection
