<?php
require_once '../controlador/InasistenciaController.php';

// Instanciamos el controlador y traemos los usuarios
$inasistenciaDAO = new inasistenciaDAO();
$inasistencia = $inasistenciaDAO->listarInasistencia();
?>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Consulta de Usuarios</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
<link rel="stylesheet" href="css/consulta_asistencia.css">
<style>
  .edit{
    width: 3em;
    height: 3em;
    margin-left: 20px;
}

.eliminar{
    width: 3em;
    height: 3em;
    margin-left: 5px;
}
</style>
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
    <h2>Lista de Inasistencias Registradas</h2>

    <?php if (!empty($inasistencia)) : ?>
      <table border="1" cellpadding="8" cellspacing="0">
          <thead>
              <tr>
                <th>Id usuario</th>
                  <th>Fecha/Hora de asistencia</th>
                  <th>Estado de asistencia</th>
                  <th>Id de la jornada</th>
                  <th>Id del aprendiz</th>
                  <th>Id del instructor</th>
                  <th>Actualizar</th>
                  <th>Eliminar</th>
              </tr>
          </thead>
          <tbody>
              <?php foreach ($inasistencia as $ina): ?>
                  <tr>
                      <td><?= htmlspecialchars($ina['id_inasistencia']) ?></td>
                      <td><?= htmlspecialchars($ina['fecha_inasistencia']) ?></td>
                      <td><?= htmlspecialchars($ina['estado_inasistencia']) ?></td>
                      <td><?= htmlspecialchars($ina['jornada_id']) ?></td>
                      <td><?= htmlspecialchars($ina['aprendiz_Usuario_id_usuario']) ?></td>
                      <td><?= htmlspecialchars($ina['instructor_Usuario_id_usuario']) ?></td>
                      <td>
        <a href="editar_inasistencia.php?id=<?= $ina['id_inasistencia'] ?>"><img class="edit" src="resources/img_edit.png" alt="edit" ></a> </td>
                      <td> 
        <a href="eliminar_inasistencia.php?id=<?= $ina['id_inasistencia'] ?>" onclick="return confirm('¿Seguro que deseas eliminar este usuario?')"><img class="eliminar" src="resources/img_eliminar.png" alt="delete" ></a>
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
