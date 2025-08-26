<?php
require_once "../modelo/UserDAO.php";
require_once "../modelo/UserDTO.php";

$userDAO = new UserDAO();

// ✅ Obtener usuario por ID
if (isset($_GET['id'])) {
    $id = intval($_GET['id']);
    $usuarios = $userDAO->listarUsuarios();

    // Buscar el usuario que coincide con el ID
    $usuario = null;
    foreach ($usuarios as $u) {
        if ($u['id_usuario'] == $id) {
            $usuario = $u;
            break;
        }
    }

    if (!$usuario) {
        die("Usuario no encontrado");
    }
} else {
    die("ID no proporcionado");
}

// ✅ Procesar actualización
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $userDTO = new UserDTO();
    $userDTO->setId($_POST['id']);
    $userDTO->setP_Nombre($_POST['p_nombre']);
    $userDTO->setS_Nombre($_POST['s_nombre']);
    $userDTO->setP_Apellido($_POST['p_apellido']);
    $userDTO->setS_Apellido($_POST['s_apellido']);
    $userDTO->setDocumento($_POST['num_documento']);
    $userDTO->setTipoDocumento($_POST['tipo_documento']);
    $userDTO->setCorreo($_POST['correo']);

    if ($userDAO->actualizarUsuario($userDTO)) {

        header("Location: consultar_usuarios.php");
        exit;
    } else {
        echo "Error al actualizar el usuario.";
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Consulta de Usuarios</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="css/editar_usuario.css" />
  <link rel="shortcut icon" href="resources/fondo oscuro N L-MACK.png" type="image/x-icon">
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
      <img src="resources/fondo oscuro N L-MACK.png" alt="Logo L-MACK" />
    </div>
    <nav>
      <a href="index.html">Inicio</a>
    </nav>
  </header>
  
  <main>
    <h2>Editar Usuario</h2>
  <form method="POST">
      <input type="hidden" name="id" value="<?= htmlspecialchars($usuario['id_usuario']) ?>">

      <label>Primer Nombre:</label>
      <input type="text" name="p_nombre" value="<?= htmlspecialchars($usuario['p_nombre']) ?>" required><br>

      <label>Segundo Nombre:</label>
      <input type="text" name="s_nombre" value="<?= htmlspecialchars($usuario['s_nombre']) ?>"><br>

      <label>Primer Apellido:</label>
      <input type="text" name="p_apellido" value="<?= htmlspecialchars($usuario['p_apellido']) ?>" required><br>

      <label>Segundo Apellido:</label>
      <input type="text" name="s_apellido" value="<?= htmlspecialchars($usuario['s_apellido']) ?>"><br>

      <label>Documento:</label>
      <input type="text" name="num_documento" value="<?= htmlspecialchars($usuario['num_documento']) ?>" required><br>

      <label>Tipo Documento:</label>
      <input type="text" name="tipo_documento" value="<?= htmlspecialchars($usuario['tipo_documento']) ?>" required><br>

      <label>Correo:</label>
      <input type="email" name="correo" value="<?= htmlspecialchars($usuario['correo']) ?>" required><br>

      <button type="submit" class="btn">Actualizar</button>
  </form>
  </main>
</body>
</html>
