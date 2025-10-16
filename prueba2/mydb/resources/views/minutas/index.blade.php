@extends('instructor.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/minuta.css') }}">
</head>
<div class="container">
    <h1>Listado de Minutas</h1>
    <a href="{{ route('minutas.create') }}" class="btn btn-success mb-3">Nueva Minuta</a>
<br><br>
    @if(session('success'))
        <div class="alert alert-success">{{ session('success') }}</div>
    @endif

    <!-- Filtros multicriterio -->
    <div style="margin-bottom: 18px; display: flex; flex-wrap: wrap; gap: 12px;">
        <input type="text" id="filtro_id" class="filter-input" placeholder="Filtrar por ID">
        <input type="text" id="filtro_guarda" class="filter-input" placeholder="Filtrar por Guarda">
        <input type="text" id="filtro_novedad" class="filter-input" placeholder="Filtrar por Novedad">
        <input type="text" id="filtro_estado" class="filter-input" placeholder="Filtrar por Estado">
    </div>

    <table id="tablaMinutas" class="table table-bordered table-striped">
        <thead>
            <tr>
                <th>ID_minuta</th>
                <th>Guarda</th>
                <th>Novedad</th>
                <th>Estado</th>
                <th>Ingreso</th>
                <th>Entrega</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        @foreach($minutas as $m)
            <tr>
                <td>{{ $m->id_minuta }}</td>
                <td>{{ $m->guarda_seguridad?->usuario->p_nombre ?? 'N/A' }} {{ $m->guarda_seguridad?->usuario->p_apellido ?? '' }}</td>
                <td>{{ $m->novedad }}</td>
                <td>
                    @if($m->estado === 'activo')
                        <span class="badge bg-success">Activo</span>
                    @else
                        <span class="badge bg-danger">Ocupado</span>
                    @endif
                </td>
                <td>{{ $m->fecha_hora_recibo }}</td>
                <td>{{ $m->fecha_hora_entrega }}</td>
                <td>
                    <div class="acciones">
                        <a href="{{ route('minutas.show',$m->id_minuta) }}" class="btn btn-info btn-sm">Ver</a>
                        <a href="{{ route('minutas.edit',$m->id_minuta) }}" class="btn btn-primary btn-sm">Editar</a>
                        <form action="{{ route('minutas.destroy',$m->id_minuta) }}" method="POST">
                            @csrf @method('DELETE')
                            <button class="btn btn-danger btn-sm" onclick="return confirm('¿Eliminar minuta?')">Eliminar</button>
                        </form>
                    </div>
                </td>
            </tr>
        @endforeach
        </tbody>
    </table>
</div>

<!-- 🔎 jQuery + DataTables + Buttons -->
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
    var table = $('#tablaMinutas').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
        paging: true,
        ordering: true,
        info: true,
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF' }
        ]
    });

    // Filtros multicriterio
    $('#filtro_id').on('keyup', function() { table.column(0).search(this.value).draw(); });
    $('#filtro_guarda').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_novedad').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_estado').on('keyup', function() { table.column(3).search(this.value).draw(); });
});
</script>
@endsection

@section('css')
<link rel="stylesheet" href="{{ asset('css/minutas.css') }}">
@endsection
