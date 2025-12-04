package util;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Objeto de transporte para filtros de reportes multicriterio.
 */
public class ReportFilter implements Serializable {

    private Integer ambienteId;
    private Integer tipoIncidenteId;
    private Integer tipoRecursoId;
    private Integer usuarioId;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public Integer getAmbienteId() {
        return ambienteId;
    }

    public void setAmbienteId(Integer ambienteId) {
        this.ambienteId = ambienteId;
    }

    public Integer getTipoIncidenteId() {
        return tipoIncidenteId;
    }

    public void setTipoIncidenteId(Integer tipoIncidenteId) {
        this.tipoIncidenteId = tipoIncidenteId;
    }

    public Integer getTipoRecursoId() {
        return tipoRecursoId;
    }

    public void setTipoRecursoId(Integer tipoRecursoId) {
        this.tipoRecursoId = tipoRecursoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}

