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
        Schema::table('recursos', function (Blueprint $table) {
            $table->foreign(['tipo_recurso'], 'recursos_ibfk_1')->references(['id_tipo_recurso'])->on('tipo_recurso')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['ambiente_id'], 'recursos_ibfk_2')->references(['id_ambiente'])->on('ambiente')->onUpdate('restrict')->onDelete('restrict');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('recursos', function (Blueprint $table) {
            $table->dropForeign('recursos_ibfk_1');
            $table->dropForeign('recursos_ibfk_2');
        });
    }
};
