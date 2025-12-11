<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Recurso
 * 
 * @property int $id_recurso
 * @property string $serial_recurso
 * @property int $num_recurso
 * @property string $nombre_recurso
 * @property int $tipo_recurso
 * @property string|null $estado
 * @property string|null $observacion
 * @property int $ambiente_id
 * 
 * @property Ambiente $ambiente
 * @property Collection|TrasladoRecurso[] $traslado_recursos
 *
 * @package App\Models
 */
class Recurso extends Model
{
	protected $table = 'recursos';
	protected $primaryKey = 'id_recurso';
	public $timestamps = false;

	protected $casts = [
		'num_recurso' => 'int',
		'tipo_recurso' => 'int',
		'ambiente_id' => 'int'
	];

	protected $fillable = [
		'serial_recurso',
		'num_recurso',
		'nombre_recurso',
		'tipo_recurso',
		'estado',
		'observacion',
		'ambiente_id'
	];

	public function tipo_recurso()
	{
		return $this->belongsTo(TipoRecurso::class, 'tipo_recurso');
	}

	public function ambiente()
	{
		return $this->belongsTo(Ambiente::class);
	}

	public function traslado_recursos()
	{
		return $this->hasMany(TrasladoRecurso::class);
	}
}
