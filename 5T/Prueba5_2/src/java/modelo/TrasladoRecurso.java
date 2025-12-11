package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TrasladoRecurso implements Serializable {

    private int idTraslado;
    private int recursoId;
    private int ambienteOrigen;
    private int ambienteDestino;
    private LocalDateTime fechaTraslado;
    private String observacion;
    

    private String recursoNombre;
    private String ambienteOrigenNombre;
    private String ambienteDestinoNombre;

    public TrasladoRecurso() {
    }

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

    public int getAmbienteOrigen() {
        return ambienteOrigen;
    }

    public void setAmbienteOrigen(int ambienteOrigen) {
        this.ambienteOrigen = ambienteOrigen;
    }

    public int getAmbienteDestino() {
        return ambienteDestino;
    }

    public void setAmbienteDestino(int ambienteDestino) {
        this.ambienteDestino = ambienteDestino;
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

    public String getRecursoNombre() {
        return recursoNombre;
    }

    public void setRecursoNombre(String recursoNombre) {
        this.recursoNombre = recursoNombre;
    }

    public String getAmbienteOrigenNombre() {
        return ambienteOrigenNombre;
    }

    public void setAmbienteOrigenNombre(String ambienteOrigenNombre) {
        this.ambienteOrigenNombre = ambienteOrigenNombre;
    }

    public String getAmbienteDestinoNombre() {
        return ambienteDestinoNombre;
    }

    public void setAmbienteDestinoNombre(String ambienteDestinoNombre) {
        this.ambienteDestinoNombre = ambienteDestinoNombre;
    }
}

