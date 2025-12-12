<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Jornada
 * 
 * @property int $id_jornada
 * @property string $nombre_jornada
 * 
 * @property Collection|Programa[] $programas
 * @property Collection|RegistroInasistencia[] $registro_inasistencia
 *
 * @package App\Models
 */
class Jornada extends Model
{
	protected $table = 'jornada';
	protected $primaryKey = 'id_jornada';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'id_jornada' => 'int'
	];

	protected $fillable = [
		'nombre_jornada'
	];

	public function programas()
	{
		return $this->hasMany(Programa::class);
	}

	public function registro_inasistencia()
	{
		return $this->hasMany(RegistroInasistencia::class);
	}
}
