<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Usuario
 * 
 * @property int $id_usuario
 * @property string $p_nombre
 * @property string|null $s_nombre
 * @property string $p_apellido
 * @property string|null $s_apellido
 * @property string $tipo_documento
 * @property int $num_documento
 * @property string $correo
 * @property string $contraseña
 * 
 * @property Aprendiz|null $aprendiz
 * @property Coordinador|null $coordinador
 * @property GuardaSeguridad|null $guarda_seguridad
 * @property Instructor|null $instructor
 * @property Collection|RegistroIncidente[] $registro_incidentes
 * @property Collection|UserRol[] $user_rols
 *
 * @package App\Models
 */
class Usuario extends Model
{
	use HasFactory;
	protected $table = 'usuario';
	protected $primaryKey = 'id_usuario';
	public $timestamps = false;

	protected $casts = [
		'num_documento' => 'int'
	];

	protected $fillable = [
		'p_nombre',
		's_nombre',
		'p_apellido',
		's_apellido',
		'tipo_documento',
		'num_documento',
		'correo',
		'contraseña'
	];

	 public function roles()
    {
        return $this->belongsToMany(Rol::class, 'user_rol', 'id_usuario', 'id_rol');
    }

	public function aprendiz()
	{
		return $this->hasOne(Aprendiz::class, 'Usuario_id_usuario');
	}

	public function coordinador()
	{
		return $this->hasOne(Coordinador::class, 'Usuario_id_usuario');
	}

	public function guarda_seguridad()
	{
		return $this->hasOne(GuardaSeguridad::class, 'Usuario_id_usuario');
	}

	public function instructor()
	{
		return $this->hasOne(Instructor::class, 'Usuario_id_usuario');
	}

	public function registro_incidentes()
	{
		return $this->hasMany(RegistroIncidente::class, 'usuario_id_usuario');
	}

	public function user_rols()
	{
		return $this->hasMany(UserRol::class, 'id_usuario');
	}
}
