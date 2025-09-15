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
        Schema::create('tipo_recurso', function (Blueprint $table) {
            $table->integer('id_tipo_recurso')->primary()->comment('Clave primaria. Identificador del tipo de recurso.
');
            $table->string('recurso_tipo', 45)->comment('Nombre del tipo (ej. PC, proyector, aire, etc.).
');
            $table->string('descripcion_tipo', 60)->nullable()->comment('Descripción adicional.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('tipo_recurso');
    }
};
