<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class GuardaSeguridad
 * 
 * @property int $Usuario_id_usuario
 * @property string $turno
 * @property Carbon $fecha_ingreso
 * @property string $estado
 * 
 * @property Usuario $usuario
 * @property Collection|RegistroMinutum[] $registro_minuta
 *
 * @package App\Models
 */
class GuardaSeguridad extends Model
{
	protected $table = 'guarda_seguridad';
	protected $primaryKey = 'Usuario_id_usuario';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'Usuario_id_usuario' => 'int',
		'fecha_ingreso' => 'datetime'
	];

	protected $fillable = [
		'turno',
		'fecha_ingreso',
		'estado'
	];

	public function usuario()
	{
		return $this->belongsTo(Usuario::class, 'Usuario_id_usuario');
	}

	public function registro_minuta()
	{
		return $this->hasMany(RegistroMinutum::class, 'guarda_seguridad_Usuario_id_usuario');
	}
}
