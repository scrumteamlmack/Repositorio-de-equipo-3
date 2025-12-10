package beans;

import dao.AprendizDAO;
import dao.AsistenciaDAO;
import dao.InstructorDAO;
import dao.JornadaDAO;
import dao.UsuarioDAO;
import modelo.Aprendiz;
import modelo.Asistencia;
import modelo.Instructor;
import modelo.Jornada;
import modelo.Usuario;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "asistenciaBean")
@ViewScoped
public class AsistenciaBean implements Serializable {

    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private final AprendizDAO aprendizDAO = new AprendizDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final JornadaDAO jornadaDAO = new JornadaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private Asistencia asistencia = new Asistencia();
    private List<Asistencia> asistencias;
    private List<Asistencia> asistenciasFiltradas;
    private List<Aprendiz> aprendices;
    private List<Instructor> instructores;
    private List<Jornada> jornadas;
    private List<Usuario> aprendicesUsuarios;
    private List<Usuario> instructoresUsuarios;
    private int idAsistenciaSeleccionada;
    
    // Propiedad Date para el calendario (conversión Date <-> LocalDate)
    private Date fechaDate;

    @PostConstruct
    public void init() {
        asistencias = asistenciaDAO.listar();
        aprendices = aprendizDAO.listar();
        instructores = instructorDAO.listar();
        jornadas = jornadaDAO.listar();
        asistencia.setFecha(LocalDate.now());
        
        // Cargar usuarios completos para mostrar nombres
        aprendicesUsuarios = new java.util.ArrayList<>();
        for (Aprendiz a : aprendices) {
            Usuario u = usuarioDAO.buscarPorId(a.getIdUsuario());
            if (u != null) {
                aprendicesUsuarios.add(u);
            }
        }
        
        instructoresUsuarios = new java.util.ArrayList<>();
        for (Instructor i : instructores) {
            Usuario u = usuarioDAO.buscarPorId(i.getIdUsuario());
            if (u != null) {
                instructoresUsuarios.add(u);
            }
        }
    }

    public void prepararNuevo() {
        asistencia = new Asistencia();
        asistencia.setFecha(LocalDate.now());
        fechaDate = new Date();
    }

    public void guardar() {
        // Convertir Date a LocalDate antes de guardar
        if (fechaDate != null) {
            asistencia.setFecha(fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
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
            // Convertir LocalDate a Date para el calendario
            if (asistencia.getFecha() != null) {
                fechaDate = java.sql.Date.valueOf(asistencia.getFecha());
            } else {
                fechaDate = null;
            }
        }
    }
    
    public void editar() {
        editar(idAsistenciaSeleccionada);
    }
    
    public void editar(Asistencia seleccionada) {
        asistencia = seleccionada;
        // Convertir LocalDate a Date para el calendario
        if (asistencia.getFecha() != null) {
            fechaDate = java.sql.Date.valueOf(asistencia.getFecha());
        } else {
            fechaDate = null;
        }
    }

    public void eliminar(int idAsistencia) {
        asistenciaDAO.eliminar(idAsistencia);
        asistencias = asistenciaDAO.listar();
        FacesUtils.addInfoMessage("Registro eliminado.");
    }
    
    public void eliminar() {
        eliminar(idAsistenciaSeleccionada);
    }
    
    public int getIdAsistenciaSeleccionada() {
        return idAsistenciaSeleccionada;
    }
    
    public void setIdAsistenciaSeleccionada(int idAsistenciaSeleccionada) {
        this.idAsistenciaSeleccionada = idAsistenciaSeleccionada;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public List<Asistencia> getAsistencias() {
        if (asistencias == null) {
            asistencias = asistenciaDAO.listar();
        }
        if (asistenciasFiltradas != null && !asistenciasFiltradas.isEmpty()) {
            return asistenciasFiltradas;
        }
        return asistencias;
    }

    public void setAsistenciasFiltradas(List<Asistencia> asistenciasFiltradas) {
        this.asistenciasFiltradas = asistenciasFiltradas;
    }
    
    public String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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

    public List<Aprendiz> getAprendices() {
        return aprendices;
    }
    
    public List<Instructor> getInstructores() {
        return instructores;
    }
    
    public List<Jornada> getJornadas() {
        return jornadas;
    }
    
    public List<Usuario> getAprendicesUsuarios() {
        return aprendicesUsuarios;
    }
    
    public List<Usuario> getInstructoresUsuarios() {
        return instructoresUsuarios;
    }
    
    public Date getFechaDate() {
        // Si hay una fecha en LocalDate, convertirla a Date
        if (asistencia != null && asistencia.getFecha() != null && fechaDate == null) {
            fechaDate = java.sql.Date.valueOf(asistencia.getFecha());
        }
        return fechaDate;
    }
    
    public void setFechaDate(Date fechaDate) {
        this.fechaDate = fechaDate;
        // Convertir Date a LocalDate cuando se establece
        if (fechaDate != null && asistencia != null) {
            asistencia.setFecha(fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

}

