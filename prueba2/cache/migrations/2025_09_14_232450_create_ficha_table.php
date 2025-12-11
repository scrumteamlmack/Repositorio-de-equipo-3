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
            $table->id('idficha'); // PK autoincremental
            $table->mediumInteger('Num_ficha');

            // FK con instructor
            $table->unsignedBigInteger('instructor_Usuario_id_usuario')->nullable();
            $table->foreign('instructor_Usuario_id_usuario')
                ->references('Usuario_id_usuario')->on('instructor')
                ->onDelete('set null');

            // FK con programas
            $table->unsignedBigInteger('programas_id_programas')->nullable();
            $table->foreign('programas_id_programas')
                ->references('id_programas')->on('programas')
                ->onDelete('set null');

            // FK con modalidad
            $table->unsignedBigInteger('modalidad_id_modalidad')->nullable();
            $table->foreign('modalidad_id_modalidad')
                ->references('id_modalidad')->on('modalidad')
                ->onDelete('set null');

            $table->timestamps(); // opcional, para created_at y updated_at
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
