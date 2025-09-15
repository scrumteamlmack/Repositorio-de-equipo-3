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
        Schema::table('traslado_recurso', function (Blueprint $table) {
            $table->foreign(['recurso_id'], 'traslado_recurso_ibfk_1')->references(['id_recurso'])->on('recursos')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['ambiente_origen'], 'traslado_recurso_ibfk_2')->references(['id_ambiente'])->on('ambiente')->onUpdate('restrict')->onDelete('restrict');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('traslado_recurso', function (Blueprint $table) {
            $table->dropForeign('traslado_recurso_ibfk_1');
            $table->dropForeign('traslado_recurso_ibfk_2');
        });
    }
};
