<?php
require_once "../modelo/inasistenciaDAO.php";
require_once "../modelo/inasistenciaDTO.php";

$inasistenciaDAO = new inasistenciaDAO();

// ✅ Obtener la inasistencia por ID
if (isset($_GET['id'])) {
    $id = intval($_GET['id']);
    $inasistencias = $inasistenciaDAO->listarInasistencia();

    $inasistencia = null;
    foreach ($inasistencias as $i) {
        if ($i['id_inasistencia'] == $id) {
            $inasistencia = $i;
            break;
        }
    }

    if (!$inasistencia) {
        die("Inasistencia no encontrada");
    }
} else {
    die("ID no proporcionado");
}

// ✅ Procesar actualización
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $inasistenciaDTO = new inasistenciaDTO();
    $inasistenciaDTO->setIdInasistencia($_POST['id_inasistencia']);
    $inasistenciaDTO->setFechaIna($_POST['fecha_inasistencia']);
    $inasistenciaDTO->setEstadoIna($_POST['estado_inasistencia']);
    $inasistenciaDTO->setJornada($_POST['jornada_id']);
    $inasistenciaDTO->setAprendiz($_POST['aprendiz_Usuario_id_usuario']);
    $inasistenciaDTO->setInstructor($_POST['instructor_Usuario_id_usuario']);

    if ($inasistenciaDAO->actualizarInasistencia($inasistenciaDTO)) {
        header("Location: consultar_inasistencia.php");
        exit;
    } else {
        echo "Error al actualizar la inasistencia.";
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Editar Inasistencia</title>
  <link rel="stylesheet" href="css/registrar_asistencia.css" />
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
    <h2>Editar Inasistencia</h2>
    <form method="POST">
      <input type="hidden" name="id_inasistencia" value="<?= htmlspecialchars($inasistencia['id_inasistencia']) ?>">

      <label>Fecha de inasistencia:</label>
      <input type="datetime-local" name="fecha_inasistencia" 
             value="<?= htmlspecialchars($inasistencia['fecha_inasistencia']) ?>" required><br>

      <label>Estado:</label>
      <select name="estado_inasistencia" required>
          <option value="S" <?= $inasistencia['estado_inasistencia'] == 'S' ? 'selected' : '' ?>>Asistió (S)</option>
          <option value="R" <?= $inasistencia['estado_inasistencia'] == 'R' ? 'selected' : '' ?>>Retraso (R)</option>
          <option value="N" <?= $inasistencia['estado_inasistencia'] == 'N' ? 'selected' : '' ?>>No asistió (N)</option>
      </select><br>

      <label>Jornada:</label>
      <input type="number" name="jornada_id" value="<?= htmlspecialchars($inasistencia['jornada_id']) ?>" required><br>

      <label>Aprendiz Id:</label>
      <input type="number" name="aprendiz_Usuario_id_usuario" 
             value="<?= htmlspecialchars($inasistencia['aprendiz_Usuario_id_usuario']) ?>" required><br>

      <label>Instructor Id:</label>
      <input type="number" name="instructor_Usuario_id_usuario" 
             value="<?= htmlspecialchars($inasistencia['instructor_Usuario_id_usuario']) ?>" required><br>

      <button type="submit" class="btn">Actualizar</button>
    </form>
  </main>
</body>
</html>
