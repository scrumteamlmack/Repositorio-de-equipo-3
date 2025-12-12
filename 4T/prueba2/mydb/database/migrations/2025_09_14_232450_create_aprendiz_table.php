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
        Schema::create('aprendiz', function (Blueprint $table) {
            $table->integer('Usuario_id_usuario')->primary()->comment('Llave primaria y foránea. Identificador del aprendiz (usuario base).
');
            $table->integer('programas_id_programas')->index('programas_id_programas')->comment('Llave foránea. Programa de formación del aprendiz.
');
            $table->integer('ficha_idficha')->index('ficha_idficha');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('aprendiz');
    }
};
