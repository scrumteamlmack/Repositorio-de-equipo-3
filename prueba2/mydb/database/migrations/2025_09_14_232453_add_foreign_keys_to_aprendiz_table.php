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
        Schema::table('aprendiz', function (Blueprint $table) {
            $table->foreign(['programas_id_programas'], 'aprendiz_ibfk_1')->references(['id_programas'])->on('programas')->onUpdate('no action')->onDelete('no action');
            $table->foreign(['Usuario_id_usuario'], 'aprendiz_ibfk_2')->references(['id_usuario'])->on('usuario')->onUpdate('no action')->onDelete('no action');
            $table->foreign(['ficha_idficha'], 'aprendiz_ibfk_3')->references(['idficha'])->on('ficha')->onUpdate('no action')->onDelete('no action');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('aprendiz', function (Blueprint $table) {
            $table->dropForeign('aprendiz_ibfk_1');
            $table->dropForeign('aprendiz_ibfk_2');
            $table->dropForeign('aprendiz_ibfk_3');
        });
    }
};
