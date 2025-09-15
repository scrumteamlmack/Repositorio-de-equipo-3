<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Model;

/**
 * Class RegistroMinutum
 * 
 * @property int $id_minuta
 * @property Carbon $fecha_hora_recibo
 * @property Carbon $fecha_hora_entrega
 * @property string|null $novedad
 * @property string|null $descripcion_min
 * @property string $estado
 * @property int $ambiente_id
 * @property int $guarda_seguridad_Usuario_id_usuario
 * @property int $responsable_id
 * @property string|null $registro_minutacol
 * 
 * @property Ambiente $ambiente
 * @property GuardaSeguridad $guarda_seguridad
 * @property Instructor $instructor
 *
 * @package App\Models
 */
class RegistroMinutum extends Model
{
	protected $table = 'registro_minuta';
	protected $primaryKey = 'id_minuta';
	public $timestamps = false;

	protected $casts = [
		'fecha_hora_recibo' => 'datetime',
		'fecha_hora_entrega' => 'datetime',
		'ambiente_id' => 'int',
		'guarda_seguridad_Usuario_id_usuario' => 'int',
		'responsable_id' => 'int'
	];

	protected $fillable = [
		'fecha_hora_recibo',
		'fecha_hora_entrega',
		'novedad',
		'descripcion_min',
		'estado',
		'ambiente_id',
		'guarda_seguridad_Usuario_id_usuario',
		'responsable_id',
		'registro_minutacol'
	];

	public function ambiente()
	{
		return $this->belongsTo(Ambiente::class);
	}

	public function guarda_seguridad()
	{
		return $this->belongsTo(GuardaSeguridad::class, 'guarda_seguridad_Usuario_id_usuario');
	}

	public function instructor()
	{
		return $this->belongsTo(Instructor::class, 'responsable_id');
	}
}
