<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Ambiente
 * 
 * @property int $id_ambiente
 * @property int $num_ambiente
 * @property int $capacidad
 * @property string $tipo_ambiente
 * @property string $estado
 * 
 * @property Collection|HistoricoIncidente[] $historico_incidentes
 * @property Collection|Recurso[] $recursos
 * @property Collection|RegistroIncidente[] $registro_incidentes
 * @property Collection|RegistroMinutum[] $registro_minuta
 * @property Collection|TrasladoRecurso[] $traslado_recursos
 *
 * @package App\Models
 */
class Ambiente extends Model
{
	protected $table = 'ambiente';
	protected $primaryKey = 'id_ambiente';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'id_ambiente' => 'int',
		'num_ambiente' => 'int',
		'capacidad' => 'int'
	];

	protected $fillable = [
		'num_ambiente',
		'capacidad',
		'tipo_ambiente',
		'estado'
	];

	public function historico_incidentes()
	{
		return $this->hasMany(HistoricoIncidente::class);
	}

	public function recursos()
	{
		return $this->hasMany(Recurso::class);
	}

	public function registro_incidentes()
	{
		return $this->hasMany(RegistroIncidente::class);
	}

	public function registro_minuta()
	{
		return $this->hasMany(RegistroMinutum::class);
	}

	public function traslado_recursos()
	{
		return $this->hasMany(TrasladoRecurso::class, 'ambiente_origen');
	}
}
