<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Coordinacion
 * 
 * @property int $id_coordinacion
 * @property string $nombre_coordinacion
 * @property string $correo_coordinacion
 * 
 * @property Collection|AlertasInasistencium[] $alertas_inasistencia
 * @property Collection|Coordinador[] $coordinadors
 * @property Collection|Instructor[] $instructors
 * @property Collection|Programa[] $programas
 *
 * @package App\Models
 */
class Coordinacion extends Model
{
	protected $table = 'coordinacion';
	protected $primaryKey = 'id_coordinacion';
	public $timestamps = false;

	protected $fillable = [
		'nombre_coordinacion',
		'correo_coordinacion'
	];

	public function alertas_inasistencia()
	{
		return $this->hasMany(AlertasInasistencium::class);
	}

	public function coordinadors()
	{
		return $this->hasMany(Coordinador::class, 'coordinacion_id_coordinacion');
	}

	public function instructors()
	{
		return $this->hasMany(Instructor::class, 'coordinacion_id_coordinacion');
	}

	public function programas()
	{
		return $this->hasMany(Programa::class);
	}
}
