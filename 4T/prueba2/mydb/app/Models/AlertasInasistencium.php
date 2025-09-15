<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Model;

/**
 * Class AlertasInasistencium
 * 
 * @property int $id_alerta
 * @property int $aprendiz_id
 * @property int $cantidad_fallas
 * @property Carbon $fecha_alerta
 * @property string $mensaje
 * @property int $coordinacion_id
 * 
 * @property Coordinacion $coordinacion
 *
 * @package App\Models
 */
class AlertasInasistencium extends Model
{
	protected $table = 'alertas_inasistencia';
	protected $primaryKey = 'id_alerta';
	public $timestamps = false;

	protected $casts = [
		'aprendiz_id' => 'int',
		'cantidad_fallas' => 'int',
		'fecha_alerta' => 'datetime',
		'coordinacion_id' => 'int'
	];

	protected $fillable = [
		'aprendiz_id',
		'cantidad_fallas',
		'fecha_alerta',
		'mensaje',
		'coordinacion_id'
	];

	public function coordinacion()
	{
		return $this->belongsTo(Coordinacion::class);
	}
}
