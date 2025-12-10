package modelo;

import java.io.Serializable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Incidente implements Serializable {

    private int idIncidente;
    private int idAmbiente;
    private int idTipoIncidente;
    private int idReportador;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;

    public int getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(int idIncidente) {
        this.idIncidente = idIncidente;
    }

    public int getIdAmbiente() {
        return idAmbiente;
    }

    public void setIdAmbiente(int idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public int getIdTipoIncidente() {
        return idTipoIncidente;
    }

    public void setIdTipoIncidente(int idTipoIncidente) {
        this.idTipoIncidente = idTipoIncidente;
    }

    public int getIdReportador() {
        return idReportador;
    }

    public void setIdReportador(int idReportador) {
        this.idReportador = idReportador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}

