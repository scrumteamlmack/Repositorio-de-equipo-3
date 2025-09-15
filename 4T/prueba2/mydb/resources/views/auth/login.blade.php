<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h2>Iniciar Sesión</h2>

    @if($errors->any())
        <div style="color:red;">
            <ul>
                @foreach($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
    @endif

    <form method="POST" action="{{ route('login.submit') }}">
        @csrf
        <input type="number" name="num_documento" placeholder="documento" required>
        <input type="password" name="contraseña" placeholder="Contraseña" required>
        <button type="submit">Entrar</button>
    </form>
</body>
</html>
