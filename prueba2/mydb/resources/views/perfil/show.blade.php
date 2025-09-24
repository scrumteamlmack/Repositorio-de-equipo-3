@extends('layouts.admin')

@section('content')
<div class="container">
    <h2>Perfil de {{ $usuario->nombre }}</h2>

    <p><strong>Email:</strong> {{ $usuario->correo }}</p>
    <p><strong>Teléfono:</strong> {{ $usuario->telefono ?? 'N/A' }}</p>

    <a href="{{ route('perfil.edit', $usuario->id_usuario) }}" class="btn btn-warning">Editar Perfil</a>
</div>
@endsection
