<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Rol
 * 
 * @property int $id_rol
 * @property string $nombre_rol
 * 
 * @property Collection|UserRol[] $user_rols
 *
 * @package App\Models
 */
class Rol extends Model
{
	protected $table = 'rol';
	protected $primaryKey = 'id_rol';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'id_rol' => 'int'
	];

	protected $fillable = [
		'nombre_rol'
	];

	public function user_rols()
	{
		return $this->hasMany(UserRol::class, 'id_rol');
	}
}
