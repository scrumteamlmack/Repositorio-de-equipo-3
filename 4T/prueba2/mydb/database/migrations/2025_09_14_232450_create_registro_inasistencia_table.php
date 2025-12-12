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
        Schema::create('registro_inasistencia', function (Blueprint $table) {
            $table->integer('id_inasistencia', true)->comment('Clave primaria. Identificador del registro.
');
            $table->date('fecha_inasistencia')->comment('Fecha del registro de asistencia.
');
            $table->enum('estado_inasistencia', ['S', 'R', 'N'])->comment('Estado: S (asistió), R (retraso), N (no asistio).
');
            $table->integer('jornada_id')->index('jornada_id')->comment('Jornada del aprendiz.
');
            $table->integer('aprendiz_Usuario_id_usuario')->index('aprendiz_usuario_id_usuario')->comment('Llave foránea al aprendiz.

');
            $table->integer('instructor_Usuario_id_usuario')->index('instructor_usuario_id_usuario');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('registro_inasistencia');
    }
};
