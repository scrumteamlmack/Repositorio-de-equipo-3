<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Registrar Usuario - L-MACK</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="../css/registrar_usuario.css" />
    <link rel="shortcut icon" href="../resources/fondo oscuro N L-MACK.png" type="image/x-icon">
    <style>
        select {
            width: 100%;
            padding: 14px 12px;
            margin-bottom: 24px;
            border: 1.5px solid #d1d5db;
            border-radius: 8px;
            font-size: 1rem;
            background-color: white;
            transition: border-color 0.3s ease;
        }
    </style>
</head>

<body>

    <header>
        <div class="logo">
            <img src="../resources/fondo oscuro N L-MACK.png" alt="Logo L-MACK" class="img">
        </div>
        <nav>
            <a href="../index.html">Inicio</a>
            <a href="../html/login.html">Cerrar Sesion</a>
        </nav>
    </header>

    <main>
        <h2>Registrar Usuario</h2>
        <form action="/../layout/controlador/UserControlador.php" method="POST">
            <label for="Documento">Tipo de documento:</label>
            <select name="tipo_documento" required>
                <option value="">Seleccione tipo de documento</option>
                <option value="CC">Cédula de ciudadanía</option>
                <option value="TI">Tarjeta de identidad</option>
                <option value="CE">Cédula de extranjería</option>
            </select><br>
            <label for="Numero">Número de documento:</label>
            <input type="text" name="num_documento" placeholder="Número de documento" required><br>
            <label for="PrimerNombre">Primer Nombre:</label>
            <input type="text" name="p_nombre" placeholder="Primer nombre" required><br>
            <label for="SegundoNombre">Segundo Nombre:</label>
            <input type="text" name="s_nombre" placeholder="Segundo nombre"><br>
            <label for="PrimerApellido">Primer Apellido:</label>
            <input type="text" name="p_apellido" placeholder="Primer apellido" required><br>
            <label for="SegundoApellido">Segundo Apellido:</label>
            <input type="text" name="s_apellido" placeholder="Segundo apellido"><br>
            <label for="email">Correo electronico:</label>
            <input type="email" name="correo" placeholder="Correo institucional" required><br>
            <label for="Psw">Contraseña</label>
            <input type="password" name="contrasena" placeholder="Contraseña" required><br>
            <button type="submit" name="btnRegistrar" class="btn">Registrar</button>
        </form>
        
    </main>

    <footer>
        &copy; 2025 L-MACK - Registro de Usuarios
    </footer>

</body>

</html>