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
        Schema::table('user_rol', function (Blueprint $table) {
            $table->foreign(['id_usuario'], 'user_rol_ibfk_1')->references(['id_usuario'])->on('usuario')->onUpdate('restrict')->onDelete('restrict');
            $table->foreign(['id_rol'], 'user_rol_ibfk_2')->references(['id_rol'])->on('rol')->onUpdate('restrict')->onDelete('restrict');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('user_rol', function (Blueprint $table) {
            $table->dropForeign('user_rol_ibfk_1');
            $table->dropForeign('user_rol_ibfk_2');
        });
    }
};
