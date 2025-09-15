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
        Schema::create('ambiente', function (Blueprint $table) {
            $table->integer('id_ambiente')->primary()->comment('Clave primaria. Identificador único del ambiente.
');
            $table->smallInteger('num_ambiente')->comment('Número o código del ambiente físico.
');
            $table->smallInteger('capacidad')->comment('Cantidad de personas que pueden estar en un ambiente');
            $table->string('tipo_ambiente', 45)->comment('Tipo del ambiente (auditorio, sala, aula, etc.).
');
            $table->string('estado', 30)->comment('Estado actual del ambiente (disponible, ocupado, mantenimiento, etc.).
');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('ambiente');
    }
};
