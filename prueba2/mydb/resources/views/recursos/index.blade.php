@extends('layouts.admin')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/recursos.css') }}">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.3.6/css/buttons.dataTables.min.css">
</head>

<div class="container">
    <h1 class="titulo-lista">💻 Lista de Recursos</h1>
    <a href="{{ route('recursos.create') }}" class="btn-nuevo">➕ Nuevo Recurso</a>

    <!-- 🔎 Filtros -->
    <div class="mb-3">
        <input type="text" id="filtro_serial" placeholder="Buscar Serial">
        <input type="text" id="filtro_numero" placeholder="Buscar Número">
        <input type="text" id="filtro_nombre" placeholder="Buscar Nombre">
        <input type="text" id="filtro_tipo" placeholder="Buscar Tipo">
        <input type="text" id="filtro_estado" placeholder="Buscar Estado">
        <input type="text" id="filtro_observacion" placeholder="Buscar Observación">
        <input type="text" id="filtro_ambiente" placeholder="Buscar Ambiente">
    </div>

    <div class="table-container">
        <table id="tablaRecursos" class="tabla-estilizada">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Serial</th>
                    <th>Número</th>
                    <th>Nombre</th>
                    <th>Tipo</th>
                    <th>Estado</th>
                    <th>Observación</th>
                    <th>Ambiente</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                @foreach($recursos as $recurso)
                <tr>
                    <td>{{ $recurso->id_recurso }}</td>
                    <td>{{ $recurso->serial_recurso }}</td>
                    <td>{{ $recurso->num_recurso }}</td>
                    <td>{{ $recurso->nombre_recurso }}</td>
                    <td>{{ $recurso->tipoRecurso->recurso_tipo ?? 'N/A' }}</td>
                    <td>
                        @if($recurso->estado == 'Disponible')
                            <span class="badge badge-success">Disponible</span>
                        @elseif($recurso->estado == 'Dañado')
                            <span class="badge badge-danger">Dañado</span>
                        @elseif($recurso->estado == 'En mantenimiento')
                            <span class="badge badge-warning">En mantenimiento</span>
                        @else
                            <span class="badge badge-secondary">{{ $recurso->estado }}</span>
                        @endif
                    </td>
                    <td>{{ $recurso->observacion }}</td>
                    <td>{{ $recurso->ambiente->num_ambiente ?? 'N/A' }}</td>
                    <td class="acciones">
                        <a href="{{ route('recursos.show', $recurso->id_recurso) }}" class="btn btn-ver">👁 Ver</a>
                        <a href="{{ route('recursos.edit', $recurso->id_recurso) }}" class="btn btn-editar">✏ Editar</a>
                        <form action="{{ route('recursos.destroy', $recurso->id_recurso) }}" method="POST" style="display:inline;">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-eliminar" onclick="return confirm('¿Seguro que deseas eliminar este recurso?')">🗑 Eliminar</button>
                        </form>
                    </td>
                </tr>
                @endforeach
            </tbody>
        </table>
    </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>

<script>
$(document).ready(function() {
    var table = $('#tablaRecursos').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF', orientation: 'landscape', pageSize: 'A4' }
        ]
    });

    // 🔎 Filtros multicriterio
    $('#filtro_serial').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_numero').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_nombre').on('keyup', function() { table.column(3).search(this.value).draw(); });
    $('#filtro_tipo').on('keyup', function() { table.column(4).search(this.value).draw(); });
    $('#filtro_estado').on('keyup', function() { table.column(5).search(this.value).draw(); });
    $('#filtro_observacion').on('keyup', function() { table.column(6).search(this.value).draw(); });
    $('#filtro_ambiente').on('keyup', function() { table.column(7).search(this.value).draw(); });
});
</script>
@endsection

