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
        Schema::create('instructor', function (Blueprint $table) {
            $table->integer('Usuario_id_usuario')->primary()->comment('Llave primaria y foránea. Usuario que actúa como instructor.
');
            $table->string('email', 100)->comment('Correo electrónico institucional.
');
            $table->string('telefono', 20)->comment('Teléfono de contacto.
');
            $table->string('coordinacion', 100)->comment('Coordinación a la que pertenece.
');
            $table->enum('estado', ['Activo', 'Inactivo'])->comment('Estado laboral (activo, inactivo).
');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('instructor');
    }
};
