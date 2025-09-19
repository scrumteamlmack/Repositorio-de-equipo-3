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
        Schema::table('coordinador', function (Blueprint $table) {
            $table->foreign(['Usuario_id_usuario'], 'coordinador_ibfk_1')->references(['id_usuario'])->on('usuario')->onUpdate('no action')->onDelete('no action');
            $table->foreign(['coordinacion_id_coordinacion'], 'coordinador_ibfk_2')->references(['id_coordinacion'])->on('coordinacion')->onUpdate('no action')->onDelete('no action');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('coordinador', function (Blueprint $table) {
            $table->dropForeign('coordinador_ibfk_1');
            $table->dropForeign('coordinador_ibfk_2');
        });
    }
};
