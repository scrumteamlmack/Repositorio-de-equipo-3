package beans;

import dao.TipoRecursoDAO;
import modelo.TipoRecurso;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "tipoRecursoBean")
@ViewScoped
public class TipoRecursoBean implements Serializable {

    private final TipoRecursoDAO tipoRecursoDAO = new TipoRecursoDAO();
    private TipoRecurso tipo = new TipoRecurso();
    private List<TipoRecurso> tipos;

    @PostConstruct
    public void init() {
        tipos = tipoRecursoDAO.listar();
    }

    public void prepararNuevo() {
        tipo = new TipoRecurso();
    }

    public void guardar() {
        if (tipo.getIdTipoRecurso() == 0) {
            tipoRecursoDAO.guardar(tipo);
            FacesUtils.addInfoMessage("Tipo de recurso creado.");
        } else {
            tipoRecursoDAO.actualizar(tipo);
            FacesUtils.addInfoMessage("Tipo de recurso actualizado.");
        }
        tipos = tipoRecursoDAO.listar();
        prepararNuevo();
    }

    public void editar(TipoRecurso seleccionado) {
        tipo = seleccionado;
    }

    public void eliminar(int id) {
        tipoRecursoDAO.eliminar(id);
        tipos = tipoRecursoDAO.listar();
        FacesUtils.addInfoMessage("Tipo de recurso eliminado.");
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecurso tipo) {
        this.tipo = tipo;
    }

    public List<TipoRecurso> getTipos() {
        return tipos;
    }
}

