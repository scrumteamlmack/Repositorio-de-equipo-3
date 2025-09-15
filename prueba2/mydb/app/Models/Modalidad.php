<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Modalidad
 * 
 * @property int $id_modalidad
 * @property string $nombre_modalidad
 * 
 * @property Collection|Programa[] $programas
 *
 * @package App\Models
 */
class Modalidad extends Model
{
	protected $table = 'modalidad';
	protected $primaryKey = 'id_modalidad';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'id_modalidad' => 'int'
	];

	protected $fillable = [
		'nombre_modalidad'
	];

	public function programas()
	{
		return $this->hasMany(Programa::class);
	}
}
