@extends('layouts.admin')

@section('content')
<head>
  <link rel="stylesheet" href="{{ asset('css/minuta_create.css') }}">
</head>
<div class="form-wrapper">
  <div class="form-container">
    <div class="container">
      <h1>Crear Minuta</h1>

      @if($errors->any())
        <div class="error-box">
          <ul class="mb-0">
            @foreach($errors->all() as $error)
              <li>{{ $error }}</li>
            @endforeach
          </ul>
        </div>
      @endif

      <form action="{{ route('minutas.store') }}" method="POST">
        @csrf

        <div class="mb-3">
          <label>Fecha Recibo</label>
          <input type="datetime-local" name="fecha_hora_recibo" class="form-control" required value="{{ old('fecha_hora_recibo') ? \Carbon\Carbon::parse(old('fecha_hora_recibo'))->format('Y-m-d\\TH:i') : '' }}">
        </div>

        <div class="mb-3">
          <label>Fecha Entrega</label>
          <input type="datetime-local" name="fecha_hora_entrega" class="form-control" required value="{{ old('fecha_hora_entrega') ? \Carbon\Carbon::parse(old('fecha_hora_entrega'))->format('Y-m-d\\TH:i') : '' }}">
        </div>

        <div class="mb-3">
          <label>Novedad</label>
          <input type="text" name="novedad" class="form-control" value="{{ old('novedad') }}">
        </div>

        <div class="mb-3">
          <label>Descripción</label>
          <textarea name="descripcion_min" class="form-control">{{ old('descripcion_min') }}</textarea>
        </div>

        <div class="mb-3">
          <label>Estado</label>
          <select name="estado" class="form-control" required>
            <option value="Disponible" {{ old('estado') == 'Disponible' ? 'selected' : '' }}>Disponible</option>
            <option value="Ocupado" {{ old('estado') == 'Ocupado' ? 'selected' : '' }}>Ocupado</option>
          </select>
        </div>

        <div class="mb-3">
          <label>Ambiente</label>
          <select name="ambiente_id" class="form-control" required>
            <option value="">-- Seleccione --</option>
            @foreach($ambientes as $amb)
              <option value="{{ $amb->id_minuta ?? $amb->id_ambiente ?? $amb->id }}" {{ old('ambiente_id') == ($amb->id_minuta ?? $amb->id_ambiente ?? $amb->id) ? 'selected' : '' }}>
                {{ $amb->num_ambiente ?? $amb->nombre ?? ('Ambiente ' . $amb->id) }}
              </option>
            @endforeach
          </select>
        </div>

        <div class="mb-3">
          <label>Guarda de Seguridad</label>
          <select name="guarda_seguridad_Usuario_id_usuario" class="form-control" required>
            <option value="">-- Seleccione --</option>
            @foreach($guardas as $g)
              <option value="{{ $g->Usuario_id_usuario ?? $g->id }}" {{ old('guarda_seguridad_Usuario_id_usuario') == ($g->Usuario_id_usuario ?? $g->id) ? 'selected' : '' }}>
                {{ $g->Usuario_id_usuario ?? 'Guarda ' . $g->id }} {{ $g->usuario->p_nombre ?? 'Guarda ' . $g->id }} {{ $g->usuario->p_apellido ?? 'Guarda ' . $g->id }}
              </option>
            @endforeach
          </select>
        </div>

        <div class="mb-3">
          <label>Responsable</label>
          <select name="responsable_id" class="form-control" required>
            <option value="">-- Seleccione --</option>
            @foreach($responsables as $r)
              <option value="{{ $r->id ?? $r->id_instructor ?? $r->Usuario_id_usuario }}" {{ old('responsable_id') == ($r->id ?? $r->id_instructor ?? $r->Usuario_id_usuario) ? 'selected' : '' }}>
                {{ $r->nombre ?? ($r->usuario->id_usuario ?? '') }} {{ $r->nombre ?? ($r->usuario->p_nombre ?? '') }} {{ $r->nombre ?? ($r->usuario->p_apellido ?? '') }}
              </option>
            @endforeach
          </select>
        </div>

        <div style="display:flex;gap:10px;justify-content:flex-end;margin-top:16px;">
          <button type="submit" class="btn-cta">Guardar</button>
          <a href="{{ route('minutas.index') }}" class="btn-cancel">Cancelar</a>
        </div>

      </form>
    </div>
  </div>
</div>

@endsection
