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
    
<<<<<<< HEAD

=======
    // Campos adicionales para mostrar nombres en la vista
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
    private String aprendizNombre;
    private String instructorNombre;
    private String jornadaNombre;

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

    public String getInstructorNombre() {
        return instructorNombre;
    }

    public void setInstructorNombre(String instructorNombre) {
        this.instructorNombre = instructorNombre;
    }

    public String getJornadaNombre() {
        return jornadaNombre;
    }

    public void setJornadaNombre(String jornadaNombre) {
        this.jornadaNombre = jornadaNombre;
    }
    
    public String getAprendizNombre() {
        return aprendizNombre;
    }
    
    public void setAprendizNombre(String aprendizNombre) {
        this.aprendizNombre = aprendizNombre;
    }
}

