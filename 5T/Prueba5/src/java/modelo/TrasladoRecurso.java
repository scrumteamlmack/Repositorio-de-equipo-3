package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Modelo para el traslado de recursos entre ambientes
 */
public class TrasladoRecurso implements Serializable {

    private int idTraslado;
    private int recursoId;
    private int ambienteOrigenId;
    private int ambienteDestinoId;
    private LocalDateTime fechaTraslado;
    private String observacion;
    
    // Campos auxiliares para mostrar información relacionada
    private String nombreRecurso;
    private String ambienteOrigen;
    private String ambienteDestino;

    public TrasladoRecurso() {
        this.fechaTraslado = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdTraslado() {
        return idTraslado;
    }

    public void setIdTraslado(int idTraslado) {
        this.idTraslado = idTraslado;
    }

    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
    }

    public int getAmbienteOrigenId() {
        return ambienteOrigenId;
    }

    public void setAmbienteOrigenId(int ambienteOrigenId) {
        this.ambienteOrigenId = ambienteOrigenId;
    }

    public int getAmbienteDestinoId() {
        return ambienteDestinoId;
    }

    public void setAmbienteDestinoId(int ambienteDestinoId) {
        this.ambienteDestinoId = ambienteDestinoId;
    }

    public LocalDateTime getFechaTraslado() {
        return fechaTraslado;
    }

    public void setFechaTraslado(LocalDateTime fechaTraslado) {
        this.fechaTraslado = fechaTraslado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNombreRecurso() {
        return nombreRecurso;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    public String getAmbienteOrigen() {
        return ambienteOrigen;
    }

    public void setAmbienteOrigen(String ambienteOrigen) {
        this.ambienteOrigen = ambienteOrigen;
    }

    public String getAmbienteDestino() {
        return ambienteDestino;
    }

    public void setAmbienteDestino(String ambienteDestino) {
        this.ambienteDestino = ambienteDestino;
    }
}

