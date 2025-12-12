package beans;

import dao.IncidenteDAO;
import dao.AmbienteDAO;
import dao.TipoIncidenteDAO;
import dao.UsuarioDAO;
import modelo.Incidente;
import modelo.Ambiente;
import modelo.TipoIncidente;
import modelo.Usuario;
import util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "incidenteBean")
@ViewScoped
public class IncidenteBean implements Serializable {

    private final IncidenteDAO incidenteDAO = new IncidenteDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();
    private final TipoIncidenteDAO tipoIncidenteDAO = new TipoIncidenteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private Incidente incidente = new Incidente();
    private Integer idIncidenteEditar;
    private String horaString;
    private Date fechaDate; 
    
    private boolean inicializado = false;

    private List<Incidente> incidentes;
    private List<Incidente> incidentesFiltrados;
    private List<Ambiente> ambientes;
    private List<TipoIncidente> tiposIncidente;
    private List<Usuario> instructores;

    public IncidenteBean() {
        System.out.println("IncidenteBean: Constructor llamado");
    }

    private void cargarDatos() {
        try {
            System.out.println("IncidenteBean.cargarDatos: Iniciando carga de datos");
            
            incidentes = incidenteDAO.listar();
            incidentesFiltrados = null;
            System.out.println(" Incidentes cargados: " + (incidentes != null ? incidentes.size() : 0));
            
            ambientes = ambienteDAO.listar();
            System.out.println(" Ambientes cargados: " + (ambientes != null ? ambientes.size() : 0));
            if (ambientes != null && !ambientes.isEmpty()) {
                for (Ambiente a : ambientes) {
                    System.out.println("Ambiente: " + a.getNumero() + " (ID: " + a.getIdAmbiente() + ")");
                }
            } else {
                System.err.println("No se encontraron ambientes en la base de datos");
            }
            
            tiposIncidente = tipoIncidenteDAO.listar();
            System.out.println(" Tipos de incidente cargados: " + (tiposIncidente != null ? tiposIncidente.size() : 0));
            if (tiposIncidente != null && !tiposIncidente.isEmpty()) {
                for (TipoIncidente t : tiposIncidente) {
                    System.out.println("Tipo: " + t.getNombre() + " (ID: " + t.getIdTipoIncidente() + ")");
                }
            } else {
                System.err.println("No se encontraron tipos de incidente en la base de datos");
            }
            
            instructores = usuarioDAO.listarPorRol("Instructor");
            System.out.println(" Instructores cargados: " + (instructores != null ? instructores.size() : 0));
            if (instructores != null && !instructores.isEmpty()) {
                for (Usuario u : instructores) {
                    System.out.println(" Instructor: " + u.getPNombre() + " " + u.getPApellido() + " (ID: " + u.getIdUsuario() + ")");
                }
            } else {
                System.err.println(" No se encontraron instructores en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("IncidenteBean.cargarDatos: Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("IncidenteBean.init: @PostConstruct ejecutado");
        System.out.println("idIncidenteEditar recibido: " + idIncidenteEditar);
        
        if (idIncidenteEditar == null || idIncidenteEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("idIncidenteEditar");
                    if (idParam != null && !idParam.isEmpty()) {
                        idIncidenteEditar = Integer.parseInt(idParam);
                        System.out.println(" idIncidenteEditar obtenido de parámetro URL: " + idIncidenteEditar);
                    }
                }
            } catch (Exception e) {
                System.err.println(" IncidenteBean.init: Error al obtener idIncidenteEditar de URL: " + e.getMessage());
            }
        }
        
        cargarDatos();
        inicializado = true;
        
        if (idIncidenteEditar != null && idIncidenteEditar > 0) {
            System.out.println("Cargando incidente para editar con ID: " + idIncidenteEditar);
            Incidente existente = incidenteDAO.buscarPorId(idIncidenteEditar);
            if (existente != null) {
                System.out.println(" Incidente existente encontrado");
                System.out.println(" Descripción: " + existente.getDescripcion());
                System.out.println(" Fecha: " + existente.getFecha());
                System.out.println(" Hora: " + existente.getHora());
                System.out.println(" Ambiente: " + existente.getIdAmbiente());
                System.out.println(" Tipo: " + existente.getIdTipoIncidente());
                System.out.println(" Reportador: " + existente.getIdReportador());
                
                incidente = existente;
                
                if (incidente.getHora() != null) {
                    horaString = incidente.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
                    System.out.println("Hora convertida a String: " + horaString);
                } else {
                    System.err.println("La hora del incidente es NULL");
                }
                
                if (incidente.getFecha() != null) {
                    fechaDate = java.sql.Date.valueOf(incidente.getFecha());
                    System.out.println(" Fecha convertida para formulario: " + fechaDate);
                } else {
                    System.err.println(" La fecha del incidente es NULL");
                }
            } else {
                System.err.println(" No se encontró el incidente con ID: " + idIncidenteEditar);
                incidente = new Incidente();
            }
        } else {
            System.out.println(" Creando nuevo incidente");
            incidente = new Incidente();
            if (loginBean != null && loginBean.getUsuarioAutenticado() != null) {
                incidente.setIdReportador(loginBean.getUsuarioAutenticado().getIdUsuario());
                System.out.println("Reportador asignado: " + loginBean.getUsuarioAutenticado().getIdUsuario());
            }
        }
    }

    public String guardar() {
        System.out.println("IncidenteBean.guardar: Iniciando guardado");
        System.out.println("incidente.getIdIncidente(): " + incidente.getIdIncidente());
        System.out.println("incidente.getDescripcion(): " + incidente.getDescripcion());
        System.out.println("incidente.getIdAmbiente(): " + incidente.getIdAmbiente());
        System.out.println("incidente.getIdTipoIncidente(): " + incidente.getIdTipoIncidente());
        System.out.println("incidente.getIdReportador(): " + incidente.getIdReportador());
        
        if (incidente.getDescripcion() == null || incidente.getDescripcion().trim().isEmpty()) {
            System.err.println(" IncidenteBean.guardar: descripcion vacía");
            FacesUtils.addErrorMessage("Debe ingresar una descripción del incidente.");
            return null;
        }
        
        if (fechaDate != null) {
            incidente.setFecha(fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            System.out.println(" Fecha convertida de Date a LocalDate: " + incidente.getFecha());
        }
        
        if (incidente.getFecha() == null) {
            System.err.println(" IncidenteBean.guardar: fecha vacía");
            FacesUtils.addErrorMessage("Debe seleccionar una fecha.");
            return null;
        }
        
        if (horaString == null || horaString.trim().isEmpty()) {
            System.err.println(" IncidenteBean.guardar: hora vacía");
            FacesUtils.addErrorMessage("Debe ingresar una hora.");
            return null;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            incidente.setHora(LocalTime.parse(horaString, formatter));
            System.out.println(" Hora convertida: " + incidente.getHora());
        } catch (Exception e) {
            System.err.println(" IncidenteBean.guardar: Error al parsear hora: " + e.getMessage());
            FacesUtils.addErrorMessage("Formato de hora inválido. Use HH:mm (ejemplo: 14:30).");
            return null;
        }
        
        if (incidente.getIdAmbiente() == 0) {
            System.err.println("IncidenteBean.guardar: ambiente no seleccionado");
            FacesUtils.addErrorMessage("Debe seleccionar un ambiente.");
            return null;
        }
        
        if (incidente.getIdTipoIncidente() == 0) {
            System.err.println("IncidenteBean.guardar: tipo de incidente no seleccionado");
            FacesUtils.addErrorMessage("Debe seleccionar un tipo de incidente.");
            return null;
        }
        
        if (incidente.getIdReportador() == 0 && loginBean != null && loginBean.getUsuarioAutenticado() != null) {
            incidente.setIdReportador(loginBean.getUsuarioAutenticado().getIdUsuario());
            System.out.println(" Reportador asignado automáticamente: " + incidente.getIdReportador());
        }
        
        if (incidente.getIdReportador() == 0) {
            System.err.println("IncidenteBean.guardar: reportador no asignado");
            FacesUtils.addErrorMessage("Error: No se pudo identificar el instructor reportador.");
            return null;
        }

        boolean guardado;
        
        if (incidente.getIdIncidente() == 0) {
            System.out.println(" Creando nuevo registro de incidente");
            int idGenerado = incidenteDAO.guardar(incidente);
            guardado = idGenerado > 0;
        } else {
            System.out.println(" Actualizando registro existente");
            guardado = incidenteDAO.actualizar(incidente);
        }

        if (guardado) {
            System.out.println("IncidenteBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Incidente registrado correctamente.");
            return "/pages/instructor/listarIncidentes.xhtml?faces-redirect=true";
        } else {
            System.err.println("IncidenteBean.guardar: No se pudo guardar");
            FacesUtils.addErrorMessage("No fue posible guardar el incidente. Verifique los logs del servidor.");
            return null;
        }
    }

    public String editar(int id) {
        System.out.println("IncidenteBean.editar: Editando incidente ID: " + id);
        return "/pages/instructor/formIncidente.xhtml?idIncidenteEditar=" + id + "&faces-redirect=true";
    }

    public String eliminar(int id) {
        System.out.println("IncidenteBean.eliminar: Eliminando incidente ID: " + id);
        boolean eliminado = incidenteDAO.eliminar(id);
        if (eliminado) {
            System.out.println("IncidenteBean.eliminar: Eliminado exitosamente");
            FacesUtils.addInfoMessage("Incidente eliminado correctamente.");
            cargarDatos();
        } else {
            System.err.println("IncidenteBean.eliminar: No se pudo eliminar");
            FacesUtils.addErrorMessage("No fue posible eliminar el incidente. Puede que tenga registros relacionados.");
        }
        return null; // Se queda en la misma página
    }

    public void prepararNuevo() {
        System.out.println("IncidenteBean.prepararNuevo: Preparando nuevo incidente");
        incidente = new Incidente();
        horaString = null;
        fechaDate = null;
        if (loginBean != null && loginBean.getUsuarioAutenticado() != null) {
            incidente.setIdReportador(loginBean.getUsuarioAutenticado().getIdUsuario());
        }
    }

    public String getNombreAmbiente(int idAmbiente) {
        Ambiente ambiente = ambienteDAO.buscarPorId(idAmbiente);
        if (ambiente != null) {
            return "Ambiente " + ambiente.getNumero() + " - " + ambiente.getTipo();
        }
        return "Ambiente " + idAmbiente;
    }

    public String getNombreTipoIncidente(int idTipoIncidente) {
        for (TipoIncidente t : getTiposIncidente()) {
            if (t.getIdTipoIncidente() == idTipoIncidente) {
                return t.getNombre();
            }
        }
        return "Tipo " + idTipoIncidente;
    }

    public String getNombreInstructor(int idInstructor) {
        Usuario usuario = usuarioDAO.buscarPorId(idInstructor);
        if (usuario != null) {
            return usuario.getPNombre() + " " + usuario.getPApellido();
        }
        return "Instructor " + idInstructor;
    }

    public String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "-";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return fecha.format(formatter);
        } catch (Exception e) {
            System.err.println("Error al formatear fecha: " + e.getMessage());
            return fecha.toString();
        }
    }

