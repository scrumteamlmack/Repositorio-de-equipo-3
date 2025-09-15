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
        Schema::create('guarda_seguridad', function (Blueprint $table) {
            $table->integer('Usuario_id_usuario')->primary()->comment('Clave primaria y foránea. Usuario que cumple funciones de guarda de seguridad.
');
            $table->enum('turno', ['Mañana', 'Tarde', 'Noche'])->comment('Turno del guarda(ej. mañana, tarde, etc...)');
            $table->date('fecha_ingreso')->comment('Fecha de ingreso laboral del guarda.
');
            $table->enum('estado', ['Activo', 'Inactivo'])->comment('Estado laboral del guarda (activo/inactivo).

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('guarda_seguridad');
    }
};
