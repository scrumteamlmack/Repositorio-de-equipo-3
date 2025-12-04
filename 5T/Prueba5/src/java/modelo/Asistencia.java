package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Asistencia implements Serializable {

    private int idAsistencia;
    private int aprendizUsuarioId;
    private int instructorUsuarioId;
    private int jornadaId;
    private LocalDate fecha;
    private String estado;

    public int getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public int getAprendizUsuarioId() {
        return aprendizUsuarioId;
    }

    public void setAprendizUsuarioId(int aprendizUsuarioId) {
        this.aprendizUsuarioId = aprendizUsuarioId;
    }

    public int getInstructorUsuarioId() {
        return instructorUsuarioId;
    }

    public void setInstructorUsuarioId(int instructorUsuarioId) {
        this.instructorUsuarioId = instructorUsuarioId;
    }

    public int getJornadaId() {
        return jornadaId;
    }

    public void setJornadaId(int jornadaId) {
        this.jornadaId = jornadaId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

