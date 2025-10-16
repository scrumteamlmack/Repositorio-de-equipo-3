@extends('instructor.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/fichas.css') }}">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.3.6/css/buttons.dataTables.min.css">
</head>

<div class="container">
    <h2>Gestión de Fichas</h2>
    <a href="{{ route('fichas.create') }}" class="btn btn-primary">Nueva Ficha</a>
    <br><br>

    <!-- Filtros multicriterio -->
    <div style="margin-bottom: 15px; display: flex; flex-wrap: wrap; gap: 10px;">
        <input type="text" id="filtro_id" class="form-control" placeholder="Filtrar por ID">
        <input type="text" id="filtro_numero" class="form-control" placeholder="Filtrar por Número de Ficha">
        <input type="text" id="filtro_instructor" class="form-control" placeholder="Filtrar por Instructor">
        <input type="text" id="filtro_programa" class="form-control" placeholder="Filtrar por Programa">
        <input type="text" id="filtro_modalidad" class="form-control" placeholder="Filtrar por Modalidad">
    </div>

    <table id="tablaFichas" class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>ID</th>
                <th>Número Ficha</th>
                <th>Instructor</th>
                <th>Programa</th>
                <th>Modalidad</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            @foreach($fichas as $ficha)
            <tr>
                <td>{{ $ficha->idficha }}</td>
                <td>{{ $ficha->Num_ficha }}</td>
                <td>{{ $ficha->instructor ? $ficha->instructor->usuario->p_nombre : 'Sin asignar' }} {{ $ficha->instructor ? $ficha->instructor->usuario->p_apellido : 'Sin asignar' }}</td>
                <td>{{ $ficha->programa->nombre_programa ?? 'N/A' }}</td>
                <td>{{ $ficha->modalidad->nombre_modalidad }}</td>
                <td>
                    <a href="{{ route('fichas.edit', $ficha->idficha) }}" class="btn btn-warning btn-sm">Editar</a>
                    <form action="{{ route('fichas.destroy', $ficha->idficha) }}" method="POST" style="display:inline">
                        @csrf @method('DELETE')
                        <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                    </form>
                </td>
            </tr>
            @endforeach
        </tbody>
    </table>
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
    var table = $('#tablaFichas').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF' }
        ]
    });

    // Filtros multicriterio
    $('#filtro_id').on('keyup', function() { table.column(0).search(this.value).draw(); });
    $('#filtro_numero').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_instructor').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_programa').on('keyup', function() { table.column(3).search(this.value).draw(); });
    $('#filtro_modalidad').on('keyup', function() { table.column(4).search(this.value).draw(); });
});
</script>
@endsection
