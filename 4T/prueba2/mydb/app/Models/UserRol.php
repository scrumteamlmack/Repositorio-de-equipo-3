<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * Class UserRol
 * 
 * @property int $id_user_rol
 * @property int $id_usuario
 * @property int $id_rol
 * 
 * @property Usuario $usuario
 * @property Rol $rol
 *
 * @package App\Models
 */
class UserRol extends Model
{
	protected $table = 'user_rol';
	protected $primaryKey = 'id_user_rol';
	public $timestamps = false;

	protected $casts = [
		'id_usuario' => 'int',
		'id_rol' => 'int'
	];

	protected $fillable = [
		'id_usuario',
		'id_rol'
	];

	public function usuario()
	{
		return $this->belongsTo(Usuario::class, 'id_usuario');
	}

	public function rol()
	{
		return $this->belongsTo(Rol::class, 'id_rol');
	}
}
