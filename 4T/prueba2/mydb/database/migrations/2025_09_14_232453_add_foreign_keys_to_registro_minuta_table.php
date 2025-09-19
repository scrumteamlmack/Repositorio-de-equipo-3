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
        Schema::table('registro_minuta', function (Blueprint $table) {
            $table->foreign(['ambiente_id'], 'registro_minuta_ibfk_1')->references(['id_ambiente'])->on('ambiente')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['guarda_seguridad_Usuario_id_usuario'], 'registro_minuta_ibfk_2')->references(['Usuario_id_usuario'])->on('guarda_seguridad')->onUpdate('no action')->onDelete('no action');
            $table->foreign(['responsable_id'], 'registro_minuta_ibfk_3')->references(['Usuario_id_usuario'])->on('instructor')->onUpdate('no action')->onDelete('no action');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('registro_minuta', function (Blueprint $table) {
            $table->dropForeign('registro_minuta_ibfk_1');
            $table->dropForeign('registro_minuta_ibfk_2');
            $table->dropForeign('registro_minuta_ibfk_3');
        });
    }
};
