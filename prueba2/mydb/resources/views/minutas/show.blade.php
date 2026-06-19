@extends('layouts.admin')

@section('content')
<head>
  <link rel="stylesheet" href="{{ asset('css/minuta_show.css') }}">
</head>

<div class="detail-wrapper">
  <div class="detail-container">
    <h1>Detalle de la Minuta</h1>

    <div class="mb-3">
        <h5><strong>Minuta #:</strong> {{ $minuta->id_minuta }}</h5>
        <p><strong>Novedad:</strong> {{ $minuta->novedad }}</p>
        <p><strong>Descripción:</strong> {{ $minuta->descripcion_min }}</p>
        <p><strong>Estado:</strong> 
            @if($minuta->estado === 'Disponible')
                <span class="badge bg-success">Disponible</span>
            @else
                <span class="badge bg-danger">Ocupado</span>
            @endif
        </p>
        <p><strong>Fecha Recibo:</strong> {{ $minuta->fecha_hora_recibo }}</p>
        <p><strong>Fecha Entrega:</strong> {{ $minuta->fecha_hora_entrega }}</p>
        <p><strong>ID Ambiente:</strong> {{ $minuta->ambiente_id }}</p>
        <p><strong>Guarda de Seguridad:</strong> 
            {{ $minuta->guarda_seguridad?->usuario?->p_nombre ?? '' }} 
            {{ $minuta->guarda_seguridad?->usuario?->p_apellido ?? '' }}
        </p>
        <p><strong>Responsable:</strong> 
            {{ $minuta->instructor?->usuario?->p_nombre ?? '' }} 
            {{ $minuta->instructor?->usuario?->p_apellido ?? '' }}
        </p>
    </div>

    <div class="d-flex justify-content-end gap-2 mt-3">
      <a href="{{ route('minutas.index') }}" class="btn btn-secondary">Volver</a>
      <a href="{{ route('minutas.edit', $minuta->id_minuta) }}" class="btn btn-success">Editar</a>
    </div>
  </div>
</div>
@endsection
