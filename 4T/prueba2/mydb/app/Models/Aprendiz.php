<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Aprendiz
 * 
 * @property int $Usuario_id_usuario
 * @property int $programas_id_programas
 * @property int $ficha_idficha
 * 
 * @property Programa $programa
 * @property Usuario $usuario
 * @property Ficha $ficha
 * @property Collection|RegistroInasistencia[] $registro_inasistencia
 *
 * @package App\Models
 */
class Aprendiz extends Model
{
	protected $table = 'aprendiz';
	protected $primaryKey = 'Usuario_id_usuario';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'Usuario_id_usuario' => 'int',
		'programas_id_programas' => 'int',
		'ficha_idficha' => 'int'
	];

	protected $fillable = [
		'programas_id_programas',
		'ficha_idficha'
	];

	public function programa()
	{
		return $this->belongsTo(Programa::class, 'programas_id_programas');
	}

	public function usuario()
	{
		return $this->belongsTo(Usuario::class, 'Usuario_id_usuario');
	}

	public function ficha()
	{
		return $this->belongsTo(Ficha::class, 'ficha_idficha');
	}

	public function registro_inasistencia()
	{
		return $this->hasMany(RegistroInasistencia::class, 'aprendiz_Usuario_id_usuario');
	}
}
