{{-- resources/views/instructor/dashboard.blade.php --}}
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Instructor</title>
    <link rel="stylesheet" href="{{ asset('css/instructor_dashboard.css') }}">
    <script>
        function toggleSubmenu(element) {
            let submenu = element.querySelector(".submenu");
            let visible = submenu.style.display === "block";
            document.querySelectorAll(".submenu").forEach(s => s.style.display = "none");
            submenu.style.display = visible ? "none" : "block";
        }
    </script>
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div>
                <h2>Panel Instructor</h2>
                <nav>
                    <ul>
                        <!-- Inicio -->
                        <li>
                            <a href="{{ route('instructor.dashboard') }}" class="btnInicio">
                                <span>Inicio</span>
                            </a>
                        </li>

                        <!-- Perfil -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Mi Perfil</span>
                            <ul class="submenu">
                                <li><a href="{{ route('instructor.perfil') }}">Ver Perfil</a></li>
                                <li><a href="{{ route('instructor.edit') }}">Editar Perfil</a></li>
</ul>
                        </li>

                        <!-- Fichas -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Fichas</span>
                            <ul class="submenu">
                                <li><a href="{{ route('instructor.fichas') }}">Mis Fichas</a></li>
                                <li><a href="{{ route('instructor.aprendices') }}">Ver Aprendices</a></li>
                            </ul>
                        </li>

                        <!-- Ambientes -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Ambientes</span>
                            <ul class="submenu">
                                <li><a href="{{ route('ambientes.index') }}">Ver Ambientes</a></li>
                            </ul>
                        </li>

                        <!-- Minutas -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Minutas</span>
                            <ul class="submenu">
                                <li><a href="{{ route('minutas.index') }}">Ver Minutas</a></li>
                            </ul>
                        </li>

                        <!-- Incidentes -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Incidentes</span>
                            <ul class="submenu">
                                <li><a href="{{ route('incidentes.create') }}">Registrar Incidente</a></li>
                                <li><a href="{{ route('incidentes.index') }}">Mis Incidentes</a></li>
                            <li><a href="{{ route('historico_incidentes.index') }}">Histórico Incidentes</a></li>
                            
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

        <!-- Contenido -->
        <main class="content">
            <h2>Bienvenido {{ Auth::user()->p_nombre }} {{ Auth::user()->p_apellido }}</h2>
            <p>
                Aquí puedes gestionar tu perfil, consultar fichas y aprendices, ambientes, revisar minutas y registrar incidentes.
            </p>
            @yield('content')
        </main>
    </div>
</body>
</html>
