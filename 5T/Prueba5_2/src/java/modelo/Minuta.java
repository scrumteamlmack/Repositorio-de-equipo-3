package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Minuta implements Serializable {

    private int idMinuta;
    private int ambienteId;
    private int guardaId;
    private int responsableId;
    private LocalDateTime fechaRecibo;
    private LocalDateTime fechaEntrega;
    private String novedad;
    private String descripcion;
    private String estado;
    

    private String ambienteNombre;
    private String guardaNombre;
    private String responsableNombre;

    public int getIdMinuta() {
        return idMinuta;
    }

    public void setIdMinuta(int idMinuta) {
        this.idMinuta = idMinuta;
    }

    public int getAmbienteId() {
        return ambienteId;
    }

    public void setAmbienteId(int ambienteId) {
        this.ambienteId = ambienteId;
    }

    public int getGuardaId() {
        return guardaId;
    }

    public void setGuardaId(int guardaId) {
        this.guardaId = guardaId;
    }

    public int getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(int responsableId) {
        this.responsableId = responsableId;
    }

    public LocalDateTime getFechaRecibo() {
        return fechaRecibo;
    }

    public void setFechaRecibo(LocalDateTime fechaRecibo) {
        this.fechaRecibo = fechaRecibo;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getNovedad() {
        return novedad;
    }

    public void setNovedad(String novedad) {
        this.novedad = novedad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAmbienteNombre() {
        return ambienteNombre;
    }

    public void setAmbienteNombre(String ambienteNombre) {
        this.ambienteNombre = ambienteNombre;
    }

    public String getGuardaNombre() {
        return guardaNombre;
    }

    public void setGuardaNombre(String guardaNombre) {
        this.guardaNombre = guardaNombre;
    }

    public String getResponsableNombre() {
        return responsableNombre;
    }

    public void setResponsableNombre(String responsableNombre) {
        this.responsableNombre = responsableNombre;
    }
}

