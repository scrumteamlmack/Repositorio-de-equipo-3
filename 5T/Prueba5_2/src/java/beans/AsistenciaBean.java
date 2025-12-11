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
    private Integer idAsistenciaEditar;
    
    private Date fechaDate;

    @PostConstruct
    public void init() {
        asistencias = asistenciaDAO.listar();
        aprendices = aprendizDAO.listar();
        instructores = instructorDAO.listar();
        jornadas = jornadaDAO.listar();
        
   
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
        

        if (!cargarAsistenciaSiEsNecesario()) {
            prepararNuevo();
        }
    }
    
    private boolean cargarAsistenciaSiEsNecesario() {
        
        try {
            javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
            if (facesContext != null) {
                String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        System.out.println("   - ID encontrado en URL en @PostConstruct: " + id);
                        // Si encontramos un ID en la URL, es probable que sea modo edición
                        if (id > 0 && (asistencia == null || asistencia.getIdAsistencia() == 0)) {
                            Asistencia encontrada = asistenciaDAO.buscarPorId(id);
                            if (encontrada != null) {
                                idAsistenciaEditar = id;
                                asistencia = encontrada;
                                // Convertir LocalDate a Date para el calendario
                                if (asistencia.getFecha() != null) {
                                    fechaDate = java.sql.Date.valueOf(asistencia.getFecha());
                                }
                                System.out.println("   ✅ Asistencia cargada en @PostConstruct: ID " + asistencia.getIdAsistencia());
                                return true; // Se cargó una asistencia para editar
                            }
                        }
                    } catch (NumberFormatException e) {
                        
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ AsistenciaBean.cargarAsistenciaSiEsNecesario: Error: " + e.getMessage());
        }
        return false; 
    }

    public void prepararNuevo() {
        asistencia = new Asistencia();
        asistencia.setFecha(LocalDate.now());
        asistencia.setAprendizUsuarioId(0);
        asistencia.setInstructorUsuarioId(0);
        asistencia.setJornadaId(0);
        asistencia.setEstado("");
        fechaDate = new Date();
    }

    public String guardar() {

        if (asistencia.getAprendizUsuarioId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar un aprendiz.");
            return null;
        }
        if (asistencia.getInstructorUsuarioId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar un instructor.");
            return null;
        }
        if (asistencia.getJornadaId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar una jornada.");
            return null;
        }
        if (asistencia.getEstado() == null || asistencia.getEstado().isEmpty()) {
            FacesUtils.addErrorMessage("Debe seleccionar un estado.");
            return null;
        }
        
        if (fechaDate != null) {
            asistencia.setFecha(fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            FacesUtils.addErrorMessage("Debe seleccionar una fecha.");
            return null;
        }
        
        if (asistencia.getIdAsistencia() == 0) {
            asistenciaDAO.guardar(asistencia);
            FacesUtils.addInfoMessage("Registro creado correctamente.");
            asistencias = asistenciaDAO.listar();
            prepararNuevo();
            return "/pages/instructor/asistencias/listarAsistencias.xhtml?faces-redirect=true";
        } else {
            asistenciaDAO.actualizar(asistencia);
            FacesUtils.addInfoMessage("Registro actualizado correctamente.");
            asistencias = asistenciaDAO.listar();
            prepararNuevo();
            return "/pages/instructor/asistencias/listarAsistencias.xhtml?faces-redirect=true";
        }
    }

    public String editar(int idAsistencia) {
        return "/pages/instructor/asistencias/editarAsistencia.xhtml?id=" + idAsistencia + "&faces-redirect=true";
    }
    
    public String cargarAsistenciaParaEditar() {
        System.out.println("AsistenciaBean.cargarAsistenciaParaEditar: INICIO");
        System.out.println("idAsistenciaEditar desde viewParam: " + idAsistenciaEditar);
        System.out.println("asistencia actual: " + (asistencia != null ? "ID=" + asistencia.getIdAsistencia() : "null"));
        
        if (asistencia != null && asistencia.getIdAsistencia() > 0 && idAsistenciaEditar != null && asistencia.getIdAsistencia() == idAsistenciaEditar) {
            System.out.println(" Asistencia ya está cargada, no es necesario recargar");
            return null;
        }
        
        if (idAsistenciaEditar == null || idAsistenciaEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    System.out.println(" Parámetro 'id' obtenido de URL: " + idParam);
                    if (idParam != null && !idParam.isEmpty()) {
                        idAsistenciaEditar = Integer.parseInt(idParam);
                        System.out.println(" idAsistenciaEditar parseado desde URL: " + idAsistenciaEditar);
                    } else {
                        System.err.println(" No se encontró parámetro 'id' en la URL");
                    }
                } else {
                    System.err.println(" FacesContext es null");
                }
            } catch (Exception e) {
                System.err.println(" AsistenciaBean.cargarAsistenciaParaEditar: Error al obtener idAsistenciaEditar de URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println(" idAsistenciaEditar FINAL: " + idAsistenciaEditar);
        
        
        if (idAsistenciaEditar != null && idAsistenciaEditar > 0) {
            System.out.println("   - Llamando a asistenciaDAO.buscarPorId(" + idAsistenciaEditar + ")");
            Asistencia encontrada = asistenciaDAO.buscarPorId(idAsistenciaEditar);
            
            if (encontrada != null) {
                System.out.println(" Asistencia encontrada en BD:");
                System.out.println(" ID: " + encontrada.getIdAsistencia());
                System.out.println(" Aprendiz: " + encontrada.getAprendizUsuarioId());
                System.out.println(" Instructor: " + encontrada.getInstructorUsuarioId());
                System.out.println(" Jornada: " + encontrada.getJornadaId());
                System.out.println(" Estado: " + encontrada.getEstado());
                System.out.println(" Fecha: " + encontrada.getFecha());
                
                
                if (asistencia == null) {
                    asistencia = new Asistencia();
                }
                asistencia.setIdAsistencia(encontrada.getIdAsistencia());
                asistencia.setAprendizUsuarioId(encontrada.getAprendizUsuarioId());
                asistencia.setInstructorUsuarioId(encontrada.getInstructorUsuarioId());
                asistencia.setJornadaId(encontrada.getJornadaId());
                asistencia.setEstado(encontrada.getEstado());
                asistencia.setFecha(encontrada.getFecha());
                
                if (asistencia.getFecha() != null) {
                    fechaDate = java.sql.Date.valueOf(asistencia.getFecha());
                } else {
                    fechaDate = null;
                }
                
                System.out.println(" Datos asignados al bean asistencia");
            } else {
                System.err.println(" Asistencia NO encontrada en BD con ID: " + idAsistenciaEditar);
                FacesUtils.addErrorMessage("Asistencia no encontrada con ID: " + idAsistenciaEditar);
            }
        } else {
            System.err.println(" idAsistenciaEditar es null o 0 - No se puede cargar asistencia");
            System.err.println(" Esto puede indicar que el viewParam no funcionó correctamente");
            FacesUtils.addErrorMessage("No se proporcionó ID de asistencia válido para editar.");
        }
        
        return null;
    }
    
    public String actualizarAsistencia() {
        System.out.println("AsistenciaBean.actualizarAsistencia: Actualizando asistencia ID: " + asistencia.getIdAsistencia());
        
        if (asistencia.getAprendizUsuarioId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar un aprendiz.");
            return null;
        }
        if (asistencia.getInstructorUsuarioId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar un instructor.");
            return null;
        }
        if (asistencia.getJornadaId() == 0) {
            FacesUtils.addErrorMessage("Debe seleccionar una jornada.");
            return null;
        }
        if (asistencia.getEstado() == null || asistencia.getEstado().isEmpty()) {
            FacesUtils.addErrorMessage("Debe seleccionar un estado.");
            return null;
        }
        
        
        if (fechaDate != null) {
            asistencia.setFecha(fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            FacesUtils.addErrorMessage("Debe seleccionar una fecha.");
            return null;
        }
        
        if (asistencia.getIdAsistencia() == 0) {
            FacesUtils.addErrorMessage("Error: No se puede actualizar una asistencia sin ID.");
            return null;
        }
        
        boolean actualizado = asistenciaDAO.actualizar(asistencia);
        
        if (actualizado) {
            FacesUtils.addInfoMessage("Asistencia actualizada correctamente.");
            asistencias = asistenciaDAO.listar();
            prepararNuevo();
            return "/pages/instructor/asistencias/listarAsistencias.xhtml?faces-redirect=true";
        } else {
            FacesUtils.addErrorMessage("Error al actualizar la asistencia.");
            return null;
        }
    }
    

    public void eliminar(int idAsistencia) {
        asistenciaDAO.eliminar(idAsistencia);
        asistencias = asistenciaDAO.listar();
        FacesUtils.addInfoMessage("Registro eliminado.");
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

    public List<Asistencia> getAsistenciasFiltradas() {
        return asistenciasFiltradas;
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

    public Integer getIdAsistenciaEditar() {
        return idAsistenciaEditar;
    }

    public void setIdAsistenciaEditar(Integer idAsistenciaEditar) {
        this.idAsistenciaEditar = idAsistenciaEditar;
    }

}

