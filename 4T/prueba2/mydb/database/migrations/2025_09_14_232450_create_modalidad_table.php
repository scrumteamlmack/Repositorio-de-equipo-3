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
        Schema::create('modalidad', function (Blueprint $table) {
            $table->integer('id_modalidad')->primary()->comment('Clave primaria. Identificador de la modalidad.
');
            $table->enum('nombre_modalidad', ['Presencial', 'sincronica'])->comment('Nombre de la modalidad (presencial, sincronica).

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('modalidad');
    }
};
