<?php

/**
 * Created by Reliese Model.
 */

namespace App\Models;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;

/**
 * Class Ficha
 * 
 * @property int $idficha
 * @property int $Num_ficha
 * @property int $instructor_Usuario_id_usuario
 * 
 * @property Instructor $instructor
 * @property Collection|Aprendiz[] $aprendizs
 *
 * @package App\Models
 */
class Ficha extends Model
{
	protected $table = 'ficha';
	protected $primaryKey = 'idficha';
	public $incrementing = false;
	public $timestamps = false;

	protected $casts = [
		'idficha' => 'int',
		'Num_ficha' => 'int',
		'instructor_Usuario_id_usuario' => 'int'
	];

	protected $fillable = [
		'Num_ficha',
		'instructor_Usuario_id_usuario'
	];

	public function instructor()
	{
		return $this->belongsTo(Instructor::class, 'instructor_Usuario_id_usuario');
	}

	public function aprendizs()
	{
		return $this->hasMany(Aprendiz::class, 'ficha_idficha');
	}
}
