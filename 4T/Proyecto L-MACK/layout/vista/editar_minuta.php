<?php
require_once "../modelo/minutaDAO.php";
require_once "../modelo/minutaDTO.php";

$minutaDAO = new minutaDAO();

// ✅ Obtener usuario por ID
if (isset($_GET['id'])) {
    $id = intval($_GET['id']);
    $minuta = $minutaDAO->listarMinuta();

    // Buscar el usuario que coincide con el ID
    $min = null;
    foreach ($minuta as $m) {
        if ($m['id_minuta'] == $id) {
            $minuta = $m;
            break;
        }
    }

    if (!$minuta) {
        die("Usuario no encontrado");
    }
} else {
    die("ID no proporcionado");
}

// ✅ Procesar actualización
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $minutaDTO = new minutaDTO();
    $minutaDTO->setIdMinuta($_POST['id_minuta']);
    $minutaDTO->setFechaRec($_POST['fecha_hora_recibo']);
    $minutaDTO->setFechaEnt($_POST['fecha_hora_entrega']);
    $minutaDTO->setNovedad($_POST['novedad']);
    $minutaDTO->setDescripcion($_POST['descripcion_min']);
    $minutaDTO->setEstado($_POST['estado']);
    $minutaDTO->setAmbiente($_POST['ambiente_id']);
    $minutaDTO->setGuarda($_POST['guarda_seguridad_Usuario_id_usuario']);
    $minutaDTO->setResponsable($_POST['responsable_id']);

    if ($minutaDAO->actualizarMinuta($minutaDTO)) {

        header("Location: consultar_minutas.php");
        exit;
    } else {
        echo "Error al actualizar la minuta.";
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
  <link rel="stylesheet" href="css/registrar_minuta.css" />
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
    <h2>Editar Minuta</h2>
  <form method="POST">
      <input type="hidden" name="id_minuta" value="<?= htmlspecialchars($minuta['id_minuta']) ?>">

   <label>Fecha y hora de recibido:</label>
      <input type="datetime-local" name="fecha_hora_recibo" value="<?= htmlspecialchars($minuta['fecha_hora_recibo']) ?>" required><br>

       <label>Fecha y hora de salida:</label>
      <input type="datetime-local" name="fecha_hora_entrega" value="<?= htmlspecialchars($minuta['fecha_hora_entrega']) ?>" required><br>

   
      <label>Novedad:</label>
      <input type="text" name="novedad" value="<?= htmlspecialchars($minuta['novedad']) ?>" required><br>

      <label>Observaciones generales:</label>
      <input type="text" name="descripcion_min" value="<?= htmlspecialchars($minuta['descripcion_min']) ?>"><br>

      <label>Estado:</label>
      <input type="text" name="estado" value="<?= htmlspecialchars($minuta['estado']) ?>" required><br>

      <label>ID Ambiente:</label>
      <input type="number" name="ambiente_id" value="<?= htmlspecialchars($minuta['ambiente_id']) ?>"><br>

      <label>ID Guarda Seguridad:</label>
      <input type="number" name="guarda_seguridad_Usuario_id_usuario" value="<?= htmlspecialchars($minuta['guarda_seguridad_Usuario_id_usuario']) ?>" required><br>
      
      <label>ID Responsable:</label>
      <input type="number" name="responsable_id" value="<?= htmlspecialchars($minuta['responsable_id']) ?>" required><br>
      <button type="submit" class="btn">Actualizar</button>
  </form>
  </main>
</body>
</html>
