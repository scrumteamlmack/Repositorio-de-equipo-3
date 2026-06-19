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
        Schema::table('ficha', function (Blueprint $table) {
            $table->foreign(['instructor_Usuario_id_usuario'], 'ficha_ibfk_1')->references(['Usuario_id_usuario'])->on('instructor')->onUpdate('no action')->onDelete('no action');
            $table->foreign(['programas_id_programas'], 'ficha_ibfk_2')->references(['id_programas'])->on('programas')->onUpdate('no action')->onDelete('no action');
            $table->foreign(['modalidad_id'], 'ficha_ibfk_3')->references(['id_modalidad'])->on('modalidad')->onUpdate('no action')->onDelete('no action');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('ficha', function (Blueprint $table) {
            $table->dropForeign('ficha_ibfk_1');
            $table->dropForeign('ficha_ibfk_2');
            $table->dropForeign('ficha_ibfk_3');
        });
    }
};
