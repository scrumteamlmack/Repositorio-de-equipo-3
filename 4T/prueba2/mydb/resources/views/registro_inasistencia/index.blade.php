@extends('layouts.app')

@section('content')
<div style="max-width:1100px;margin:32px auto;padding:0 16px;">
  <h2 style="text-align:center;color:#10b981;margin-bottom:16px;">Registro de Inasistencias</h2>

  @if(session('success'))
    <div style="background:#ecfdf5;color:#065f46;padding:12px;border-radius:8px;margin-bottom:12px;">
      {{ session('success') }}
    </div>
  @endif

  <div style="text-align:center;margin-bottom:16px;">
    <a href="{{ route('registro_inasistencia.create') }}" style="display:inline-block;padding:12px 20px;background:#10b981;color:white;border-radius:10px;text-decoration:none;font-weight:600;">➕ Nuevo registro</a>
  </div>

  <table style="width:100%;border-collapse:collapse;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 8px 20px rgba(16,185,129,0.12);">
    <thead>
      <tr style="background:#10b981;color:#fff;">
        <th style="padding:12px;text-align:left;">Aprendiz</th>
        <th style="padding:12px;text-align:center;">Fecha</th>
        <th style="padding:12px;text-align:center;">Estado</th>
        <th style="padding:12px;text-align:left;">Instructor</th>
        <th style="padding:12px;text-align:center;">Jornada</th>

        <th style="padding:12px;text-align:center;">Acciones</th>
      </tr>
    </thead>
    <tbody>
      @foreach($registros as $r)
      <tr style="border-bottom:1px solid #e5e7eb;">
        <td style="padding:12px;">
          {{ $r->aprendiz->usuario->p_nombre ?? '—' }} {{ $r->aprendiz->usuario->p_apellido ?? '' }}
        </td>
        <td style="padding:12px;text-align:center;">
          {{ \Carbon\Carbon::parse($r->fecha_inasistencia)->format('Y-m-d') }}
        </td>
        <td style="padding:12px;text-align:center;
                   color:
                     {{ $r->estado_inasistencia == 'S' ? 'green' :
                        ($r->estado_inasistencia == 'R' ? '#f59e0b' : 'red') }};
                   font-weight:600;">
          @if($r->estado_inasistencia == 'S') Asistió
          @elseif($r->estado_inasistencia == 'R') Retardo
          @else No Asistió
          @endif
        </td>
         <td style="padding:12px;">
          {{ $r->Instructor->usuario->p_nombre ?? '—' }} {{ $r->Instructor->usuario->p_apellido ?? '' }}
        </td>

        <td style="padding:12px">
                    {{ $r->jornada->nombre_jornada?? '—' }}

        </td>
        <td style="padding:12px;text-align:center;">
          <a href="{{ route('registro_inasistencia.edit', $r->id_inasistencia) }}" style="margin-right:6px;padding:6px 10px;background:#f59e0b;color:#fff;border-radius:6px;text-decoration:none;font-weight:600;">✏️</a>

          <form action="{{ route('registro_inasistencia.destroy', $r->id_inasistencia) }}" method="POST" style="display:inline;">
            @csrf
            @method('DELETE')
            <button type="submit" style="padding:6px 10px;background:#ef4444;color:#fff;border:none;border-radius:6px;cursor:pointer;font-weight:600;">🗑️</button>
          </form>
        </td>
      </tr>
      @endforeach
    </tbody>
  </table>
</div>
@endsection
