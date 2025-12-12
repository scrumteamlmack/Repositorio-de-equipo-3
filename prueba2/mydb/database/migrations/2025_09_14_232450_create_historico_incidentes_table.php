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
        Schema::create('historico_incidentes', function (Blueprint $table) {
            $table->integer('id_historico', true)->comment('Clave primaria. Identificador del historial del incidente.
');
            $table->integer('incidente_id')->index('incidente_id')->comment('Llave foránea. Incidente asociado.
');
            $table->integer('ambiente_id')->index('ambiente_id')->comment('Llave foránea. Ambiente en donde ocurrió.
');
            $table->integer('tipo_incidente_id')->index('tipo_incidente_id')->comment('Llave foránea. Tipo de incidente registrado.
');
            $table->text('descripcion')->nullable()->comment('Descripción de los hechos o seguimiento.
');
            $table->dateTime('fecha_registro')->comment('Fecha del registro en el historial.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('historico_incidentes');
    }
};
