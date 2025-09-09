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
        Schema::table('instructor', function (Blueprint $table) {
            $table->foreign(['Usuario_id_usuario'], 'instructor_ibfk_1')->references(['id_usuario'])->on('usuario')->onUpdate('restrict')->onDelete('restrict');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('instructor', function (Blueprint $table) {
            $table->dropForeign('instructor_ibfk_1');
        });
    }
};
