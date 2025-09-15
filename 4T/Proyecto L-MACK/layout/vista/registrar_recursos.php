<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Registrar Recursos - L-MACK</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="css/registrar_recursos.css" />
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
    <h2>Registrar Recursos</h2>
    <form action="/../layout/controlador/RecursosController.php" method="post">

      <input type="hidden" id="id_recurso" name="id_recurso">


      <label for="serial_recurso">Serial del Recurso</label>
      <input type="text" id="serial_recurso" name="serial_recurso" placeholder="Ej: PC-LAB-003, PROY-AUL-002" required>

      <label for="num_recurso">Número del Recurso</label>
      <input type="text" id="num_recurso" name="num_recurso" placeholder="Ej: 1, 12" required>

      <label for="nombre_recurso">Nombre del Recurso</label>
      <input type="text" id="nombre_recurso" name="nombre_recurso" placeholder="Ej: Proyector, Extensión, Altavoz" required>

      <label for="tipo_recurso">Tipo de Recurso</label>
      <input type="number" id="tipo_recurso" name="tipo_recurso" placeholder="ID del tipo de recurso" required>

      <label for="estado">Estado</label>
      <select id="estadoRec" name="estadoRec" required>
        <option value="Disponible">Disponible</option>
        <option value="Dañado">Dañado</option>
        <option value="En mantenimiento">En mantenimiento</option>
      </select>

      <label for="observacion">Observación</label>
      <textarea id="observacion" name="observacion" placeholder="Escriba alguna observación sobre el recurso..."></textarea>

      <label for="ambiente_id">Ambiente</label>
      <input type="number" id="ambiente_id_rec" name="ambiente_id_rec" placeholder="ID del ambiente" required>

      <button type="submit" class="btn" name="btnRecurso">Registrar Recurso</button>
    </form>
  </main>

  <footer>
    &copy; 2025 L-MACK - Registro de Recursos
  </footer>

</body>
</html>
