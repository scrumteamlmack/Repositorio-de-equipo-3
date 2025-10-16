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
public $timestamps = false;
	public $incrementing = false;

protected $casts = [
    'idficha' => 'int',
    'Num_ficha' => 'int',
    'instructor_Usuario_id_usuario' => 'int',
    'programas_id_programas' => 'int',
    'modalidad_id' => 'int',
];



protected $fillable = [
    'Num_ficha',
    'programas_id_programas',
    'modalidad_id',
    'instructor_Usuario_id_usuario',
];


	 public function instructor()
    {
        return $this->belongsTo(Instructor::class, 'instructor_Usuario_id_usuario', 'Usuario_id_usuario');
    }

  public function aprendices()
{
    return $this->hasMany(Aprendiz::class, 'ficha_idficha', 'idficha');
}

public function programa()
{
    return $this->belongsTo(Programa::class, 'programas_id_programas', 'id_programas');
}

public function modalidad()
{
    return $this->belongsTo(Modalidad::class, 'modalidad_id', 'id_modalidad');
}
}
