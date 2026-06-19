<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
    <h2>Panel de Administrador</h2>

    <p>Bienvenido {{ Auth::user()->p_nombre }} {{ Auth::user()->p_apellido }}</p>


    <form method="POST" action="{{ route('logout') }}">
        @csrf
        <button type="submit">Cerrar sesión</button>
    </form>
</body>
</html>
