package beans;

import dao.AsistenciaDAO;
import modelo.Asistencia;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "asistenciaAprendizBean")
@ViewScoped
public class AsistenciaAprendizBean implements Serializable {

    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private List<Asistencia> asistencias;
    private List<Asistencia> asistenciasFiltradas;

    @PostConstruct
    public void init() {
<<<<<<< HEAD
        System.out.println("AsistenciaAprendizBean.init: Inicializando bean");
=======
        System.out.println("🔍 AsistenciaAprendizBean.init: Inicializando bean");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        cargarAsistencias();
    }

    private void cargarAsistencias() {
        try {
            if (loginBean != null && loginBean.isAutenticado() && loginBean.getUsuarioAutenticado() != null) {
                int idAprendiz = loginBean.getUsuarioAutenticado().getIdUsuario();
<<<<<<< HEAD
                System.out.println("ID Aprendiz: " + idAprendiz);
                asistencias = asistenciaDAO.listarPorAprendiz(idAprendiz);
                System.out.println(" Asistencias cargadas: " + (asistencias != null ? asistencias.size() : 0));
            } else {
                System.err.println(" No hay usuario autenticado");
=======
                System.out.println("   - ID Aprendiz: " + idAprendiz);
                asistencias = asistenciaDAO.listarPorAprendiz(idAprendiz);
                System.out.println("   - Asistencias cargadas: " + (asistencias != null ? asistencias.size() : 0));
            } else {
                System.err.println("   ⚠️ No hay usuario autenticado");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                asistencias = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("❌ AsistenciaAprendizBean.cargarAsistencias: Error: " + e.getMessage());
            e.printStackTrace();
            asistencias = new ArrayList<>();
        }
    }

    public String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getEstadoTexto(String estado) {
        if (estado == null) {
            return "";
        }
        switch (estado) {
            case "S":
                return "Asistió";
            case "R":
                return "Retraso";
            case "N":
                return "No Asistió";
            default:
                return estado;
        }
    }

    public String getEstadoSeverity(String estado) {
        if (estado == null) {
            return "info";
        }
        switch (estado) {
            case "S":
                return "success";
            case "R":
                return "warn";
            case "N":
                return "danger";
            default:
                return "info";
        }
    }

    public List<Asistencia> getAsistencias() {
        if (asistencias == null) {
            cargarAsistencias();
        }
        if (asistenciasFiltradas != null && !asistenciasFiltradas.isEmpty()) {
            return asistenciasFiltradas;
        }
        return asistencias;
    }

    public List<Asistencia> getAsistenciasFiltradas() {
        return asistenciasFiltradas;
    }

    public void setAsistenciasFiltradas(List<Asistencia> asistenciasFiltradas) {
        this.asistenciasFiltradas = asistenciasFiltradas;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}

