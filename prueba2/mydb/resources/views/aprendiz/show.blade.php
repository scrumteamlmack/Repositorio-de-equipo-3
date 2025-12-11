@extends('layouts.admin')

@section('content')
<div class="container">
    <h2>Perfil de Aprendiz</h2>

    <p><strong>Nombre:</strong> {{ $aprendiz->usuario->p_nombre ?? 'N/A' }}</p>
    <p><strong>Apellidos:</strong> {{ $aprendiz->usuario->p_apellido ?? 'N/A' }}</p>
    <p><strong>Correo:</strong> {{ $aprendiz->usuario->correo ?? 'N/A' }}</p>

    <a href="{{ route('aprendiz.edit', $aprendiz) }}" class="btn btn-warning">Editar Perfil</a>
</div>
@endsection
