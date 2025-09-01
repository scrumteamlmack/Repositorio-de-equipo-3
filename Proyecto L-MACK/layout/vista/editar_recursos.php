<?php
require_once "../modelo/recursosDAO.php";
require_once "../modelo/recursosDTO.php";

$recursosDAO = new recursosDAO();

// ✅ Obtener usuario por ID
if (isset($_GET['id'])) {
    $id = intval($_GET['id']);
    $recursos = $recursosDAO->listarRecursos();

    // Buscar el usuario que coincide con el ID
    $rec = null;
    foreach ($recursos as $r) {
        if ($r['id_recurso'] == $id) {
            $recursos = $r;
            break;
        }
    }

    if (!$recursos) {
        die("Recurso no encontrado");
    }
} else {
    die("ID no proporcionado");
}

// ✅ Procesar actualización
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $recursosDTO = new recursosDTO();
    $recursosDTO->setIdRecurso($_POST['id_recurso']);
    $recursosDTO->setSerialRec($_POST['serial_recurso']);
    $recursosDTO->setNumRec($_POST['num_recurso']);
    $recursosDTO->setNomRec($_POST['nombre_recurso']);
    $recursosDTO->setTipoRecurso($_POST['tipo_recurso']);
    $recursosDTO->setEstadoRec($_POST['estadoRec']);
    $recursosDTO->setObservacion($_POST['observacion']);
    $recursosDTO->setAmbienteRec($_POST['ambiente_id_rec']);

    if ($recursosDAO->actualizarRecursos($recursosDTO)) {

        header("Location: consultar_recursos.php");
        exit;
    } else {
        echo "Error al actualizar el recurso.";
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
  <link rel="stylesheet" href="css/registrar_recursos.css" />
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
    <h2>Editar Recursos</h2>
  <form method="POST">
      <input type="hidden" name="id_recurso" value="<?= htmlspecialchars($recursos['id_recurso']) ?>">

   <label>Serial del recurso:</label>
      <input type="text" name="serial_recurso" value="<?= htmlspecialchars($recursos['serial_recurso']) ?>" required><br>


       <label>Número del recurso:</label>
      <input type="number" name="num_recurso" value="<?= htmlspecialchars($recursos['num_recurso']) ?>" required><br>

   
      <label>Nombre del Recurso:</label>
      <input type="text" name="nombre_recurso" value="<?= htmlspecialchars($recursos['nombre_recurso']) ?>" required><br>

      <label>Tipo de Recurso:</label>
      <input type="text" name="tipo_recurso" value="<?= htmlspecialchars($recursos['tipo_recurso']) ?>"><br>

      <label>Estado:</label>
      <select name="estadoRec"  
      required>
      <option value="<?= htmlspecialchars($recursos['estado']) ?>">Seleccione tipo de documento</option>
                <option value="Disponible">Disponible</option>
                <option value="Dañado">Dañado</option>
                <option value="En mantenimiento">En mantenimiento</option>
            </select><br>
      <label>Observación:</label>
      <input type="text" name="observacion" value="<?= htmlspecialchars($recursos['observacion']) ?>"><br>

      <label>Ambiente:</label>
      <input type="number" name="ambiente_id_rec" value="<?= htmlspecialchars($recursos['ambiente_id']) ?>" required><br>

      <button type="submit" class="btn">Actualizar</button>
  </form>
  </main>
</body>
</html>
