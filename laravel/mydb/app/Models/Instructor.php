<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Instructor
 * 
 * @property int $Usuario_id_usuario
 * @property string $email
 * @property string $telefono
 * @property string $coordinacion
 * @property string $estado
 * 
 * @property Usuario $usuario
 * @property Collection|Ficha[] $fichas
 * @property Collection|RegistroInasistencium[] $registro_inasistencia
 * @property Collection|RegistroMinutum[] $registro_minuta
 *
 * @package App\Models
 */
class Instructor extends Model
{
	protected $table = 'instructor';
	protected $primaryKey = 'Usuario_id_usuario';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'Usuario_id_usuario' => 'int'
	];

	protected $fillable = [
		'email',
		'telefono',
		'coordinacion',
		'estado'
	];

	public function usuario()
	{
		return $this->belongsTo(Usuario::class, 'Usuario_id_usuario');
	}

	public function fichas()
	{
		return $this->hasMany(Ficha::class, 'instructor_Usuario_id_usuario');
	}

	public function registro_inasistencia()
	{
		return $this->hasMany(RegistroInasistencium::class, 'instructor_Usuario_id_usuario');
	}

	public function registro_minuta()
	{
		return $this->hasMany(RegistroMinutum::class, 'responsable_id');
	}
}
