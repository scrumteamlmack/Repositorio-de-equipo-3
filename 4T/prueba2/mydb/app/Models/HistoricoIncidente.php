<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Model;

/**
 * Class HistoricoIncidente
 * 
 * @property int $id_historico
 * @property int $incidente_id
 * @property int $ambiente_id
 * @property int $tipo_incidente_id
 * @property string|null $descripcion
 * @property Carbon $fecha_registro
 * 
 * @property RegistroIncidente $registro_incidente
 * @property Ambiente $ambiente
 * @property TipoIncidente $tipo_incidente
 *
 * @package App\Models
 */
class HistoricoIncidente extends Model
{
	protected $table = 'historico_incidentes';
	protected $primaryKey = 'id_historico';
	public $timestamps = false;

	protected $casts = [
		'incidente_id' => 'int',
		'ambiente_id' => 'int',
		'tipo_incidente_id' => 'int',
		'fecha_registro' => 'datetime'
	];

	protected $fillable = [
		'incidente_id',
		'ambiente_id',
		'tipo_incidente_id',
		'descripcion',
		'fecha_registro'
	];

	public function registro_incidente()
	{
		return $this->belongsTo(RegistroIncidente::class, 'incidente_id');
	}

	public function ambiente()
	{
		return $this->belongsTo(Ambiente::class);
	}

	public function tipo_incidente()
	{
		return $this->belongsTo(TipoIncidente::class);
	}
}
