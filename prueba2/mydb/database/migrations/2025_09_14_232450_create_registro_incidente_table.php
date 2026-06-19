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
        Schema::create('registro_incidente', function (Blueprint $table) {
            $table->integer('id_incidente', true)->comment('Clave primaria. Identificador del incidente.

');
            $table->text('descripcion')->nullable()->comment('Descripción general del incidente.
');
            $table->date('fecha_incidente')->comment('Fecha en que ocurrió.
');
            $table->time('hora_incidente')->comment('Hora en que ocurrio.');
            $table->integer('ambiente_id')->index('ambiente_id')->comment('Ambiente donde sucedió.
');
            $table->integer('tipo_inc_id')->index('tipo_inc_id')->comment('Tipo de incidente.
');
            $table->integer('usuario_id_usuario')->index('usuario_id_usuario');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('registro_incidente');
    }
};
