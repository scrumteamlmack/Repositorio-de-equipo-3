package beans;

import dao.AmbienteDAO;
import modelo.Ambiente;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "ambienteBean")
@ViewScoped
public class AmbienteBean implements Serializable {

    private final AmbienteDAO ambienteDAO = new AmbienteDAO();
    private Ambiente ambiente = new Ambiente();
    private List<Ambiente> ambientes;
    private List<Ambiente> ambientesFiltrados;

    @PostConstruct
    public void init() {
        ambientes = ambienteDAO.listar();
    }

    public void prepararNuevo() {
        ambiente = new Ambiente();
    }

    public void guardar() {
        if (ambiente.getIdAmbiente() == 0) {
            int id = ambienteDAO.guardar(ambiente);
            if (id > 0) {
                FacesUtils.addInfoMessage("Ambiente registrado correctamente.");
            } else {
                FacesUtils.addErrorMessage("No fue posible guardar el ambiente.");
            }
        } else {
            ambienteDAO.actualizar(ambiente);
            FacesUtils.addInfoMessage("Ambiente actualizado.");
        }
        ambientes = ambienteDAO.listar();
        prepararNuevo();
    }

    public void editar(int idAmbiente) {
        Ambiente encontrado = ambienteDAO.buscarPorId(idAmbiente);
        if (encontrado != null) {
            ambiente = encontrado;
        }
    }

    public void eliminar(int idAmbiente) {
        ambienteDAO.eliminar(idAmbiente);
        ambientes = ambienteDAO.listar();
        FacesUtils.addInfoMessage("Ambiente eliminado.");
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

    public List<Ambiente> getAmbientes() {
        return ambientes;
    }

    public List<Ambiente> getAmbientesFiltrados() {
        return ambientesFiltrados;
    }

    public void setAmbientesFiltrados(List<Ambiente> ambientesFiltrados) {
        this.ambientesFiltrados = ambientesFiltrados;
    }

    // Métodos para estadísticas
    public int contarDisponibles() {
        if (ambientes == null) return 0;
        int count = 0;
        for (Ambiente a : ambientes) {
            if ("Disponible".equalsIgnoreCase(a.getEstado())) {
                count++;
            }
        }
        return count;
    }

    public int contarOcupados() {
        if (ambientes == null) return 0;
        int count = 0;
        for (Ambiente a : ambientes) {
            if ("Ocupado".equalsIgnoreCase(a.getEstado())) {
                count++;
            }
        }
        return count;
    }

    public int getTotalAmbientes() {
        return ambientes != null ? ambientes.size() : 0;
    }
}

