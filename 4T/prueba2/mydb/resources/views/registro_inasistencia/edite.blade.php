@extends('layouts.app')

@section('content')
<div style="max-width:700px;margin:32px auto;padding:0 16px;">
  <h2 style="text-align:center;color:#10b981;margin-bottom:16px;">Editar Inasistencia</h2>

  @if ($errors->any())
    <div style="background:#fee2e2;color:#b91c1c;padding:12px;border-radius:8px;margin-bottom:12px;">
      <ul style="margin-left:18px;">
        @foreach ($errors->all() as $error)
          <li>{{ $error }}</li>
        @endforeach
      </ul>
    </div>
  @endif

  <form action="{{ route('registro_inasistencia.update', $registro->id_inasistencia) }}" method="POST"
        style="background:#fff;padding:24px;border-radius:12px;box-shadow:0 8px 20px rgba(16,185,129,0.12);">
    @csrf
    @method('PUT')

    <!-- Aprendiz -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Aprendiz</label>
      <select name="aprendiz_Usuario_id_usuario" required
              style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
        <option value="">Seleccione un aprendiz</option>
        @foreach ($aprendices as $aprendiz)
          @php
            $aprId = $aprendiz->Usuario_id_usuario
                     ?? ($aprendiz->usuario->id_usuario ?? null)
                     ?? ($aprendiz->id_aprendiz ?? $aprendiz->id ?? null);
            $selectedApr = old('aprendiz_Usuario_id_usuario', $registro->aprendiz_Usuario_id_usuario);
          @endphp
          @if($aprId)
            <option value="{{ $aprId }}" {{ $selectedApr == $aprId ? 'selected' : '' }}>
              {{ $aprendiz->usuario->p_nombre ?? $aprendiz->nombre ?? 'Sin nombre' }}
              {{ $aprendiz->usuario->p_apellido ?? '' }}
            </option>
          @endif
        @endforeach
      </select>
    </div>

    <!-- Fecha -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Fecha</label>
      <input type="date" name="fecha_inasistencia" required
             value="{{ old('fecha_inasistencia', \Carbon\Carbon::parse($registro->fecha_inasistencia)->format('Y-m-d')) }}"
             style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
    </div>

    <!-- Estado -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Estado</label>
      @php $selectedEstado = old('estado_inasistencia', $registro->estado_inasistencia); @endphp
      <select name="estado_inasistencia" required
              style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
        <option value="">Seleccione estado</option>
        <option value="R" {{ $selectedEstado === 'R' ? 'selected' : '' }}>Retardo</option>
        <option value="S" {{ $selectedEstado === 'S' ? 'selected' : '' }}>Sí</option>
        <option value="N" {{ $selectedEstado === 'N' ? 'selected' : '' }}>No</option>
      </select>
    </div>

    <!-- Instructor -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Instructor</label>
      <select name="instructor_Usuario_id_usuario" required
              style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
        <option value="">Seleccione un instructor</option>
        @foreach ($instructores as $instructor)
          @php
            $insId = $instructor->Usuario_id_usuario
                   ?? ($instructor->usuario->id_usuario ?? null)
                   ?? ($instructor->id ?? null);
            $selectedIns = old('instructor_Usuario_id_usuario', $registro->instructor_Usuario_id_usuario);
          @endphp
          @if($insId)
            <option value="{{ $insId }}" {{ $selectedIns == $insId ? 'selected' : '' }}>
              {{ $instructor->usuario->p_nombre ?? $instructor->nombre ?? 'Sin nombre' }}
              {{ $instructor->usuario->p_apellido ?? '' }}
            </option>
          @endif
        @endforeach
      </select>
    </div>

    <!-- Jornada -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Jornada</label>
      <select name="jornada_id" required
              style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
        <option value="">Seleccione una jornada</option>
        @foreach ($jornadas as $jornada)
          @php
            $jId = $jornada->id_jornada ?? $jornada->id ?? null;
            $selectedJ = old('jornada_id', $registro->jornada_id);
          @endphp
          @if($jId)
            <option value="{{ $jId }}" {{ $selectedJ == $jId ? 'selected' : '' }}>
              {{ $jornada->nombre_jornada ?? $jornada->nombre ?? 'Jornada '.$loop->iteration }}
            </option>
          @endif
        @endforeach
      </select>
    </div>

    <!-- Botones -->
    <div style="display:flex;gap:12px;">
      <button type="submit" style="background:#10b981;color:#fff;padding:10px 18px;border-radius:10px;border:none;font-weight:600;">
        Actualizar
      </button>
      <a href="{{ route('registro_inasistencia.index') }}"
         style="background:#6b7280;color:#fff;padding:10px 18px;border-radius:10px;text-decoration:none;display:inline-block;">
         Cancelar
      </a>
    </div>
  </form>
</div>
@endsection
