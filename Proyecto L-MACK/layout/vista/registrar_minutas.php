<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Registrar Minuta - L-MACK</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
<link rel="stylesheet" href="css/registrar_minuta.css" />
  <link rel="shortcut icon" href="resources/fondo oscuro N L-MACK.png" type="image/x-icon">
</head>
<body>

  <header>
    <div class="logo">
      <img src="resources/fondo oscuro N L-MACK.png" alt="Logo L-MACK" class="img">
    </div>
    <nav>
      <a href="index.html">Inicio</a>
      <a href="login.html">Cerrar Sesión</a>
    </nav>
  </header>

  <main>
    <h2>Registrar Minuta</h2>
    <form action="../controlador/MinutaController.php" method="post">
      
      
      <input type="hidden" id="id_minuta" name="id_minuta">

      <label for="fecha_hora_recibo">Fecha y hora de recibo</label>
      <input type="datetime-local" id="fecha_hora_recibo" name="fecha_hora_recibo" required>

      <label for="fecha_hora_entrega">Fecha y hora de entrega</label>
      <input type="datetime-local" id="fecha_hora_entrega" name="fecha_hora_entrega" required>

      <label for="novedad">Novedad</label>
      <textarea id="novedad" name="novedad" rows="4" placeholder="Describe alguna eventualidad o novedad ocurrida"></textarea>

      <label for="descripcion_min">Observaciones Generales</label>
      <textarea id="descripcion_min" name="descripcion_min" rows="4" placeholder="Escribe observaciones generales"></textarea>

      <label for="estado">Estado</label>
      <input type="text" id="estado" name="estado" placeholder="Estado del ambiente (Ej: Limpio, Desordenado...)" required>

      <label for="ambiente_id">ID Ambiente</label>
      <input type="number" id="ambiente_id" name="ambiente_id" required>

      <label for="guarda_seguridad_Usuario_id_usuario">ID Guarda Seguridad</label>
      <input type="number" id="guarda_seguridad_Usuario_id_usuario" name="guarda_seguridad_Usuario_id_usuario" required>

      <label for="responsable_id">ID Responsable</label>
      <input type="number" id="responsable_id" name="responsable_id" required>

      <button type="submit" class="btn" name="btnMinuta">Guardar Minuta</button>
    </form>
  </main>

  <footer>
    &copy; 2025 L-MACK - Registro de Minutas
  </footer>

</body>
</html>
