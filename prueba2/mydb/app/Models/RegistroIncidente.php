<?php

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

class RegistroIncidente extends Model
{
    protected $table = 'registro_incidente';
    protected $primaryKey = 'id_incidente';
    public $timestamps = false;

    protected $casts = [
        'fecha_incidente' => 'date', // SOLO date, no datetime
        'ambiente_id' => 'int',
        'tipo_inc_id' => 'int',
        'usuario_id_usuario' => 'int'
    ];

    protected $fillable = [
        'descripcion',
        'fecha_incidente',
        'hora_incidente',
        'ambiente_id',
        'tipo_inc_id',
        'usuario_id_usuario'
    ];

    // ✅ Accessor para convertir la hora en Carbon
    public function getHoraIncidenteAttribute($value)
    {
        return $value ? Carbon::createFromFormat('H:i:s', $value) : null;
    }

    // Relaciones
    public function ambiente()
    {
        return $this->belongsTo(Ambiente::class, 'ambiente_id', 'id_ambiente');
    }

    public function tipo_incidente()
    {
        return $this->belongsTo(TipoIncidente::class, 'tipo_inc_id', 'id_tipo_inc');
    }

    public function usuario()
    {
        return $this->belongsTo(Usuario::class, 'usuario_id_usuario');
    }

    public function historico_incidentes()
    {
        return $this->hasMany(HistoricoIncidente::class, 'incidente_id');
    }
}
