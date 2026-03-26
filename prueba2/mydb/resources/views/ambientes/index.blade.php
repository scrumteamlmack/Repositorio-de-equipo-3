@extends('instructor.dashboard')
<<<<<<< HEAD
<!DOCTYPE html>
<html>
    @section('content')
<head>
    <title>Lista de Ambientes</title>
    <style>
        /* Estilo completo L-MACK con navegación incluida */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Poppins', sans-serif;
            background-color: #F5F5DC;
            color: #374151;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        header, footer {
            background-color: #10b981;
            color: white;
            padding: 16px 32px;
        }

        main {
            flex-grow: 1;
            padding: 32px 20px;
            max-width: 1100px;
            margin: 0 auto;
        }

        h1 {
            text-align: center;
            font-weight: 700;
            margin-bottom: 24px;
            color: #10b981;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 8px 20px rgba(16, 185, 129, 0.15);
        }

        th, td {
            padding: 14px;
            text-align: center;
            border-bottom: 1px solid #e5e7eb;
            font-size: 0.95rem;
        }

        th {
            background-color: #10b981;
            color: white;
            font-weight: 600;
        }

        tbody tr:hover {
            background-color: #f0fdf4;
        }

        /* Botones L-MACK */
        .btn-solicitar {
            display: inline-block;
            margin: 16px auto 32px;
            padding: 14px 28px;
            background-color: #10b981;
            color: white;
            text-decoration: none;
            border-radius: 12px;
            font-weight: 600;
            font-size: 1rem;
            text-align: center;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }

        .btn-solicitar:hover {
            background-color: #059669;
        }

        .btn-editar {
            display: inline-block;
            padding: 8px 16px;
            margin-right: 4px;
            background-color: #10b981;
            color: white;
            border-radius: 8px;
            font-weight: 600;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }

        .btn-editar:hover {
            background-color: #059669;
        }

        .btn-eliminar {
            display: inline-block;
            padding: 8px 16px;
            background-color: #10b981;
            color: white;
            border-radius: 8px;
            font-weight: 600;
            text-decoration: none;
            transition: background-color 0.3s ease;
            border: none;
            cursor: pointer;
        }

        .btn-eliminar:hover {
            background-color: #b91c1c;
        }

        footer {
            background-color: #10b981;
            color: white;
            text-align: center;
            padding: 16px;
            font-size: 0.9rem;
            user-select: none;
            margin-top: 40px;
        }
    </style>
</head>
<body>
    <main>
        <h1>Ambientes registrados</h1>

        <a href="{{ route('ambientes.create') }}" class="btn-solicitar">➕ Crear nuevo ambiente</a>

        <table>
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
    </main>
    @endsection
</body>
</html>
=======

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
>>>>>>> 6b4a9da6b570592154cd1b9ae2483bf24c9bd186
