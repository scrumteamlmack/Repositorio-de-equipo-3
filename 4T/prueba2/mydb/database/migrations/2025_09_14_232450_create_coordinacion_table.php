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
        Schema::create('coordinacion', function (Blueprint $table) {
            $table->integer('id_coordinacion', true)->comment('Clave primaria. Identificador de la coordinación.
');
            $table->string('nombre_coordinacion', 45)->comment('Nombre de la coordinación (ej. tecnologia e innovacion).
');
            $table->string('correo_coordinacion', 30)->comment('Correo electrónico institucional de la coordinación.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('coordinacion');
    }
};
