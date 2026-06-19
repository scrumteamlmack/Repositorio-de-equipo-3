<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Asistencia</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="css/registrar_asistencia.css" />
  <link rel="shortcut icon" href="resources/fondo oscuro N L-MACK.png" type="image/x-icon">
  
</head>
<body>
  <header>
        <div class="logo">
            <img src="resources/fondo oscuro N L-MACK.png" alt="Logo L-MACK" class="img">
        </div>
        <nav>
            <a href="index.html">Inicio</a>
            <a href="login.html">Cerrar Sesion</a>
        </nav>
    </header>

    <main>
         <h2>Registro de Inasistencia</h2>

  <form action="../controlador/InasistenciaController.php" method="POST">
<input type="hidden" id="id_inasistencia" name="id_inasistencia">

    <!-- Fecha de inasistencia -->
    <label for="fecha_inasistencia">Fecha de inasistencia:</label>
    <input type="datetime-local" name="fecha_inasistencia" id="fecha_inasistencia" required>
    <br><br>

    <!-- Estado de inasistencia -->
    <label for="estado_inasistencia">Estado:</label>
    <select name="estado_inasistencia" id="estado_inasistencia" required>
      <option value="S">Asistió (S)</option>
      <option value="R">Retraso (R)</option>
      <option value="N">No asistió (N)</option>
    </select>
    <br><br>

    <!-- Jornada -->
    <label for="jornada_id">Jornada:</label>
    <input type="number" name="jornada_id" id="jornada_id" placeholder="ID de jornada" required>
    <br><br>

    <!-- Aprendiz -->
    <label for="aprendiz_id">Aprendiz (ID usuario):</label>
    <input type="number" name="aprendiz_id" id="aprendiz_id" placeholder="ID de aprendiz" required>
    <br><br>

    <!-- Instructor -->
    <label for="instructor_id">Instructor (ID usuario):</label>
    <input type="number" name="instructor_id" id="instructor_id" placeholder="ID de instructor" required>
    <br><br>

    <button type="submit" class="btn" name="btnInasistencia">Registrar Inasistencia</button>
  </form>
        
    </main>


  <footer>
    &copy; 2025 L-MACK
  </footer>
</body>
</html>
