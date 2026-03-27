<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Administrador</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="{{ asset('css/dashboard.css') }}">

  
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
                <h2>Panel Admin</h2>
                <nav>
                    <ul>
                        <!-- Opción Inicio -->
                        <li>
    <a href="{{ route('dashboard') }}" class="btnInicio"><span>Inicio</span></a>
</li>


                        <li onclick="toggleSubmenu(this)">
                            <span>Usuarios</span>
                            <ul class="submenu">
                                <li><a href="{{ route('usuarios.create') }}">Registrar Usuario</a></li>
                                <li><a href="{{ route('usuarios.index') }}">Consultar Usuarios</a></li>
                            </ul>
                        </li>
                        <!-- ✅ Fichas -->
                        <li onclick="toggleSubmenu(this)">
                            <span>Fichas</span>
                            <ul class="submenu">
                                <li><a href="{{ route('fichas.create') }}">Registrar Ficha</a></li>
                                <li><a href="{{ route('fichas.index') }}">Consultar Fichas</a></li>
                            </ul>
                        </li>
                         <li onclick="toggleSubmenu(this)">
                            <span>Ambientes</span>
                            <ul class="submenu">
                                <li><a href="{{ route('ambientes.create') }}">Registrar Ambiente</a></li>
                                <li><a href="{{ route('ambientes.index') }}">Consultar Ambiente</a></li>
                            </ul>
                        </li>
                        </li>
                        <li onclick="toggleSubmenu(this)">
                            <span>Recursos</span>
                            <ul class="submenu">
                                <li><a href=" {{ route('recursos.create') }}">Registrar Recurso</a></li>
                                <li><a href="{{  route('recursos.index') }}">Consultar Recursos</a></li>
</ul>
</li> <li onclick="toggleSubmenu(this)">
                            <span>Traslado Recursos</span>
                            <ul class="submenu">
                                <li><a href=" {{ route('traslados.create') }}">Registrar Traslado</a></li>
                                <li><a href="{{  route('traslados.index') }}">Consultar Traslado</a></li>
                            </ul>
                        </li>
                    </ul>
                </nav>
            </div>

            <!-- Botón cerrar sesión -->
            <form action="{{ route('logout') }}" method="POST" style="display:inline;">
    @csrf
    <button type="submit" class="btn-logout">🚪 Cerrar sesión</button>
</form>

        </aside>

        <!-- Contenido dinámico -->
        <main class="content">
           @if(Auth::check())
    <p>Bienvenido {{ Auth::user()->p_nombre }}  {{ Auth::user()->p_apellido }}</p>
@else
    <script>window.location.href = "{{ route('login') }}";</script>
@endif

            @yield('content')
        </main>
    </div>
</body>
</html>
