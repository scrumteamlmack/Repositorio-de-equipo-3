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
        Schema::table('historico_incidentes', function (Blueprint $table) {
            $table->foreign(['incidente_id'], 'historico_incidentes_ibfk_1')->references(['id_incidente'])->on('registro_incidente')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['ambiente_id'], 'historico_incidentes_ibfk_2')->references(['id_ambiente'])->on('ambiente')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['tipo_incidente_id'], 'historico_incidentes_ibfk_3')->references(['id_tipo_inc'])->on('tipo_incidente')->onUpdate('restrict')->onDelete('restrict');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('historico_incidentes', function (Blueprint $table) {
            $table->dropForeign('historico_incidentes_ibfk_1');
            $table->dropForeign('historico_incidentes_ibfk_2');
            $table->dropForeign('historico_incidentes_ibfk_3');
        });
    }
};
