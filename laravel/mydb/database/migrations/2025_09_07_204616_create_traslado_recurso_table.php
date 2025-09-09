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
        Schema::create('traslado_recurso', function (Blueprint $table) {
            $table->integer('id_traslado', true)->comment('Clave primaria. Identificador del traslado.
');
            $table->integer('recurso_id')->index('recurso_id')->comment('Recurso trasladado.
');
            $table->integer('ambiente_origen')->index('ambiente_origen')->comment('Ambiente de origen.
');
            $table->integer('ambiente_destino')->comment('Ambiente de destino.
');
            $table->dateTime('fecha_traslado')->comment('Fecha del traslado.
');
            $table->text('observacion')->nullable()->comment('Observaciones del traslado.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('traslado_recurso');
    }
};
