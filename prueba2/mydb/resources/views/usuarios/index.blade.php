@extends('layouts.admin')

@section('content')
<head>
    <!-- Estilos propios -->
    <link rel="stylesheet" href="{{ asset('css/usuarios.css') }}">
</head>
<main class="usuario-container">
    <h2>Lista de Usuarios</h2>

    <!-- Botón Registrar -->
    <a href="{{ route('usuarios.create') }}" class="btn-registrar">Registrar Usuario</a>

    <!-- Mensaje de éxito -->
    @if(session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif

    <!-- 🔎 Filtros multicriterio -->
    <div style="margin-bottom: 18px; display: flex; flex-wrap: wrap; gap: 12px;">
        <input type="text" id="filtro_id" class="filter-input" placeholder="Filtrar por ID">
        <input type="text" id="filtro_nombre" class="filter-input" placeholder="Filtrar por Nombre">
        <input type="text" id="filtro_apellido" class="filter-input" placeholder="Filtrar por Apellido">
        <input type="text" id="filtro_documento" class="filter-input" placeholder="Filtrar por Documento">
        <input type="text" id="filtro_correo" class="filter-input" placeholder="Filtrar por Correo">
    </div>

    <!-- Tabla de usuarios -->
    <div class="tabla-container">
        <table id="tablaUsuarios" class="tabla-usuarios">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombres</th>
                    <th>Apellidos</th>
                    <th>Documento</th>
                    <th>Correo</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            @foreach($usuarios as $usuario)
                <tr>
                    <td>{{ $usuario->id_usuario }}</td>
                    <td>{{ $usuario->p_nombre }} {{ $usuario->s_nombre }}</td>
                    <td>{{ $usuario->p_apellido }} {{ $usuario->s_apellido }}</td>
                    <td>{{ $usuario->tipo_documento }} {{ $usuario->num_documento }}</td>
                    <td>{{ $usuario->correo }}</td>
                    <td class="acciones">
                        <a href="{{ route('usuarios.edit', $usuario->id_usuario) }}" class="btn-warning">Editar</a>
                        <form action="{{ route('usuarios.destroy', $usuario->id_usuario) }}" method="POST" class="inline-form">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn-danger" onclick="return confirm('¿Seguro que deseas eliminar este usuario?')">
                                Eliminar
                            </button>
                        </form>
                    </td>
                </tr>
            @endforeach
            </tbody>
        </table>
    </div>
</main>

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
    var table = $('#tablaUsuarios').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json' },
        dom: 'Bfrtip', 
        buttons: [
            { extend: 'excelHtml5', text: '📊 Exportar Excel' },
            { extend: 'pdfHtml5', text: '📄 Exportar PDF' }

        ]
    });

    // 🔎 Filtros multicriterio
    $('#filtro_id').on('keyup', function() { table.column(0).search(this.value).draw(); });
    $('#filtro_nombre').on('keyup', function() { table.column(1).search(this.value).draw(); });
    $('#filtro_apellido').on('keyup', function() { table.column(2).search(this.value).draw(); });
    $('#filtro_documento').on('keyup', function() { table.column(3).search(this.value).draw(); });
    $('#filtro_correo').on('keyup', function() { table.column(4).search(this.value).draw(); });
});
</script>
@endsection