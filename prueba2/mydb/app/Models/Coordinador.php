<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * Class Coordinador
 * 
 * @property int $Usuario_id_usuario
 * @property int $coordinacion_id_coordinacion
 * 
 * @property Usuario $usuario
 * @property Coordinacion $coordinacion
 *
 * @package App\Models
 */
class Coordinador extends Model
{
	protected $table = 'coordinador';
	protected $primaryKey = 'Usuario_id_usuario';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'Usuario_id_usuario' => 'int',
		'coordinacion_id_coordinacion' => 'int'
	];

	protected $fillable = [
		'coordinacion_id_coordinacion'
	];

	public function usuario()
	{
		return $this->belongsTo(Usuario::class, 'Usuario_id_usuario');
	}

	public function coordinacion()
	{
		return $this->belongsTo(Coordinacion::class, 'coordinacion_id_coordinacion');
	}
}
