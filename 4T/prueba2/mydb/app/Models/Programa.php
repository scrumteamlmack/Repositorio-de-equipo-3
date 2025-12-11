<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Programa
 * 
 * @property int $id_programas
 * @property string $nombre_programa
 * @property string $nivel_formacion
 * @property string $duracion
 * @property int $jornada_id
 * @property int $modalidad_id
 * @property int $coordinacion_id
 * 
 * @property Jornada $jornada
 * @property Modalidad $modalidad
 * @property Coordinacion $coordinacion
 * @property Collection|Aprendiz[] $aprendizs
 *
 * @package App\Models
 */
class Programa extends Model
{
	protected $table = 'programas';
	protected $primaryKey = 'id_programas';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'id_programas' => 'int',
		'jornada_id' => 'int',
		'modalidad_id' => 'int',
		'coordinacion_id' => 'int'
	];

	protected $fillable = [
		'nombre_programa',
		'nivel_formacion',
		'duracion',
		'jornada_id',
		'modalidad_id',
		'coordinacion_id'
	];

	public function jornada()
	{
		return $this->belongsTo(Jornada::class);
	}

	public function modalidad()
	{
		return $this->belongsTo(Modalidad::class);
	}

	public function coordinacion()
	{
		return $this->belongsTo(Coordinacion::class);
	}

	public function aprendizs()
	{
		return $this->hasMany(Aprendiz::class, 'programas_id_programas');
	}
}
