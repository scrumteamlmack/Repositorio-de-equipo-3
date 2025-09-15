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
        Schema::create('user_rol', function (Blueprint $table) {
            $table->integer('id_user_rol', true)->comment('Clave primaria. Identificador del registro.
');
            $table->integer('id_usuario')->index('id_usuario')->comment('Usuario asociado.
');
            $table->integer('id_rol')->index('id_rol')->comment('Rol asignado al usuario.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('user_rol');
    }
};
