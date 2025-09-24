<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>L-MACK</title>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet" />
<link rel="stylesheet" href="{{ asset('css/estilos.css') }}">

 <link rel="shortcut icon" href="{{ asset('resources/fondo claro N L-MACK.png') }}" type="image/x-icon">


</head>
<body>
  <header>
    <div class="logo">
      <img src="{{ asset('resources/fondo oscuro N L-MACK.png') }}" alt="Logo L-MACK" width="150" class="img">

    </div>
    <nav aria-label="Navegación principal">
    </nav>
  </header>
  <main>
    <h1>Bienvenido al Sistema de Gestión</h1>
    <p class="lead">Accede fácilmente a tus funciones según tu rol: Instructor, Guarda, Aprendiz o Administrador.</p>
   <a href="{{ route('login') }}" class="btn">Iniciar Sesión</a>


  </main>
  <footer>
    &copy; 2025 L-MACK - Todos los derechos reservados
  </footer>
</body>
</html>

