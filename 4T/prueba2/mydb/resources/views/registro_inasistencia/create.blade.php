@extends('layouts.app')

@section('content')
<div style="max-width:700px;margin:32px auto;padding:0 16px;">
  <h2 style="text-align:center;color:#10b981;margin-bottom:16px;">Crear Registro de Inasistencia</h2>

  @if ($errors->any())
    <div style="background:#fee2e2;color:#b91c1c;padding:12px;border-radius:8px;margin-bottom:12px;">
      <ul style="margin-left:18px;">
        @foreach ($errors->all() as $error) 
          <li>{{ $error }}</li> 
        @endforeach
      </ul>
    </div>
  @endif

  <form action="{{ route('registro_inasistencia.store') }}" method="POST" style="background:#fff;padding:24px;border-radius:12px;box-shadow:0 8px 20px rgba(16,185,129,0.12);">
    @csrf

    <!-- Select de aprendiz: value = id de la tabla usuario (id_usuario) -->
    <div class="form-group" style="margin-bottom:14px;">
      <label for="aprendiz_Usuario_id_usuario" style="font-weight:600;display:block;margin-bottom:6px;">Seleccione Aprendiz:</label>
      <select name="aprendiz_Usuario_id_usuario" id="aprendiz_Usuario_id_usuario" class="form-control" required style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
          <option value="">-- Seleccione --</option>
          @foreach($aprendices as $aprendiz)
              <option value="{{ $aprendiz->usuario->id_usuario ?? $aprendiz->usuario->id ?? $aprendiz->id }}"
                {{ old('aprendiz_Usuario_id_usuario') == ($aprendiz->usuario->id_usuario ?? $aprendiz->usuario->id ?? $aprendiz->id) ? 'selected' : '' }}>
                  {{ $aprendiz->usuario->p_nombre ?? '' }} {{ $aprendiz->usuario->p_apellido ?? '' }}
                  @if(!empty($aprendiz->usuario->documento)) - {{ $aprendiz->usuario->documento }} @endif
              </option>
          @endforeach
      </select>
      @error('aprendiz_Usuario_id_usuario') <div style="color:#b91c1c;margin-top:6px;">{{ $message }}</div> @enderror
    </div>

    <!-- Fecha -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Fecha</label>
      <input type="date" name="fecha_inasistencia" value="{{ old('fecha_inasistencia') }}" required style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
      @error('fecha_inasistencia') <div style="color:#b91c1c;margin-top:6px;">{{ $message }}</div> @enderror
    </div>

    <!-- Estado: usamos el nombre exacto que veo en tu BD -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Estado</label>
      <select name="estado_inasistencia" required style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
        <option value="">-- Seleccione --</option>
        <option value="S" {{ old('estado_inasistencia') == 'S' ? 'selected' : '' }}>Asistió</option>
        <option value="R" {{ old('estado_inasistencia') == 'R' ? 'selected' : '' }}>Retardo</option>
        <option value="N" {{ old('estado_inasistencia') == 'N' ? 'selected' : '' }}>No asistió</option>
      </select>
      @error('estado_inasistencia') <div style="color:#b91c1c;margin-top:6px;">{{ $message }}</div> @enderror
    </div>

    <!-- Jornada -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Jornada (ID)</label>
      <input type="number" name="jornada_id" value="{{ old('jornada_id') }}" required style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
      @error('jornada_id') <div style="color:#b91c1c;margin-top:6px;">{{ $message }}</div> @enderror
    </div>

    <!-- Instructor: enviamos id del usuario del instructor -->
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Instructor (ID usuario)</label>
      <input type="number" name="instructor_Usuario_id_usuario" value="{{ old('instructor_Usuario_id_usuario') }}" required style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
      @error('instructor_Usuario_id_usuario') <div style="color:#b91c1c;margin-top:6px;">{{ $message }}</div> @enderror
    </div>

  

    <div style="display:flex;gap:12px;">
      <button type="submit" style="background:#10b981;color:#fff;padding:10px 18px;border-radius:10px;border:none;font-weight:600;">Guardar</button>
      <a href="{{ route('registro_inasistencia.index') }}" style="background:#6b7280;color:#fff;padding:10px 18px;border-radius:10px;text-decoration:none;display:inline-block;">Cancelar</a>
    </div>
  </form>
</div>
@endsection
