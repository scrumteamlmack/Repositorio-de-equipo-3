<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Model;

/**
 * Class RegistroInasistencia
 * 
 * @property int $id_inasistencia
 * @property Carbon $fecha_inasistencia
 * @property string $estado_inasistencia
 * @property int $jornada_id
 * @property int $aprendiz_Usuario_id_usuario
 * @property int $instructor_Usuario_id_usuario
 * 
 * @property Jornada $jornada
 * @property Aprendiz $aprendiz
 * @property Instructor $instructor
 *
 * @package App\Models
 */
class RegistroInasistencia extends Model
{
	protected $table = 'registro_inasistencia';
	protected $primaryKey = 'id_inasistencia';
	public $timestamps = false;

	protected $casts = [
		'fecha_inasistencia' => 'datetime',
		'jornada_id' => 'int',
		'aprendiz_Usuario_id_usuario' => 'int',
		'instructor_Usuario_id_usuario' => 'int'
	];

	protected $fillable = [
		'fecha_inasistencia',
		'estado_inasistencia',
		'jornada_id',
		'aprendiz_Usuario_id_usuario',
		'instructor_Usuario_id_usuario'
	];

	public function jornada()
	{
		return $this->belongsTo(Jornada::class, 'jornada_id');
	}

	public function aprendiz()
	{
		return $this->belongsTo(Aprendiz::class, 'aprendiz_Usuario_id_usuario', 'Usuario_id_usuario');
	}

	public function instructor()
	{
		return $this->belongsTo(Instructor::class, 'instructor_Usuario_id_usuario');
	}
}
