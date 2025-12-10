package beans;

import dao.TipoIncidenteDAO;
import modelo.TipoIncidente;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "tipoIncidenteBean")
@ViewScoped
public class TipoIncidenteBean implements Serializable {

    private final TipoIncidenteDAO tipoIncidenteDAO = new TipoIncidenteDAO();
    private TipoIncidente tipo = new TipoIncidente();
    private List<TipoIncidente> tipos;

    @PostConstruct
    public void init() {
        tipos = tipoIncidenteDAO.listar();
    }

    public void prepararNuevo() {
        tipo = new TipoIncidente();
    }

    public void guardar() {
        if (tipo.getIdTipoIncidente() == 0) {
            tipoIncidenteDAO.guardar(tipo);
            FacesUtils.addInfoMessage("Tipo de incidente creado.");
        } else {
            tipoIncidenteDAO.actualizar(tipo);
            FacesUtils.addInfoMessage("Tipo de incidente actualizado.");
        }
        tipos = tipoIncidenteDAO.listar();
        prepararNuevo();
    }

    public void editar(TipoIncidente seleccionado) {
        tipo = seleccionado;
    }

    public void eliminar(int id) {
        tipoIncidenteDAO.eliminar(id);
        tipos = tipoIncidenteDAO.listar();
        FacesUtils.addInfoMessage("Tipo de incidente eliminado.");
    }

    public TipoIncidente getTipo() {
        return tipo;
    }

    public void setTipo(TipoIncidente tipo) {
        this.tipo = tipo;
    }

    public List<TipoIncidente> getTipos() {
        return tipos;
    }
}

