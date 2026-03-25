@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/traslado_index.css') }}">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.3.6/css/buttons.dataTables.min.css">
</head>

<div class="container">
    <h2 class="mb-4">Gestión de Traslados de Recursos</h2>

    <!-- Botón Crear Traslado -->
    <a href="{{ route('traslados.create') }}" class="btn btn-success mb-3">Registrar Traslado</a>

    @if(session('success'))
        <div class="alert alert-success">{{ session('success') }}</div>
    @endif

    <!-- Filtros -->
    <div class="mb-3">
        <input type="text" id="id" placeholder="Buscar por ID">
        <input type="text" id="filtro_recurso" placeholder="Buscar Recurso">
        <input type="text" id="filtro_origen" placeholder="Buscar Ambiente Origen">
        <input type="text" id="filtro_destino" placeholder="Buscar Ambiente Destino">
        <input type="text" id="filtro_fecha" placeholder="Buscar Fecha Traslado">
        <input type="text" id="filtro_observacion" placeholder="Buscar Observación">
    </div>

    <table id="tablaTraslados" class="table table-bordered table-striped">
        <thead>
            <tr>
                <th>ID Traslado</th>
                <th>Recurso</th>
                <th>Ambiente Origen</th>
                <th>Ambiente Destino</th>
                <th>Fecha Traslado</th>
                <th>Observación</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            @foreach($traslados as $traslado)
                <tr>
                    <td>{{ $traslado->id_traslado }}</td>
                    <td>{{ $traslado->recurso->serial_recurso?? 'Sin recurso asignado' }}</td>
                    <td>{{ $traslado->ambienteOrigen->num_ambiente ?? 'N/A' }}</td>
                    <td>{{ $traslado->ambienteDestino->num_ambiente ?? 'N/A' }}</td>
                    <td>{{ $traslado->fecha_traslado }}</td>
                    <td>{{ $traslado->observacion }}</td>
                    <td>
                        <a href="{{ route('traslados.edit', $traslado->id_traslado) }}" class="btn btn-warning btn-sm">Editar</a>
                        <form action="{{ route('traslados.destroy', $traslado->id_traslado) }}" method="POST" style="display:inline-block;">
                            @csrf
                            @method('DELETE')
                            <button class="btn btn-danger btn-sm" onclick="return confirm('¿Eliminar este traslado?')">Eliminar</button>
                        </form>
                    </td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>

<!-- Scripts DataTables -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>

<script>
$(document).ready(function() {
    var table = $('#tablaTraslados').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF', orientation: 'landscape', pageSize: 'A4' }
        ]
    });

    // Filtros multicriterio
    $('#id').on('keyup', function() { table.column(0).search(this.value).draw(); });
    $('#filtro_recurso').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_origen').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_destino').on('keyup', function() { table.column(3).search(this.value).draw(); });
    $('#filtro_fecha').on('keyup', function() { table.column(4).search(this.value).draw(); });
    $('#filtro_observacion').on('keyup', function() { table.column(5).search(this.value).draw(); });
});
</script>
@endsection
