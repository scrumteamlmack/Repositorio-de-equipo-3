@extends('instructor.dashboard')
@section('title', 'Asistencias')
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

  <!-- 🔎 Filtros multicriterio -->
  <div style="margin-bottom: 18px; display: flex; flex-wrap: wrap; gap: 12px;">
      <input type="text" id="filtro_aprendiz" class="filter-input" placeholder="Filtrar por Aprendiz">
      <input type="" id="filtro_fecha" class="filter-input" placeholder="Filtrar por Fecha">
      <input type="text" id="filtro_estado" class="filter-input" placeholder="Filtrar por Estado">
      <input type="text" id="filtro_instructor" class="filter-input" placeholder="Filtrar por Instructor">
      <input type="text" id="filtro_jornada" class="filter-input" placeholder="Filtrar por Jornada">
  </div>

  <table id="tablaAsistencia" style="width:100%;border-collapse:collapse;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 8px 20px rgba(16,185,129,0.12);">
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
        <td style="padding:12px">{{ $r->jornada->nombre_jornada?? '—' }}</td>
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

<!-- 🔎 jQuery + DataTables -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.3.6/css/buttons.dataTables.min.css">
<script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>
<script>

$(document).ready(function() {
    var table = $('#tablaAsistencia').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
         dom: 'Bfrtip', 
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF' }

        ]
    });

    // 🔎 Filtros multicriterio
    $('#filtro_aprendiz').on('keyup', function() { table.column(0).search(this.value).draw(); });
    $('#filtro_fecha').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_estado').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_instructor').on('keyup', function() { table.column(3).search(this.value).draw(); });
    $('#filtro_jornada').on('keyup', function() { table.column(4).search(this.value).draw(); });
});
</script>
@endsection
