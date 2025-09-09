<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Model;

/**
 * Class TrasladoRecurso
 * 
 * @property int $id_traslado
 * @property int $recurso_id
 * @property int $ambiente_origen
 * @property int $ambiente_destino
 * @property Carbon $fecha_traslado
 * @property string|null $observacion
 * 
 * @property Recurso $recurso
 * @property Ambiente $ambiente
 *
 * @package App\Models
 */
class TrasladoRecurso extends Model
{
	protected $table = 'traslado_recurso';
	protected $primaryKey = 'id_traslado';
	public $timestamps = false;

	protected $casts = [
		'recurso_id' => 'int',
		'ambiente_origen' => 'int',
		'ambiente_destino' => 'int',
		'fecha_traslado' => 'datetime'
	];

	protected $fillable = [
		'recurso_id',
		'ambiente_origen',
		'ambiente_destino',
		'fecha_traslado',
		'observacion'
	];

	public function recurso()
	{
		return $this->belongsTo(Recurso::class);
	}

	public function ambiente()
	{
		return $this->belongsTo(Ambiente::class, 'ambiente_origen');
	}
}
