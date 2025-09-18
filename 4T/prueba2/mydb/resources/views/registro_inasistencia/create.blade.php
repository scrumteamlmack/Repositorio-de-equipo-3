@extends('layouts.app')

@section('content')
<div style="max-width:700px;margin:32px auto;padding:0 16px;">
  <h2 style="text-align:center;color:#10b981;margin-bottom:16px;">Registrar Inasistencia</h2>

  @if ($errors->any())
    <div style="background:#fee2e2;color:#991b1b;padding:12px;border-radius:8px;margin-bottom:16px;">
      <ul>
        @foreach ($errors->all() as $error)
          <li>{{ $error }}</li>
        @endforeach
      </ul>
    </div>
  @endif

  <form action="{{ route('inasistencias.store') }}" method="POST" style="display:flex;flex-direction:column;gap:16px;">
    @csrf

    <!-- Fecha -->
    <div>
      <label for="fecha_inasistencia" style="display:block;margin-bottom:4px;">Fecha:</label>
      <input type="date" name="fecha_inasistencia" id="fecha_inasistencia" value="{{ old('fecha_inasistencia') }}" required style="width:100%;padding:8px;border:1px solid #d1d5db;border-radius:8px;">
    </div>

    <!-- Estado -->
    <div>
      <label for="estado_inasistencia" style="display:block;margin-bottom:4px;">Estado:</label>
      <select name="estado_inasistencia" id="estado_inasistencia" required style="width:100%;padding:8px;border:1px solid #d1d5db;border-radius:8px;">
        <option value="">Seleccione...</option>
        <option value="S" {{ old('estado_inasistencia') == 'S' ? 'selected' : '' }}>Sí</option>
        <option value="N" {{ old('estado_inasistencia') == 'N' ? 'selected' : '' }}>No</option>
        <option value="R" {{ old('estado_inasistencia') == 'R' ? 'selected' : '' }}>Retardo</option>
      </select>
    </div>

    <!-- Aprendiz -->
    <div>
      <label for="aprendiz_Usuario_id_usuario" style="display:block;margin-bottom:4px;">Aprendiz:</label>
      <select name="aprendiz_Usuario_id_usuario" id="aprendiz_Usuario_id_usuario" required style="width:100%;padding:8px;border:1px solid #d1d5db;border-radius:8px;">
        <option value="">Seleccione un aprendiz</option>
        @foreach ($aprendices as $aprendiz)
          <option value="{{ $aprendiz->Usuario_id_usuario }}" {{ old('aprendiz_Usuario_id_usuario') == $aprendiz->Usuario_id_usuario ? 'selected' : '' }}>
            {{ $aprendiz->usuario->p_nombre ?? 'Sin nombre' }}
          </option>
        @endforeach
      </select>
    </div>

    <!-- Jornada -->
    <div>
      <label for="jornada_id" style="display:block;margin-bottom:4px;">Jornada:</label>
      <select name="jornada_id" id="jornada_id" required style="width:100%;padding:8px;border:1px solid #d1d5db;border-radius:8px;">
        <option value="">Seleccione una jornada</option>
        @foreach ($jornadas as $jornada)
          <option value="{{ $jornada->id_jornada }}" {{ old('jornada_id') == $jornada->id_jornada ? 'selected' : '' }}>
            {{ $jornada->nombre_jornada }}
          </option>
        @endforeach
      </select>
    </div>

    <!-- Instructor -->
    <div>
      <label for="instructor_Usuario_id_usuario" style="display:block;margin-bottom:4px;">Instructor:</label>
      <select name="instructor_Usuario_id_usuario" id="instructor_Usuario_id_usuario" required style="width:100%;padding:8px;border:1px solid #d1d5db;border-radius:8px;">
        <option value="">Seleccione un instructor</option>
        @foreach ($instructores as $instructor)
          <option value="{{ $instructor->Usuario_id_usuario }}" {{ old('instructor_Usuario_id_usuario') == $instructor->Usuario_id_usuario ? 'selected' : '' }}>
            {{ $instructor->usuario->p_nombre ?? 'Sin nombre' }}
          </option>
        @endforeach
      </select>
    </div>

    <!-- Botón -->
    <div style="text-align:center;">
      <button type="submit" style="background:#10b981;color:white;padding:10px 20px;border:none;border-radius:8px;cursor:pointer;">
        Guardar
      </button>
    </div>
  </form>
</div>
@endsection
