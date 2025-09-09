@extends('layouts.app')

@section('content')
<div class="container">
    <h1>Registrar Aprendiz</h1>

    <form action="{{ route('aprendiz.store') }}" method="POST">
    @csrf

    <input type="hidden" name="Usuario_id_usuario" value="{{ $usuario_id }}">

    <div>
        <label for="programas_id_programas">Programa</label>
        <select name="programas_id_programas" required>
            @foreach($programas as $programa)
                <option value="{{ $programa->id_programas }}">{{ $programa->nombre }}</option>
            @endforeach
        </select>
    </div>

    <div>
        <label for="ficha_idficha">Ficha</label>
        <select name="ficha_idficha" required>
            @foreach($fichas as $ficha)
                <option value="{{ $ficha->idficha }}">{{ $ficha->codigo }}</option>
            @endforeach
        </select>
    </div>

    <button type="submit">Guardar Aprendiz</button>
</form>

</div>
@endsection
