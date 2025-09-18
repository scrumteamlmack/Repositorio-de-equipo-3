<!DOCTYPE html>
<html>
<head>
    <title>Editar Ambiente</title>
</head>
<body style="font-family: 'Poppins', sans-serif; background-color:#F5F5DC; color:#374151; min-height:100vh; display:flex; flex-direction:column; align-items:center; padding:32px 20px;">

    <h1 style="text-align:center; color:#10b981; font-weight:700; margin-bottom:24px;">Editar Ambiente</h1>

    <form action="{{ route('ambientes.update', $ambiente->id_ambiente) }}" method="POST"
          style="background-color:white; padding:32px; border-radius:12px; box-shadow:0 8px 20px rgba(16,185,129,0.15); width:100%; max-width:600px;">
        @csrf
        @method('PUT')

        <div style="margin-bottom:20px;">
            <label for="num_ambiente" style="font-weight:600; display:block; margin-bottom:6px;">Número de ambiente:</label>
            <input type="text" name="num_ambiente" value="{{ $ambiente->num_ambiente }}" required
                   style="width:100%; padding:12px; border-radius:8px; border:1px solid #d1d5db; font-size:1rem;">
        </div>

        <div style="margin-bottom:20px;">
            <label for="capacidad" style="font-weight:600; display:block; margin-bottom:6px;">Capacidad:</label>
            <input type="number" name="capacidad" value="{{ $ambiente->capacidad }}" required
                   style="width:100%; padding:12px; border-radius:8px; border:1px solid #d1d5db; font-size:1rem;">
        </div>

        <div style="margin-bottom:20px;">
            <label for="tipo_ambiente" style="font-weight:600; display:block; margin-bottom:6px;">Tipo de ambiente:</label>
            <input type="text" name="tipo_ambiente" value="{{ $ambiente->tipo_ambiente }}" required
                   style="width:100%; padding:12px; border-radius:8px; border:1px solid #d1d5db; font-size:1rem;">
        </div>

        <div style="margin-bottom:20px;">
            <label for="estado" style="font-weight:600; display:block; margin-bottom:6px;">Estado:</label>
            <input type="text" name="estado" value="{{ $ambiente->estado }}" required
                   style="width:100%; padding:12px; border-radius:8px; border:1px solid #d1d5db; font-size:1rem;">
        </div>

        <div style="display:flex; justify-content:flex-start; gap:16px; margin-top:24px;">
            <button type="submit"
                    style="background-color:#10b981; color:white; padding:14px 28px; border-radius:12px; font-weight:600; font-size:1rem; border:none; cursor:pointer; transition:background-color 0.3s;">
                Actualizar
            </button>
            <a href="{{ route('ambientes.index') }}"
               style="background-color:#059669; color:white; padding:14px 28px; border-radius:12px; font-weight:600; font-size:1rem; text-decoration:none; display:inline-block; text-align:center; transition:background-color 0.3s;">
                ⬅️ Volver
            </a>
        </div>
    </form>
</body>
</html>
