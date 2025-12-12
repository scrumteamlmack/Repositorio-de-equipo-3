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
        Schema::create('recursos', function (Blueprint $table) {
            $table->integer('id_recurso', true)->comment('Clave primaria. Identificador del recurso.
');
            $table->string('serial_recurso', 100)->comment('Serial físico o interno del recurso.
');
            $table->tinyInteger('num_recurso')->comment('Nombre del recurso en el ambiente.
');
            $table->string('nombre_recurso', 60)->comment('Nombre del recurso.
');
            $table->integer('tipo_recurso')->index('tipo_recurso')->comment('Llave foránea. Tipo de recurso.
');
            $table->enum('estado', ['Disponible', 'En mantenimiento', 'Dañado'])->nullable()->comment('Estado del recurso (operativo, dañado, en mantenimiento).
');
            $table->text('observacion')->nullable()->comment('Observacion hacia algun recurso.');
            $table->integer('ambiente_id')->index('ambiente_id')->comment('Llave foranea, Ambiente al que pertenece.');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('recursos');
    }
};
