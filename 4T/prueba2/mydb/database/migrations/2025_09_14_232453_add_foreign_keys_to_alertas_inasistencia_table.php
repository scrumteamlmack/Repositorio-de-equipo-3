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
        Schema::table('alertas_inasistencia', function (Blueprint $table) {
            $table->foreign(['coordinacion_id'], 'alertas_inasistencia_ibfk_1')->references(['id_coordinacion'])->on('coordinacion')->onUpdate('restrict')->onDelete('restrict');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('alertas_inasistencia', function (Blueprint $table) {
            $table->dropForeign('alertas_inasistencia_ibfk_1');
        });
    }
};
