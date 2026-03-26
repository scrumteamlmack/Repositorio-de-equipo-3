<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('programas', function (Blueprint $table) {
            $table->integer('id_programas')->primary()->comment('Clave primaria. Identificador del programa académico.
');
            $table->string('nombre_programa', 50)->comment('Nombre del programa.
');
            $table->string('nivel_formacion', 30)->comment('Nivel de formación (tecnólogo, técnico, etc.).
');
            $table->string('duracion', 50)->comment('Duración estimada del programa.
');
            $table->integer('jornada_id')->index('jornada_id')->comment('Jornada asignada.
');
            $table->integer('modalidad_id')->index('modalidad_id')->comment('Modalidad del programa.
');
            $table->integer('coordinacion_id')->index('coordinacion_id')->comment('Coordinación responsable del programa.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('programas');
    }
};
