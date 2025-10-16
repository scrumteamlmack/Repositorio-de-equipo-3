@extends('instructor.dashboard')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/ins_aprendiz.css') }}">
</head>
<div class="container">
    <h2>Aprendices de la Ficha: {{ $ficha->Num_ficha }}</h2>

    @if($ficha->aprendices->isEmpty())
        <div class="alert alert-info">
            No hay aprendices asignados a esta ficha.
        </div>
    @else
        <table class="table table-bordered mt-3">
            <thead class="thead-light">
                <tr>
                    <th>#</th>
                    <th>Nombre Completo</th>
                    <th>Tipo Documento</th>
                    <th>Número Documento</th>
                    <th>Correo</th>
                </tr>
            </thead>
            <tbody>
                @foreach($ficha->aprendices as $index => $aprendiz)
                    <tr>
                        <td>{{ $index + 1 }}</td>
                        <td>
                            {{ $aprendiz->usuario->p_nombre ?? '' }}
                            {{ $aprendiz->usuario->s_nombre ?? '' }}
                            {{ $aprendiz->usuario->p_apellido ?? '' }}
                            {{ $aprendiz->usuario->s_apellido ?? '' }}
                        </td>
                        <td>{{ $aprendiz->usuario->tipo_documento ?? '' }}</td>
                        <td>{{ $aprendiz->usuario->num_documento ?? '' }}</td>
                        <td>{{ $aprendiz->usuario->correo ?? '' }}</td>
                    </tr>
                @endforeach
            </tbody>
        </table>
    @endif

    <a href="{{ route('instructor.fichas') }}" class="btn btn-secondary mt-3">Volver a Fichas</a>
</div>
@endsection
