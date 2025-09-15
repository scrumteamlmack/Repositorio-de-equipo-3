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
        Schema::table('registro_incidente', function (Blueprint $table) {
            $table->foreign(['ambiente_id'], 'registro_incidente_ibfk_1')->references(['id_ambiente'])->on('ambiente')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['tipo_inc_id'], 'registro_incidente_ibfk_2')->references(['id_tipo_inc'])->on('tipo_incidente')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['usuario_id_usuario'], 'registro_incidente_ibfk_3')->references(['id_usuario'])->on('usuario')->onUpdate('no action')->onDelete('no action');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('registro_incidente', function (Blueprint $table) {
            $table->dropForeign('registro_incidente_ibfk_1');
            $table->dropForeign('registro_incidente_ibfk_2');
            $table->dropForeign('registro_incidente_ibfk_3');
        });
    }
};
