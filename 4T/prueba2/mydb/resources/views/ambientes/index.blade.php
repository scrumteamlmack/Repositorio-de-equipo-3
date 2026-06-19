<!DOCTYPE html>
<html>
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
</body>
</html>
