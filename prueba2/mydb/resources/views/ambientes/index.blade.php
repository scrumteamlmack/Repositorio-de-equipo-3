@extends('instructor.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/ambientes.css') }}">
</head>
<div class="container">
    <h1>Ambientes registrados</h1>

    <a href="{{ route('ambientes.create') }}" class="btn-solicitar">➕ Crear nuevo ambiente</a>

    <!-- 🔎 Filtros multicriterio -->
    <div style="margin-bottom: 18px; display: flex; flex-wrap: wrap; gap: 12px;">
        <input type="text" id="filtro_numero" class="filter-input" placeholder="Filtrar por Número">
        <input type="text" id="filtro_capacidad" class="filter-input" placeholder="Filtrar por Capacidad">
        <input type="text" id="filtro_tipo" class="filter-input" placeholder="Filtrar por Tipo">
        <input type="text" id="filtro_estado" class="filter-input" placeholder="Filtrar por Estado">
    </div>

    <table id="tablaAmbientes">
        <thead>
            <tr>
                <th>ID</th>
                <th>Número de Ambiente</th>
                <th>Capacidad</th>
                <th>Tipo de Ambiente</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            @foreach ($ambientes as $ambiente)
                <tr>
                    <td>{{ $ambiente->id_ambiente }}</td>
                    <td>{{ $ambiente->num_ambiente }}</td>
                    <td>{{ $ambiente->capacidad }}</td>
                    <td>{{ $ambiente->tipo_ambiente }}</td>
                    <td>{{ $ambiente->estado }}</td>
                    <td>
                        <a href="{{ route('ambientes.edit', $ambiente->id_ambiente) }}" class="btn-editar">Editar</a>
                        <form action="{{ route('ambientes.destroy', $ambiente->id_ambiente) }}" method="POST" style="display:inline;">
                            @csrf
                            @method('DELETE')
                            <button class="btn-eliminar" onclick="return confirm('¿Seguro que deseas eliminar este ambiente?')">Eliminar</button>
                        </form>
                    </td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>

<!-- Scripts DataTables -->
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.3.6/css/buttons.dataTables.min.css">
<script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>

<script>
$(document).ready(function() {
    var table = $('#tablaAmbientes').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF' }
        ]
    });

    // Filtros multicriterio
    $('#filtro_numero').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_capacidad').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_tipo').on('keyup', function() { table.column(3).search(this.value).draw(); });
    $('#filtro_estado').on('keyup', function() { table.column(4).search(this.value).draw(); });
});
</script>
@endsection

@section('css')
<link rel="stylesheet" href="{{ asset('css/ambientes.css') }}">
@endsection
