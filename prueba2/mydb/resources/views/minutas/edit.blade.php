@extends('layouts.admin')

@section('content')
<head>
  <link rel="stylesheet" href="{{ asset('css/minuta_edit.css') }}">
</head>

<div class="edit-wrapper">
  <div class="edit-container">
    <h1>Editar Minuta</h1>

    {{-- Mostrar errores de validación --}}
    @if($errors->any())
      <div class="error-box">
        <ul style="margin:0;padding-left:18px;">
          @foreach($errors->all() as $error)
            <li>{{ $error }}</li>
          @endforeach
        </ul>
      </div>
    @endif

    <form action="{{ route('minutas.update', $minuta->id_minuta) }}" method="POST">
        @csrf
        @method('PUT')

        {{-- Fecha Recibo --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Fecha Recibo</label>
            <input type="datetime-local" name="fecha_hora_recibo"
                   value="{{ old('fecha_hora_recibo', $minuta->fecha_hora_recibo ? \Carbon\Carbon::parse($minuta->fecha_hora_recibo)->format('Y-m-d\TH:i') : '') }}"
                   class="form-control" required>
            @error('fecha_hora_recibo') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        {{-- Fecha Entrega --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Fecha Entrega</label>
            <input type="datetime-local" name="fecha_hora_entrega"
                   value="{{ old('fecha_hora_entrega', $minuta->fecha_hora_entrega ? \Carbon\Carbon::parse($minuta->fecha_hora_entrega)->format('Y-m-d\TH:i') : '') }}"
                   class="form-control" required>
            @error('fecha_hora_entrega') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        {{-- Novedad --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Novedad</label>
            <input type="text" name="novedad" value="{{ old('novedad', $minuta->novedad) }}" class="form-control">
            @error('novedad') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        {{-- Descripción --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Descripción</label>
            <textarea name="descripcion_min" class="form-control">{{ old('descripcion_min', $minuta->descripcion_min) }}</textarea>
            @error('descripcion_min') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        {{-- Estado --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Estado</label>
            <select name="estado" class="form-control" required>
                <option value="Disponible" {{ old('estado', $minuta->estado)=='Disponible' ? 'selected' : '' }}>Disponible</option>
                <option value="Ocupado" {{ old('estado', $minuta->estado)=='Ocupado' ? 'selected' : '' }}>Ocupado</option>
            </select>
            @error('estado') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        {{-- Ambiente --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Ambiente</label>
            <select name="ambiente_id" class="form-control" required>
                <option value="">-- Seleccione --</option>
                @foreach($ambientes ?? [] as $amb)
                    @php
                        $ambId = $amb->id_minuta ?? $amb->id_ambiente ?? $amb->id;
                    @endphp
                    <option value="{{ $ambId }}" {{ (string) old('ambiente_id', $minuta->ambiente_id) === (string)$ambId ? 'selected' : '' }}>
                        {{ $amb->num_ambiente ?? $amb->nombre ?? 'Ambiente '.$ambId }}
                    </option>
                @endforeach
            </select>
            @error('ambiente_id') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        {{-- Guarda de Seguridad --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Guarda de Seguridad</label>
            <select name="guarda_seguridad_Usuario_id_usuario" class="form-control" required>
                <option value="">-- Seleccione --</option>
                @foreach($guardas ?? [] as $g)
                    @php $gId = $g->Usuario_id_usuario ?? $g->id; @endphp
                    <option value="{{ $gId }}" {{ (string) old('guarda_seguridad_Usuario_id_usuario', $minuta->guarda_seguridad_Usuario_id_usuario) === (string)$gId ? 'selected' : '' }}>
                        {{ $g->Usuario_id_usuario ?? ($g->nombre ?? 'Guarda '.$gId) }} {{ $g->usuario->p_nombre ?? '' }} {{ $g->usuario->p_apellido ?? '' }}
                    </option>
                @endforeach
            </select>
            @error('guarda_seguridad_Usuario_id_usuario') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        {{-- Responsable --}}
        <div style="margin-bottom:16px;">
            <label style="font-weight:600;">Responsable</label>
            <select name="responsable_id" class="form-control" required>
                <option value="">-- Seleccione --</option>
                @foreach($responsables ?? [] as $r)
                    @php $rId = $r->id ?? $r->id_instructor ?? $r->Usuario_id_usuario; @endphp
                    <option value="{{ $rId }}" {{ (string) old('responsable_id', $minuta->responsable_id) === (string)$rId ? 'selected' : '' }}>
                        {{ $r->nombre ?? ($r->nombres ?? ''.$rId) }} {{ $r->usuario->p_nombre ?? '' }} {{ $r->usuario->p_apellido ?? '' }}
                    </option>
                @endforeach
            </select>
            @error('responsable_id') <small class="field-error">{{ $message }}</small> @enderror
        </div>

        <div style="display:flex;gap:10px;justify-content:flex-end;margin-top:24px;">
          <button type="submit" class="btn-cta">Actualizar</button>
          <a href="{{ route('minutas.index') }}" class="btn-cancel">Cancelar</a>
        </div>
    </form>
  </div>
</div>
@endsection
