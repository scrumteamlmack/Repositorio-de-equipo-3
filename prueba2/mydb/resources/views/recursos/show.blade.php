@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/rec_show.css') }}">
</head>

<div class="container">
    <h1>Detalle del Recurso</h1>

    <p><strong>ID:</strong> {{ $recurso->id_recurso }}</p>
    <p><strong>Serial:</strong> {{ $recurso->serial_recurso }}</p>
    <p><strong>Número:</strong> {{ $recurso->num_recurso }}</p>
    <p><strong>Nombre:</strong> {{ $recurso->nombre_recurso }}</p>
    <p><strong>Tipo:</strong> {{ $recurso->tipoRecurso->recurso_tipo ?? 'N/A' }}</p>
    <p><strong>Estado:</strong> {{ $recurso->estado }}</p>
    <p><strong>Observación:</strong> {{ $recurso->observacion }}</p>
    <p><strong>Ambiente:</strong> {{ $recurso->ambiente->num_ambiente ?? 'N/A' }}</p>

    <div class="actions">
        <a href="{{ route('recursos.index') }}">⬅️ Volver</a>
        <a href="{{ route('recursos.edit', $recurso->id_recurso) }}">✏️ Editar</a>
    </div>
</div>
@endsection
