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
        Schema::create('tipo_incidente', function (Blueprint $table) {
            $table->integer('id_tipo_inc')->primary()->comment('Clave primaria. Identificador del tipo de incidente.
');
            $table->string('tipo_incidente', 45)->comment('Nombre del tipo.
');
            $table->text('observacion_inc')->comment('Observación adicional.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('tipo_incidente');
    }
};
