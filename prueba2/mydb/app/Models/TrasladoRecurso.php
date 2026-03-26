<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class TrasladoRecurso extends Model
{
    use HasFactory;

    protected $table = 'traslado_recurso';
    protected $primaryKey = 'id_traslado';
    public $timestamps = false;

    protected $fillable = [
        'recurso_id',
        'ambiente_origen',
        'ambiente_destino',
        'fecha_traslado',
        'observacion'
    ];

    public function recurso()
    {
        return $this->belongsTo(Recurso::class, 'recurso_id', 'id_recurso');
    }

    public function ambienteOrigen()
    {
        return $this->belongsTo(Ambiente::class, 'ambiente_origen', 'id_ambiente');
    }

    public function ambienteDestino()
    {
        return $this->belongsTo(Ambiente::class, 'ambiente_destino', 'id_ambiente');
    }
}
