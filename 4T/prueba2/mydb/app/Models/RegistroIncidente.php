<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class RegistroIncidente
 * 
 * @property int $id_incidente
 * @property string|null $descripcion
 * @property Carbon $fecha_incidente
 * @property Carbon $hora_incidente
 * @property int $ambiente_id
 * @property int $tipo_inc_id
 * @property int $usuario_id_usuario
 * 
 * @property Ambiente $ambiente
 * @property TipoIncidente $tipo_incidente
 * @property Usuario $usuario
 * @property Collection|HistoricoIncidente[] $historico_incidentes
 *
 * @package App\Models
 */
class RegistroIncidente extends Model
{
	protected $table = 'registro_incidente';
	protected $primaryKey = 'id_incidente';
	public $timestamps = false;

	protected $casts = [
		'fecha_incidente' => 'datetime',
		'hora_incidente' => 'datetime',
		'ambiente_id' => 'int',
		'tipo_inc_id' => 'int',
		'usuario_id_usuario' => 'int'
	];

	protected $fillable = [
		'descripcion',
		'fecha_incidente',
		'hora_incidente',
		'ambiente_id',
		'tipo_inc_id',
		'usuario_id_usuario'
	];

	public function ambiente()
	{
		return $this->belongsTo(Ambiente::class);
	}

	public function tipo_incidente()
	{
		return $this->belongsTo(TipoIncidente::class, 'tipo_inc_id');
	}

	public function usuario()
	{
		return $this->belongsTo(Usuario::class, 'usuario_id_usuario');
	}

	public function historico_incidentes()
	{
		return $this->hasMany(HistoricoIncidente::class, 'incidente_id');
	}
}
