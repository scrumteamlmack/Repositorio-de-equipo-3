<?php
require_once '../controlador/UserControlador.php';

// Instanciamos el controlador y traemos los usuarios
$userDAO = new UserDAO();
$usuarios = $userDAO->listarUsuarios();
?>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Consulta de Usuarios</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="css/consultar_usuarios.css" />
  <link rel="shortcut icon" href="resources/fondo oscuro N L-MACK.png" type="image/x-icon">
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
    <h2>Lista de Usuarios Registrados</h2>

    <?php if (!empty($usuarios)): ?>
      <table border="1" cellpadding="8" cellspacing="0">
          <thead>
              <tr>
                  <th>ID</th>
                  <th>Primer Nombre</th>
                  <th>Segundo Nombre</th>
                  <th>Primer Apellido</th>
                  <th>Segundo Apellido</th>
                  <th>Documento</th>
                  <th>Tipo Documento</th>
                  <th>Correo</th>
                  <th>Actualizar</th>
                  <th>Eliminar</th>
              </tr>
          </thead>
          <tbody>
              <?php foreach ($usuarios as $user): ?>
                  <tr>
                      <td><?= htmlspecialchars($user['id_usuario']) ?></td>
                      <td><?= htmlspecialchars($user['p_nombre']) ?></td>
                      <td><?= htmlspecialchars($user['s_nombre']) ?></td>
                      <td><?= htmlspecialchars($user['p_apellido']) ?></td>
                      <td><?= htmlspecialchars($user['s_apellido']) ?></td>
                      <td><?= htmlspecialchars($user['num_documento']) ?></td>
                      <td><?= htmlspecialchars($user['tipo_documento']) ?></td>
                      <td><?= htmlspecialchars($user['correo']) ?></td>
                      <td>
        <a href="editar_usuario.php?id=<?= $user['id_usuario'] ?>"><img class="edit" src="resources/img_edit.png" alt="edit" ></a> </td>
                      <td> 
        <a href="eliminar_usuario.php?id=<?= $user['id_usuario'] ?>" onclick="return confirm('¿Seguro que deseas eliminar este usuario?')"><img class="eliminar" src="resources/img_eliminar.png" alt="delete" ></a>
      </td>
                  </tr>
              <?php endforeach; ?>
          </tbody>
      </table>
    <?php else: ?>
      <p>No hay usuarios registrados.</p>
    <?php endif; ?>

  </main>

  <footer>
    &copy; 2025 L-MACK
  </footer>
</body>
</html>
