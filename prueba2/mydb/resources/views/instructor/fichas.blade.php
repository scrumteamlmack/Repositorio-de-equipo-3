@extends('instructor.dashboard')

@section('content')
<link rel="stylesheet" href="{{ asset('css/perfil_edit.css') }}">

<div class="fichas-container">
    <h1>Mis Fichas</h1>

    @if($fichas->isEmpty())
        <p class="mensaje">No tienes fichas asignadas.</p>
    @else
        <div class="fichas-list">
            @foreach($fichas as $ficha)
                <div class="ficha-card">
                    <h2>Ficha #{{ $ficha->Num_ficha }}</h2>
                    <p><strong>Aprendices:</strong> {{ $ficha->aprendices_count }}</p>
                    <a href="{{ route('instructor.fichaAprendices', $ficha->idficha) }}" class="btn-ver">Ver aprendices</a>
                </div>
            @endforeach
        </div>
    @endif
</div>
@endsection