    public String formatearHora(LocalTime hora) {
        if (hora == null) {
            return "-";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return hora.format(formatter);
        } catch (Exception e) {
            System.err.println("Error al formatear hora: " + e.getMessage());
            return hora.toString();
        }
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public Integer getIdIncidenteEditar() {
        return idIncidenteEditar;
    }

    public void setIdIncidenteEditar(Integer idIncidenteEditar) {
        this.idIncidenteEditar = idIncidenteEditar;
    }

    public String getHoraString() {
        if (horaString == null && incidente != null && incidente.getHora() != null) {
            horaString = incidente.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return horaString;
    }

    public void setHoraString(String horaString) {
        this.horaString = horaString;
    }

    public Date getFechaDate() {
        if (incidente != null && incidente.getFecha() != null && fechaDate == null) {
            fechaDate = java.sql.Date.valueOf(incidente.getFecha());
        }
        return fechaDate;
    }

    public void setFechaDate(Date fechaDate) {
        this.fechaDate = fechaDate;
        if (fechaDate != null && incidente != null) {
            incidente.setFecha(fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            System.out.println("Fecha establecida desde Date: " + incidente.getFecha());
        }
    }

    public List<Incidente> getIncidentes() {
        if (incidentes == null) {
            incidentes = incidenteDAO.listar();
        }
        return incidentes;
    }

    public List<Incidente> getIncidentesFiltrados() {
        return incidentesFiltrados;
    }

    public void setIncidentesFiltrados(List<Incidente> incidentesFiltrados) {
        this.incidentesFiltrados = incidentesFiltrados;
    }

    public List<Ambiente> getAmbientes() {
        System.out.println("IncidenteBean.getAmbientes: Getter llamado");
        if (!inicializado) {
            System.out.println("️ Bean no inicializado, ejecutando init()...");
            init();
        }
        if (ambientes == null || ambientes.isEmpty()) {
            System.out.println(" Ambientes es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println("Retornando " + (ambientes != null ? ambientes.size() : 0) + " ambientes");
        return ambientes != null ? ambientes : new java.util.ArrayList<>();
    }

    public List<TipoIncidente> getTiposIncidente() {
        System.out.println("IncidenteBean.getTiposIncidente: Getter llamado");
        if (!inicializado) {
            System.out.println(" Bean no inicializado, ejecutando init()...");
            init();
        }
        if (tiposIncidente == null || tiposIncidente.isEmpty()) {
            System.out.println(" Tipos de incidente es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println(" Retornando " + (tiposIncidente != null ? tiposIncidente.size() : 0) + " tipos de incidente");
        return tiposIncidente != null ? tiposIncidente : new java.util.ArrayList<>();
    }

    public List<Usuario> getInstructores() {
        System.out.println("IncidenteBean.getInstructores: Getter llamado");
        if (!inicializado) {
            System.out.println(" Bean no inicializado, ejecutando init()...");
            init();
        }
        if (instructores == null || instructores.isEmpty()) {
            System.out.println(" Instructores es null o vacío, cargando datos...");
            cargarDatos();
        }
        System.out.println(" Retornando " + (instructores != null ? instructores.size() : 0) + " instructores");
        return instructores != null ? instructores : new java.util.ArrayList<>();
    }

    public String getNombreReportador() {
        if (loginBean != null && loginBean.getUsuarioAutenticado() != null) {
            Usuario usuario = loginBean.getUsuarioAutenticado();
            return usuario.getPNombre() + " " + usuario.getPApellido();
        }
        return "No identificado";
    }

    public boolean isModoEdicion() {
        return idIncidenteEditar != null && idIncidenteEditar > 0;
    }
}

