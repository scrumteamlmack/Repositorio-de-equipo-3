<?php
require_once '../controlador/RecursosController.php';

// Instanciamos el controlador y traemos los usuarios
$recursosDAO = new recursosDAO();
$recursos = $recursosDAO->listarRecursos();
?>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Consulta de Usuarios</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="css/consultar_minutas.css" />
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
    <h2>Lista de Minutas Registradas</h2>

    <?php if (!empty($recursos)): ?>
      <table border="1" cellpadding="8" cellspacing="0">
          <thead>
              <tr>
                <th>Id recursos</th>
                  <th>Serial de recursos</th>
                  <th>Numero del recurso</th>
                  <th>Nombre del recurso</th>
                  <th>Tipo de recurso</th>
                  <th>Estado del recurso</th>
                  <th>Observacion</th>
                  <th>Id del ambiente</th>
                  <th>Actualizar</th>
                  <th>Eliminar</th>
              </tr>
          </thead>
          <tbody>
              <?php foreach ($recursos as $rec): ?>
                  <tr>
                      <td><?= htmlspecialchars($rec['id_recurso']) ?></td>
                      <td><?= htmlspecialchars($rec['serial_recurso']) ?></td>
                      <td><?= htmlspecialchars($rec['num_recurso']) ?></td>
                      <td><?= htmlspecialchars($rec['nombre_recurso']) ?></td>
                      <td><?= htmlspecialchars($rec['tipo_recurso']) ?></td>
                      <td><?= htmlspecialchars($rec['estado']) ?></td>
                      <td><?= htmlspecialchars($rec['observacion']) ?></td>
                      <td><?= htmlspecialchars($rec['ambiente_id']) ?></td>
                      <td>
        <a href="editar_recursos.php?id=<?= $rec['id_recurso'] ?>"><img class="edit" src="resources/img_edit.png" alt="edit" ></a> </td>
                      <td> 
        <a href="eliminar_recursos.php?id=<?= $rec['id_recurso'] ?>" onclick="return confirm('¿Seguro que deseas eliminar este usuario?')"><img class="eliminar" src="resources/img_eliminar.png" alt="delete" ></a>
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
