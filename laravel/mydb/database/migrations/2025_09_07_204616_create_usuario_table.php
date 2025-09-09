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
        Schema::create('usuario', function (Blueprint $table) {
            $table->integer('id_usuario', true)->comment('Clave primaria. Identificador del usuario.
');
            $table->string('p_nombre', 50)->comment('Primer nombre.
');
            $table->string('s_nombre', 50)->nullable()->comment('Segundo nombre.
');
            $table->string('p_apellido', 45)->comment('Primer apellido.
');
            $table->string('s_apellido', 45)->nullable()->comment('Segundo apellido.
');
            $table->enum('tipo_documento', ['CC', 'TI', 'CE', 'OTRO'])->comment('Tipo de documento.
');
            $table->integer('num_documento')->comment('Número de documento.
');
            $table->string('correo', 100)->comment('Correo institucional.
');
            $table->string('contraseña', 100)->comment('Contraseña cifrada.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('usuario');
    }
};
