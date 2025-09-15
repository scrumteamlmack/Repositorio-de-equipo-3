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
        Schema::create('jornada', function (Blueprint $table) {
            $table->integer('id_jornada')->primary()->comment('Clave primaria. Identificador de la jornada.
');
            $table->enum('nombre_jornada', ['Mañana', 'Tarde', 'Noche', 'Madrugada'])->comment('Nombre de la jornada (mañana, tarde, noche).

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('jornada');
    }
};
