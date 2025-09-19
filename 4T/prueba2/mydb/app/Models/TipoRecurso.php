<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class TipoRecurso
 * 
 * @property int $id_tipo_recurso
 * @property string $recurso_tipo
 * @property string|null $descripcion_tipo
 * 
 * @property Collection|Recurso[] $recursos
 *
 * @package App\Models
 */
class TipoRecurso extends Model
{
	protected $table = 'tipo_recurso';
	protected $primaryKey = 'id_tipo_recurso';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'id_tipo_recurso' => 'int'
	];

	protected $fillable = [
		'recurso_tipo',
		'descripcion_tipo'
	];

	public function recursos()
	{
		return $this->hasMany(Recurso::class, 'tipo_recurso');
	}
}
