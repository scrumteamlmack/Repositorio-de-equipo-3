@extends('instructor.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/incidentes.css') }}">

    {{-- Librerías DataTables --}}
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>

    {{-- Librerías exportación --}}
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.3.6/css/buttons.dataTables.min.css">
    <script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>
</head>

<div class="incidente-container">
    <h2>📋 Lista de Incidentes</h2>

    <a href="{{ route('incidentes.create') }}" class="btn-registrar">➕ Nuevo Incidente</a>

    @if(session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif

    <div class="tabla-container">
        <div class="filtros">
            <input type="text" id="filtro_id" placeholder="Buscar ID">
            <input type="text" id="filtro_descripcion" placeholder="Buscar Descripción">
            <input type="text" id="filtro_fecha" placeholder="Buscar Fecha">
            <input type="text" id="filtro_hora" placeholder="Buscar Hora">
            <input type="text" id="filtro_ambiente" placeholder="Buscar Ambiente">
            <input type="text" id="filtro_tipo" placeholder="Buscar Tipo">
            <input type="text" id="filtro_instructor" placeholder="Buscar Instructor">
        </div>

        <table id="tablaIncidentes" class="tabla-incidentes">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Descripción</th>
                    <th>Fecha</th>
                    <th>Hora</th>
                    <th>Ambiente</th>
                    <th>Tipo</th>
                    <th>Instructor</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                @forelse($incidentes as $incidente)
                    <tr>
                        <td>{{ $incidente->id_incidente }}</td>
                        <td>{{ $incidente->descripcion }}</td>
                        <td>{{ $incidente->fecha_incidente->format('Y-m-d') }}</td>
                        <td>{{ $incidente->hora_incidente->format('H:i') }}</td>
                        <td>{{ $incidente->ambiente->num_ambiente ?? 'N/A' }}</td>
                        <td>{{ $incidente->tipo_incidente->tipo_incidente ?? 'N/A' }}</td>
                        <td>{{ $incidente->usuario->p_nombre ?? 'N/A' }} {{ $incidente->usuario->p_apellido ?? '' }}</td>
                        <td class="acciones">
                            <a href="{{ route('incidentes.show', $incidente) }}" class="btn-show">👁 Ver</a>
                            <a href="{{ route('incidentes.edit', $incidente) }}" class="btn-warning">✏️ Editar</a>
                            <form action="{{ route('incidentes.destroy', $incidente) }}" method="POST" class="inline-form">
                                @csrf
                                @method('DELETE')
                                <button type="submit" class="btn-danger" onclick="return confirm('¿Eliminar este incidente?')">🗑 Eliminar</button>
                            </form>
                        </td>
                    </tr>
                @empty
                    <tr><td colspan="8">⚠️ No hay incidentes registrados.</td></tr>
                @endforelse
            </tbody>
        </table>
    </div>
</div>

<script>
$(document).ready(function() {
    var table = $('#tablaIncidentes').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF', orientation: 'landscape', pageSize: 'A4' }
        ]
    });

    $('#filtro_id').on('keyup', function() { table.column(0).search(this.value).draw(); });
    $('#filtro_descripcion').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_fecha').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_hora').on('keyup', function() { table.column(3).search(this.value).draw(); });
    $('#filtro_ambiente').on('keyup', function() { table.column(4).search(this.value).draw(); });
    $('#filtro_tipo').on('keyup', function() { table.column(5).search(this.value).draw(); });
    $('#filtro_instructor').on('keyup', function() { table.column(6).search(this.value).draw(); });
});
</script>
@endsection
