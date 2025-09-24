<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>@yield('title', 'Usuarios')</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
   
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    
<div class="container">
    <a href="{{  route('usuarios.index') }}" class="navbar-brand">Tienda
    </a>
</div>
</nav>

<main class="container mt-4">
    @if(session('succes'))
    <div class="alert alert-success">{{session('success')}}</div>
    @endif
 @yield('content')
</main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>

