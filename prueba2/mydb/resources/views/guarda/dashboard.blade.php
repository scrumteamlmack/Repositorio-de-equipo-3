<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Guarda de Seguridad</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="{{ asset('css/guarda.css') }}">

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
                <h2>Panel Guarda</h2>
                <nav>
                    <ul>
                        <!-- Inicio -->
                        <li>
                            <a href="{{ route('guarda.dashboard') }}" class="btnInicio"><span>Inicio</span></a>
                        </li>

                        <!-- Perfil -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Mi perfil</span>
                            <ul class="submenu">
<<<<<<< HEAD
                                <li><a href="#">Ver perfil</a></li>
                                <li><a href="#">Editar perfil</a></li>
=======
                                <li><a href="{{ route('guarda.perfil') }}">Ver perfil</a></li>
                                <li><a href="{{ route('guarda.editar') }}">Editar perfil</a></li>
>>>>>>> 6b4a9da6b570592154cd1b9ae2483bf24c9bd186
                            </ul>
                        </li>

                        <!-- Minutas -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Minutas</span>
                            <ul class="submenu">
<<<<<<< HEAD
                                <li><a href="#">Ver minutas</a></li>
                                <li><a href="#">Registrar minuta</a></li>
=======
                                <li><a href="{{ route('minutas.index') }}">Ver minutas</a></li>
                                <li><a href="{{route('minutas.create')}}">Registrar minuta</a></li>
>>>>>>> 6b4a9da6b570592154cd1b9ae2483bf24c9bd186
                            </ul>
                        </li>

                        <!-- Ambientes -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Ambientes</span>
                            <ul class="submenu">
                                <li><a href="{{ route('ambientes.index') }}">Ver ambientes</a></li>
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
            <h2>Bienvenido {{ Auth::user()->p_nombre }} {{ Auth::user()->p_apellido }}</h2>
            <p>Aquí puedes gestionar tus minutas, consultar los ambientes y actualizar tus datos personales.</p>

            @yield('content')
        </main>
    </div>
</body>
</html>
