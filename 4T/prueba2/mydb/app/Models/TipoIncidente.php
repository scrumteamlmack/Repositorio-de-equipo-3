<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class TipoIncidente
 * 
 * @property int $id_tipo_inc
 * @property string $tipo_incidente
 * @property string $observacion_inc
 * 
 * @property Collection|HistoricoIncidente[] $historico_incidentes
 * @property Collection|RegistroIncidente[] $registro_incidentes
 *
 * @package App\Models
 */
class TipoIncidente extends Model
{
	protected $table = 'tipo_incidente';
	protected $primaryKey = 'id_tipo_inc';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'id_tipo_inc' => 'int'
	];

	protected $fillable = [
		'tipo_incidente',
		'observacion_inc'
	];

	public function historico_incidentes()
	{
		return $this->hasMany(HistoricoIncidente::class);
	}

	public function registro_incidentes()
	{
		return $this->hasMany(RegistroIncidente::class, 'tipo_inc_id');
	}
}
