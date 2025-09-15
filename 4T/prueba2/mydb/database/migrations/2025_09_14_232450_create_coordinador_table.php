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
        Schema::create('coordinador', function (Blueprint $table) {
            $table->integer('Usuario_id_usuario')->primary()->comment('Llave primaria y foránea. Usuario que tiene el rol de coordinador.
');
            $table->integer('coordinacion_id_coordinacion')->index('coordinacion_id_coordinacion')->comment('Llave foránea. Relaciona con la coordinación que lidera.

');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('coordinador');
    }
};
