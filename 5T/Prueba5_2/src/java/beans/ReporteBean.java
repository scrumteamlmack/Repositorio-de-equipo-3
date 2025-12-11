package beans;

import dao.*;
import modelo.Ambiente;
import modelo.Incidente;
import modelo.Recurso;
import modelo.TipoIncidente;
import modelo.TipoRecurso;
import modelo.Usuario;
import util.ReportFilter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "reporteBean")
@ViewScoped
public class ReporteBean implements Serializable {

    private final IncidenteDAO incidenteDAO = new IncidenteDAO();
    private final RecursoDAO recursoDAO = new RecursoDAO();
    private final TipoIncidenteDAO tipoIncidenteDAO = new TipoIncidenteDAO();
    private final TipoRecursoDAO tipoRecursoDAO = new TipoRecursoDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private ReportFilter filtro = new ReportFilter();
    private List<Incidente> incidentesFiltrados = Collections.emptyList();
    private List<Recurso> recursosFiltrados = Collections.emptyList();
    private List<TipoIncidente> tiposIncidente;
    private List<TipoRecurso> tiposRecurso;
    private List<Ambiente> ambientes;
    private List<Usuario> usuarios;

    @PostConstruct
    public void init() {
        tiposIncidente = tipoIncidenteDAO.listar();
        tiposRecurso = tipoRecursoDAO.listar();
        ambientes = ambienteDAO.listar();
        usuarios = usuarioDAO.listar();
    }

    public void filtrar() {
        incidentesFiltrados = incidenteDAO.filtrar(filtro);
        // Para recursos solo se filtra por ambiente/tipo recurso
        recursosFiltrados = recursoDAO.listar();
        if (filtro.getAmbienteId() != null || filtro.getTipoRecursoId() != null) {
            recursosFiltrados.removeIf(recurso ->
                    (filtro.getAmbienteId() != null && recurso.getIdAmbiente() != filtro.getAmbienteId()) ||
                            (filtro.getTipoRecursoId() != null && recurso.getIdTipoRecurso() != filtro.getTipoRecursoId())
            );
        }
    }

    public void limpiar() {
        filtro = new ReportFilter();
        incidentesFiltrados = Collections.emptyList();
        recursosFiltrados = Collections.emptyList();
    }

    public ReportFilter getFiltro() {
        return filtro;
    }

    public List<Incidente> getIncidentesFiltrados() {
        return incidentesFiltrados;
    }

    public List<Recurso> getRecursosFiltrados() {
        return recursosFiltrados;
    }

    public List<TipoIncidente> getTiposIncidente() {
        return tiposIncidente;
    }

    public List<TipoRecurso> getTiposRecurso() {
        return tiposRecurso;
    }

    public List<Ambiente> getAmbientes() {
        return ambientes;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}

