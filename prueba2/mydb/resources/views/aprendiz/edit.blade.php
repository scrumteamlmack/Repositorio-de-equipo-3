@extends('layouts.aprendiz')

@section('content')
<head>
    <link rel="stylesheet" href="{{ asset('css/edit_aprendiz.css') }}">
</head>
<div class="container">
    <h2>Editar Perfil</h2>

    @if ($errors->any())
        <div class="alert alert-danger">
            <ul>
              @foreach ($errors->all() as $error)
                <li>{{ $error }}</li>
              @endforeach
            </ul>
        </div>
    @endif

    <form action="{{ route('aprendiz.update', $aprendiz->Usuario_id_usuario ?? $aprendiz->id) }}" method="POST">
        @csrf
        @method('PUT')

        <div class="form-group">
            <label>Primer Nombre</label>
            <input type="text" name="p_nombre" class="form-control"
                value="{{ old('p_nombre', $aprendiz->usuario->p_nombre ?? '') }}" required>
        </div>

        <div class="form-group">
            <label>Segundo Nombre</label>
            <input type="text" name="s_nombre" class="form-control"
                value="{{ old('s_nombre', $aprendiz->usuario->s_nombre ?? '') }}">
        </div>

        <div class="form-group">
            <label>Primer Apellido</label>
            <input type="text" name="p_apellido" class="form-control"
                value="{{ old('p_apellido', $aprendiz->usuario->p_apellido ?? '') }}" required>
        </div>

        <div class="form-group">
            <label>Segundo Apellido</label>
            <input type="text" name="s_apellido" class="form-control"
                value="{{ old('s_apellido', $aprendiz->usuario->s_apellido ?? '') }}">
        </div>

        <div class="form-group">
            <label>Tipo Documento</label>
            <input type="text" name="tipo_documento" class="form-control"
                value="{{ old('tipo_documento', $aprendiz->usuario->tipo_documento ?? '') }}" required>
        </div>

        <div class="form-group">
            <label>Número Documento</label>
            <input type="number" name="num_documento" class="form-control"
                value="{{ old('num_documento', $aprendiz->usuario->num_documento ?? '') }}" required>
        </div>

        <div class="form-group">
            <label>Correo</label>
            <input type="email" name="correo" class="form-control"
                value="{{ old('correo', $aprendiz->usuario->correo ?? '') }}" required>
        </div>

        <button type="submit" class="btn btn-success">Guardar</button>
        <a href="{{ route('aprendiz.perfil') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection
