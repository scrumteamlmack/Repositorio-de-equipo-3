<?php

namespace App\Http\Controllers;

use App\Models\HistoricoIncidente;

class HistoricoIncidenteController extends Controller
{
    public function index()
    {
        // Traemos el historial con las relaciones (evitamos N+1 queries)
        $historicos = HistoricoIncidente::with(['registro_incidente', 'ambiente', 'tipo_incidente'])
                        ->orderBy('fecha_registro', 'desc')
                        ->paginate(10);

        return view('historico_incidentes.index', compact('historicos'));
    }
}
