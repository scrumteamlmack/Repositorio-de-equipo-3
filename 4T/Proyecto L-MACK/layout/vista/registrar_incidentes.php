<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Registrar Incidente - L-MACK</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="css/registrar_incidente.css" />
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
    <h2>Registrar Incidente</h2>
    <form action="../controlador/IncidentesController.php" method="post">
      
      <label for="descripcion">Descripción del Incidente</label>
      <textarea id="descripcion" name="descripcion" rows="5" placeholder="Describe el incidente con detalle" required></textarea>

      <label for="fecha_incidente">Fecha del Incidente</label>
      <input type="date" id="fecha_incidente" name="fecha_incidente" required>

      <label for="hora_incidente">Hora del Incidente</label>
      <input type="time" id="hora_incidente" name="hora_incidente" required>

      <label for="ambiente_id">Ambiente</label>
      <input type="number" id="ambiente_id" name="ambiente_id" placeholder="ID del ambiente" required>

      <label for="tipo_inc_id">Tipo de Incidente</label>
      <input type="number" id="tipo_inc_id" name="tipo_inc_id" placeholder="ID del tipo de incidente" required>

      <label for="usuario_id_usuario">Usuario</label>
      <input type="number" id="usuario_id_usuario" name="usuario_id_usuario" placeholder="ID del usuario relacionado" required>
<br><br>
      <button type="submit" class="btn">Registrar Incidente</button>
    </form>
  </main>

  <footer>
    &copy; 2025 L-MACK - Registro de Incidentes
  </footer>

</body>
</html>
