package beans;

import dao.AmbienteDAO;
import dao.RecursoDAO;
import dao.TipoRecursoDAO;
import modelo.Ambiente;
import modelo.Recurso;
import modelo.TipoRecurso;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "recursoBean")
@ViewScoped
public class RecursoBean implements Serializable {

    private final RecursoDAO recursoDAO = new RecursoDAO();
    private final TipoRecursoDAO tipoRecursoDAO = new TipoRecursoDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();

    private Recurso recurso = new Recurso();
    private List<Recurso> recursos;
    private List<TipoRecurso> tipos;
    private List<Ambiente> ambientes;

    @PostConstruct
    public void init() {
        recursos = recursoDAO.listar();
        tipos = tipoRecursoDAO.listar();
        ambientes = ambienteDAO.listar();
    }

    public void prepararNuevo() {
        recurso = new Recurso();
    }

    public void guardar() {
        if (recurso.getIdRecurso() == 0) {
            recursoDAO.guardar(recurso);
            FacesUtils.addInfoMessage("Recurso registrado.");
        } else {
            recursoDAO.actualizar(recurso);
            FacesUtils.addInfoMessage("Recurso actualizado.");
        }
        recursos = recursoDAO.listar();
        prepararNuevo();
    }

    public void editar(int idRecurso) {
        Recurso encontrado = recursoDAO.buscarPorId(idRecurso);
        if (encontrado != null) {
            recurso = encontrado;
        }
    }
    
    public void editar(Recurso seleccionado) {
        recurso = seleccionado;
    }

    public void eliminar(int idRecurso) {
        recursoDAO.eliminar(idRecurso);
        recursos = recursoDAO.listar();
        FacesUtils.addInfoMessage("Recurso eliminado.");
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public List<TipoRecurso> getTipos() {
        return tipos;
    }

    public List<Ambiente> getAmbientes() {
        return ambientes;
    }
}

