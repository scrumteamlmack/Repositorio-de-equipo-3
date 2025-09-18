@extends('layouts.app')

@section('content')
<div style="max-width:700px;margin:32px auto;padding:0 16px;">
  <h2 style="text-align:center;color:#10b981;margin-bottom:16px;">Editar Registro</h2>

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

    {{-- Aprendiz (editable) --}}
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Aprendiz</label>
      <input type="text" name="aprendiz_nombre" 
             value="{{ $registro->aprendiz->usuario->p_nombre }} {{ $registro->aprendiz->usuario->p_apellido }}" 
             style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
    </div>

    {{-- Fecha --}}
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Fecha</label>
      <input type="date" name="fecha_inasistencia" required 
             value="{{ \Carbon\Carbon::parse($registro->fecha_inasistencia)->format('Y-m-d') }}" 
             style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
    </div>

    {{-- Estado --}}
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Estado</label>
      <select name="estado" required style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
        <option value="Presente" {{ $registro->estado == 'Presente' ? 'selected' : '' }}>Presente</option>
        <option value="Ausente" {{ $registro->estado == 'Ausente' ? 'selected' : '' }}>Ausente</option>
      </select>
    </div>

    {{-- Instructor (editable) --}}
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Instructor</label>
      <input type="text" name="instructor_nombre" 
             value="{{ $registro->instructor->usuario->p_nombre }} {{ $registro->instructor->usuario->p_apellido }}" 
             style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
    </div>

    {{-- Jornada (editable) --}}
    <div style="margin-bottom:14px;">
      <label style="font-weight:600;display:block;margin-bottom:6px;">Jornada</label>
      <input type="text" name="jornada_nombre" 
             value="{{ $registro->jornada->nombre_jornada }}" 
             style="width:100%;padding:10px;border-radius:8px;border:1px solid #d1d5db;">
    </div>


    {{-- Botones --}}
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
