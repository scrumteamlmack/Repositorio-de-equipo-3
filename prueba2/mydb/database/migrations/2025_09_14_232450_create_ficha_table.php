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
        Schema::create('ficha', function (Blueprint $table) {
            $table->integer('idficha')->primary();
            $table->mediumInteger('Num_ficha');
            $table->integer('instructor_Usuario_id_usuario')->index('instructor_usuario_id_usuario');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('ficha');
    }
};
