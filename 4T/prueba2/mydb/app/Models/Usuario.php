<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;

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
class Usuario extends Authenticatable
{
    use Notifiable;

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

    protected $hidden = [
        'contraseña',
    ];

    // 👇 importante: Laravel usará este campo como contraseña
    public function getAuthPassword()
    {
        return $this->contraseña;
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
