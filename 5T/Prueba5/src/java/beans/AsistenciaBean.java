package beans;

import dao.AprendizDAO;
import dao.AsistenciaDAO;
import dao.InstructorDAO;
import dao.JornadaDAO;
import modelo.Aprendiz;
import modelo.Asistencia;
import modelo.Instructor;
import modelo.Jornada;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@ManagedBean(name = "asistenciaBean")
@ViewScoped
public class AsistenciaBean implements Serializable {

    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private final AprendizDAO aprendizDAO = new AprendizDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final JornadaDAO jornadaDAO = new JornadaDAO();

    private Asistencia asistencia = new Asistencia();
    private List<Asistencia> asistencias;
    private List<Aprendiz> aprendices;
    private List<Instructor> instructores;
    private List<Jornada> jornadas;

    @PostConstruct
    public void init() {
        asistencias = asistenciaDAO.listar();
        aprendices = aprendizDAO.listar();
        instructores = instructorDAO.listar();
        jornadas = jornadaDAO.listar();
        asistencia.setFecha(LocalDate.now());
    }

    public void prepararNuevo() {
        asistencia = new Asistencia();
        asistencia.setFecha(LocalDate.now());
    }

    public void guardar() {
        if (asistencia.getIdAsistencia() == 0) {
            asistenciaDAO.guardar(asistencia);
            FacesUtils.addInfoMessage("Registro creado.");
        } else {
            asistenciaDAO.actualizar(asistencia);
            FacesUtils.addInfoMessage("Registro actualizado.");
        }
        asistencias = asistenciaDAO.listar();
        prepararNuevo();
    }

    public void editar(int idAsistencia) {
        Asistencia encontrada = asistenciaDAO.buscarPorId(idAsistencia);
        if (encontrada != null) {
            asistencia = encontrada;
        }
    }
    
    public void editar(Asistencia seleccionada) {
        asistencia = seleccionada;
    }

    public void eliminar(int idAsistencia) {
        asistenciaDAO.eliminar(idAsistencia);
        asistencias = asistenciaDAO.listar();
        FacesUtils.addInfoMessage("Registro eliminado.");
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public List<Aprendiz> getAprendices() {
        return aprendices;
    }
    
    public List<Instructor> getInstructores() {
        return instructores;
    }
    
    public List<Jornada> getJornadas() {
        return jornadas;
    }

}

