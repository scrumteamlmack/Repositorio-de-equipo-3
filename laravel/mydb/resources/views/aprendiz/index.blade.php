@extends('layouts.app')

@section('content')
<div class="container">
    <h2>Listado de Aprendices</h2>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>ID Aprendiz</th>
                <th>Nombre Usuario</th>
                <th>Correo</th>
                <th>Programa</th>
                <th>Ficha</th>
            </tr>
        </thead>
        <tbody>
            @foreach ($aprendices as $aprendiz)
                <tr>
                    <td>{{ $aprendiz->id_aprendiz }}</td>
                    <td>{{ $aprendiz->usuario->p_nombre }} {{ $aprendiz->usuario->p_apellido }}</td>
                    <td>{{ $aprendiz->usuario->correo }}</td>
                    <td>{{ $aprendiz->programas_id_programas }}</td>
                    <td>{{ $aprendiz->ficha_idficha }}</td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>
@endsection
