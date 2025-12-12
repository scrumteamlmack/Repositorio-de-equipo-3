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
        Schema::table('registro_inasistencia', function (Blueprint $table) {
            $table->foreign(['jornada_id'], 'registro_inasistencia_ibfk_1')->references(['id_jornada'])->on('jornada')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['aprendiz_Usuario_id_usuario'], 'registro_inasistencia_ibfk_2')->references(['Usuario_id_usuario'])->on('aprendiz')->onUpdate('no action')->onDelete('no action');
            $table->foreign(['instructor_Usuario_id_usuario'], 'registro_inasistencia_ibfk_3')->references(['Usuario_id_usuario'])->on('instructor')->onUpdate('no action')->onDelete('no action');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('registro_inasistencia', function (Blueprint $table) {
            $table->dropForeign('registro_inasistencia_ibfk_1');
            $table->dropForeign('registro_inasistencia_ibfk_2');
            $table->dropForeign('registro_inasistencia_ibfk_3');
        });
    }
};
