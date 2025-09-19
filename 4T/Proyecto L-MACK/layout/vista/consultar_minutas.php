<?php
require_once '../controlador/MinutaController.php';

// Instanciamos el controlador y traemos los usuarios
$minutaDAO = new minutaDAO();
$minuta = $minutaDAO->listarMinuta();
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

    <?php if (!empty($minuta)): ?>
      <table border="1" cellpadding="8" cellspacing="0">
          <thead>
              <tr>
                <th>Id usuario</th>
                  <th>Fecha/Hora de recibido</th>
                  <th>Fecha/hora de entrega</th>
                  <th>Novedad</th>
                  <th>Observaciones generales</th>
                  <th>Estado</th>
                  <th>Id_ambiente</th>
                  <th>Id_guarda</th>
                  <th>Id_responsable</th>
                  <th>Actualizar</th>
                  <th>Eliminar</th>
              </tr>
          </thead>
          <tbody>
              <?php foreach ($minuta as $min): ?>
                  <tr>
                      <td><?= htmlspecialchars($min['id_minuta']) ?></td>
                      <td><?= htmlspecialchars($min['fecha_hora_recibo']) ?></td>
                      <td><?= htmlspecialchars($min['fecha_hora_entrega']) ?></td>
                      <td><?= htmlspecialchars($min['novedad']) ?></td>
                      <td><?= htmlspecialchars($min['descripcion_min']) ?></td>
                      <td><?= htmlspecialchars($min['estado']) ?></td>
                      <td><?= htmlspecialchars($min['ambiente_id']) ?></td>
                      <td><?= htmlspecialchars($min['guarda_seguridad_Usuario_id_usuario']) ?></td>
                      <td><?= htmlspecialchars($min['responsable_id']) ?></td>
                      <td>
        <a href="editar_minuta.php?id=<?= $min['id_minuta'] ?>"><img class="edit" src="resources/img_edit.png" alt="edit" ></a> </td>
                      <td> 
        <a href="eliminar_minuta.php?id=<?= $min['id_minuta'] ?>" onclick="return confirm('¿Seguro que deseas eliminar este usuario?')"><img class="eliminar" src="resources/img_eliminar.png" alt="delete" ></a>
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
