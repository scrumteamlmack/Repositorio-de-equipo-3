package beans;

import dao.AmbienteDAO;
import dao.GuardaSeguridadDAO;
import dao.InstructorDAO;
import dao.MinutaDAO;
import modelo.Ambiente;
import modelo.GuardaSeguridad;
import modelo.Instructor;
import modelo.Minuta;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ManagedBean(name = "minutaBean")
@ViewScoped
public class MinutaBean implements Serializable {

    private final MinutaDAO minutaDAO = new MinutaDAO();
    private final GuardaSeguridadDAO guardaDAO = new GuardaSeguridadDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();

    private Minuta minuta = new Minuta();
    private List<Minuta> minutas;
    private List<GuardaSeguridad> guardas;
    private List<Instructor> instructores;
    private List<Ambiente> ambientes;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        minutas = minutaDAO.listar();
        guardas = guardaDAO.listar();
        instructores = instructorDAO.listar();
        ambientes = ambienteDAO.listar();
        minuta.setFechaRecibo(LocalDateTime.now());
        minuta.setFechaEntrega(LocalDateTime.now());
    }

    public void prepararNuevo() {
        minuta = new Minuta();
        minuta.setFechaRecibo(LocalDateTime.now());
        minuta.setFechaEntrega(LocalDateTime.now());
        minuta.setEstado("Normal");
        if (loginBean != null && loginBean.isAutenticado()) {
            minuta.setGuardaId(loginBean.getUsuarioAutenticado().getIdUsuario());
        }
    }

    public void guardar() {
        if (minuta.getIdMinuta() == 0) {
            minutaDAO.guardar(minuta);
            FacesUtils.addInfoMessage("Minuta registrada.");
        } else {
            minutaDAO.actualizar(minuta);
            FacesUtils.addInfoMessage("Minuta actualizada.");
        }
        minutas = minutaDAO.listar();
        prepararNuevo();
    }

    public void editar(int idMinuta) {
        Minuta encontrada = minutaDAO.buscarPorId(idMinuta);
        if (encontrada != null) {
            minuta = encontrada;
        }
    }
    
    public void editar(Minuta seleccionada) {
        minuta = seleccionada;
    }

    public void eliminar(int idMinuta) {
        minutaDAO.eliminar(idMinuta);
        minutas = minutaDAO.listar();
        FacesUtils.addInfoMessage("Minuta eliminada.");
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public List<Minuta> getMinutas() {
        return minutas;
    }

    public List<GuardaSeguridad> getGuardas() {
        return guardas;
    }
    
    public List<Instructor> getInstructores() {
        return instructores;
    }

    public List<Ambiente> getAmbientes() {
        return ambientes;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}

