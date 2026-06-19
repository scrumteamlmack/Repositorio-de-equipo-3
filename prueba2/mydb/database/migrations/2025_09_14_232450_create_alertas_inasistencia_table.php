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
        Schema::create('alertas_inasistencia', function (Blueprint $table) {
            $table->integer('id_alerta', true)->comment('Clave primaria. Identificador de la alerta.
');
            $table->integer('aprendiz_id')->comment('Llave foránea. Relaciona la alerta con el aprendiz.
');
            $table->integer('cantidad_fallas')->comment('Número acumulado de inasistencias.
');
            $table->dateTime('fecha_alerta')->comment('Fecha de generación de la alerta.
');
            $table->text('mensaje')->comment('Descripción o detalle de la alerta.
');
            $table->integer('coordinacion_id')->index('coordinacion_id')->comment('Coordinación que recibe o emite la alerta.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('alertas_inasistencia');
    }
};
