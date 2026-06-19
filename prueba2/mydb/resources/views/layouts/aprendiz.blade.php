<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Aprendiz</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="{{ asset('css/aprendiz.css') }}">

    <script>
        function toggleSubmenu(element) {
            let submenu = element.querySelector(".submenu");
            let visible = submenu.style.display === "block";

            // Cerrar todos los submenús
            document.querySelectorAll(".submenu").forEach(s => s.style.display = "none");

            // Abrir solo el actual
            submenu.style.display = visible ? "none" : "block";
        }
    </script>
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div>
                <h2>Panel Aprendiz</h2>
                <nav>
                    <ul>
                        <!-- Inicio -->
                        <li>
                            <a href="{{ route('aprendiz.dashboard') }}" class="btnInicio"><span>Inicio</span></a>
                        </li>

                        <!-- Mi Perfil -->
<li onclick="toggleSubmenu(this)">
    <span>Mi Perfil</span>
    <ul class="submenu">
        <li><a href="{{ route('aprendiz.perfil') }}">Ver mi perfil</a></li>
        <li><a href="{{ route('aprendiz.edit') }}">Editar mi perfil</a></li>
    </ul>
</li>

<!-- Programa -->
<li onclick="toggleSubmenu(this)">
    <span>Programa</span>
    <ul class="submenu">
        <li><a href="{{ route('aprendiz.programa') }}">Ver mi programa</a></li>
    </ul>
</li>

<!-- Ficha -->
<li onclick="toggleSubmenu(this)">
    <span>Ficha</span>
    <ul class="submenu">
        <li><a href="{{ route('aprendiz.ficha') }}">Ver mi ficha</a></li>
    </ul>
</li>

<!-- Asistencia -->
<li onclick="toggleSubmenu(this)">
    <span>Asistencia</span>
    <ul class="submenu">
        <li><a href="{{ route('aprendiz.asistencias') }}">Ver mis asistencias</a></li>
    </ul>
</li>

                    </ul>
                </nav>
            </div>

            <!-- Botón cerrar sesión -->
            <form method="POST" action="{{ route('logout') }}">
                @csrf
                <button type="submit">Cerrar sesión</button>
            </form>
        </aside>

        <!-- Contenido dinámico -->
        <main class="content">
            
            @yield('content')
        </main>
    </div>
</body>
</html>
