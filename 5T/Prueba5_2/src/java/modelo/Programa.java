package modelo;

import java.io.Serializable;

public class Programa implements Serializable {

    private int idProgramas;
    private String nombrePrograma;
    private String nivelFormacion;
    private String duracion;
    private int jornadaId;
    private int modalidadId;
    private int coordinacionId;


    public int getIdProgramas() {
        return idProgramas;
    }

    public void setIdProgramas(int idProgramas) {
        this.idProgramas = idProgramas;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public String getNivelFormacion() {
        return nivelFormacion;
    }

    public void setNivelFormacion(String nivelFormacion) {
        this.nivelFormacion = nivelFormacion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getJornadaId() {
        return jornadaId;
    }

    public void setJornadaId(int jornadaId) {
        this.jornadaId = jornadaId;
    }

    public int getModalidadId() {
        return modalidadId;
    }

    public void setModalidadId(int modalidadId) {
        this.modalidadId = modalidadId;
    }

    public int getCoordinacionId() {
        return coordinacionId;
    }

    public void setCoordinacionId(int coordinacionId) {
        this.coordinacionId = coordinacionId;
    }
}
