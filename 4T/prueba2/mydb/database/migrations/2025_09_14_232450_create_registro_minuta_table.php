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
        Schema::create('registro_minuta', function (Blueprint $table) {
            $table->integer('id_minuta', true)->comment('Clave primaria. Identificador del registro de minuta.
');
            $table->dateTime('fecha_hora_recibo')->comment('Fecha y hora de recibo del ambiente.
');
            $table->dateTime('fecha_hora_entrega')->comment('Fecha y hora de entrega.
');
            $table->text('novedad')->nullable()->comment('Novedad o eventualidad ocurrida.
');
            $table->text('descripcion_min')->nullable()->comment('Observaciones generales.
');
            $table->text('estado')->comment('Estado general del ambiente al momento.
');
            $table->integer('ambiente_id')->index('ambiente_id')->comment('Ambiente relacionado.
');
            $table->integer('guarda_seguridad_Usuario_id_usuario')->index('guarda_seguridad_usuario_id_usuario')->comment('Guarda que recibió o entregó.

');
            $table->integer('responsable_id')->index('responsable_id');
            $table->string('registro_minutacol', 45)->nullable();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('registro_minuta');
    }
};
