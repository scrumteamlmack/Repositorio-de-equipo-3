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
        Schema::table('programas', function (Blueprint $table) {
            $table->foreign(['jornada_id'], 'programas_ibfk_1')->references(['id_jornada'])->on('jornada')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['modalidad_id'], 'programas_ibfk_2')->references(['id_modalidad'])->on('modalidad')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['coordinacion_id'], 'programas_ibfk_3')->references(['id_coordinacion'])->on('coordinacion')->onUpdate('restrict')->onDelete('restrict');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('programas', function (Blueprint $table) {
            $table->dropForeign('programas_ibfk_1');
            $table->dropForeign('programas_ibfk_2');
            $table->dropForeign('programas_ibfk_3');
        });
    }
};
