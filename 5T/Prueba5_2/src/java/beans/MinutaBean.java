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
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "minutaBean")
@ViewScoped
public class MinutaBean implements Serializable {

    private final MinutaDAO minutaDAO = new MinutaDAO();
    private final GuardaSeguridadDAO guardaDAO = new GuardaSeguridadDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private Minuta minuta = new Minuta();
    private Integer idMinutaEditar;
    private int idMinutaSeleccionada;
    
    // Propiedades Date para los calendarios (conversión Date <-> LocalDateTime)
    private Date fechaReciboDate;
    private Date fechaEntregaDate;
    
    private boolean inicializado = false;

    private List<Minuta> minutas;
    private List<Minuta> minutasFiltradas;
    private List<GuardaSeguridad> guardas;
    private List<Instructor> instructores;
    private List<Ambiente> ambientes;

    private void cargarDatos() {
        try {
            System.out.println("🔍 MinutaBean.cargarDatos: Iniciando carga de datos");
            
            minutas = minutaDAO.listar();
            System.out.println("   - Minutas cargadas: " + (minutas != null ? minutas.size() : 0));
            
            guardas = guardaDAO.listar();
            System.out.println("   - Guardas cargados: " + (guardas != null ? guardas.size() : 0));
            
            instructores = instructorDAO.listar();
            System.out.println("   - Instructores cargados: " + (instructores != null ? instructores.size() : 0));
            
            ambientes = ambienteDAO.listar();
            System.out.println("   - Ambientes cargados: " + (ambientes != null ? ambientes.size() : 0));
        } catch (Exception e) {
            System.err.println("❌ MinutaBean.cargarDatos: Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("🔍 MinutaBean.init: @PostConstruct ejecutado");
        System.out.println("   - idMinutaEditar recibido: " + idMinutaEditar);
        
        cargarDatos();
        
        // Si no hay idMinutaEditar, intentar obtenerlo de la URL manualmente
        if (idMinutaEditar == null || idMinutaEditar == 0) {
            try {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("idMinutaEditar");
                    if (idParam == null || idParam.isEmpty()) {
                        idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    }
                    if (idParam != null && !idParam.isEmpty()) {
                        idMinutaEditar = Integer.parseInt(idParam);
                        System.out.println("   - idMinutaEditar obtenido de URL: " + idMinutaEditar);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ MinutaBean.init: Error al obtener idMinutaEditar de URL: " + e.getMessage());
            }
        }
        
        if (idMinutaEditar != null && idMinutaEditar > 0) {
            // Modo edición: cargar la minuta
            Minuta encontrada = minutaDAO.buscarPorId(idMinutaEditar);
            if (encontrada != null) {
                minuta = encontrada;
                // Convertir LocalDateTime a Date para los calendarios
                if (minuta.getFechaRecibo() != null) {
                    fechaReciboDate = Date.from(minuta.getFechaRecibo().atZone(ZoneId.systemDefault()).toInstant());
                }
                if (minuta.getFechaEntrega() != null) {
                    fechaEntregaDate = Date.from(minuta.getFechaEntrega().atZone(ZoneId.systemDefault()).toInstant());
                }
                System.out.println("   ✅ Minuta cargada para edición: ID " + idMinutaEditar);
            } else {
                System.err.println("   ⚠️ No se encontró minuta con ID: " + idMinutaEditar);
                prepararNuevo();
            }
        } else {
            // Modo creación: preparar nueva minuta
            prepararNuevo();
        }
        
        inicializado = true;
    }

    public void prepararNuevo() {
        minuta = new Minuta();
        minuta.setFechaRecibo(LocalDateTime.now());
        minuta.setFechaEntrega(LocalDateTime.now());
        minuta.setEstado("Normal");
        fechaReciboDate = new Date();
        fechaEntregaDate = new Date();
        
        if (loginBean != null && loginBean.isAutenticado()) {
            minuta.setGuardaId(loginBean.getUsuarioAutenticado().getIdUsuario());
        }
        
        idMinutaEditar = null;
    }

    public void guardar() {
        try {
            // Convertir Date a LocalDateTime antes de guardar
            if (fechaReciboDate != null) {
                minuta.setFechaRecibo(fechaReciboDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            if (fechaEntregaDate != null) {
                minuta.setFechaEntrega(fechaEntregaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            
            // Validar campos requeridos
            if (minuta.getAmbienteId() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un ambiente");
                return;
            }
            if (minuta.getGuardaId() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un guarda de seguridad");
                return;
            }
            if (minuta.getResponsableId() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un instructor responsable");
                return;
            }
            
            String mensaje;
            if (minuta.getIdMinuta() == 0) {
                int id = minutaDAO.guardar(minuta);
                if (id > 0) {
                    mensaje = "✅ Minuta registrada correctamente";
                } else {
                    FacesUtils.addErrorMessage("❌ No se pudo registrar la minuta");
                    return;
                }
            } else {
                boolean exito = minutaDAO.actualizar(minuta);
                if (exito) {
                    mensaje = "✅ Minuta actualizada correctamente";
                } else {
                    FacesUtils.addErrorMessage("❌ No se pudo actualizar la minuta");
                    return;
                }
            }
            
            minutas = minutaDAO.listar();
            prepararNuevo();
            
            // Guardar mensaje en Flash para que sobreviva la redirección
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mensaje", mensaje);
            FacesUtils.redirect("/faces/pages/guarda/minutas/listarMinutas.xhtml");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar la minuta: " + e.getMessage());
            System.err.println("❌ MinutaBean.guardar: Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String editar(int idMinuta) {
        return "/pages/guarda/minutas/formMinuta.xhtml?faces-redirect=true&idMinutaEditar=" + idMinuta;
    }
    
    public void editar() {
        idMinutaEditar = idMinutaSeleccionada;
        // Cargar la minuta para edición
        if (idMinutaSeleccionada > 0) {
            Minuta minutaEncontrada = minutaDAO.buscarPorId(idMinutaSeleccionada);
            if (minutaEncontrada != null) {
                minuta = minutaEncontrada;
                // Convertir LocalDateTime a Date para los calendarios
                if (minuta.getFechaRecibo() != null) {
                    fechaReciboDate = Date.from(minuta.getFechaRecibo().atZone(ZoneId.systemDefault()).toInstant());
                }
                if (minuta.getFechaEntrega() != null) {
                    fechaEntregaDate = Date.from(minuta.getFechaEntrega().atZone(ZoneId.systemDefault()).toInstant());
                }
            }
        }
    }

    public void eliminar(int idMinuta) {
        try {
            boolean exito = minutaDAO.eliminar(idMinuta);
            if (exito) {
                minutas = minutaDAO.listar();
                FacesUtils.addInfoMessage("✅ Minuta eliminada correctamente");
            } else {
                FacesUtils.addErrorMessage("❌ No se pudo eliminar la minuta");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar la minuta: " + e.getMessage());
            System.err.println("❌ MinutaBean.eliminar: Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void eliminar() {
        eliminar(idMinutaSeleccionada);
    }
    
    public int getIdMinutaSeleccionada() {
        return idMinutaSeleccionada;
    }
    
    public void setIdMinutaSeleccionada(int idMinutaSeleccionada) {
        this.idMinutaSeleccionada = idMinutaSeleccionada;
    }
    
    // Métodos helper para formatear fechas
    public String formatearFecha(LocalDateTime fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    // Getters y Setters para propiedades Date
    public Date getFechaReciboDate() {
        if (fechaReciboDate == null && minuta.getFechaRecibo() != null) {
            fechaReciboDate = Date.from(minuta.getFechaRecibo().atZone(ZoneId.systemDefault()).toInstant());
        }
        return fechaReciboDate;
    }
    
    public void setFechaReciboDate(Date fechaReciboDate) {
        this.fechaReciboDate = fechaReciboDate;
        if (fechaReciboDate != null) {
            minuta.setFechaRecibo(fechaReciboDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
    }
    
    public Date getFechaEntregaDate() {
        if (fechaEntregaDate == null && minuta.getFechaEntrega() != null) {
            fechaEntregaDate = Date.from(minuta.getFechaEntrega().atZone(ZoneId.systemDefault()).toInstant());
        }
        return fechaEntregaDate;
    }
    
    public void setFechaEntregaDate(Date fechaEntregaDate) {
        this.fechaEntregaDate = fechaEntregaDate;
        if (fechaEntregaDate != null) {
            minuta.setFechaEntrega(fechaEntregaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
    }

    // Getters y Setters
    public Minuta getMinuta() {
        if (minuta == null) {
            minuta = new Minuta();
        }
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public List<Minuta> getMinutas() {
        if (minutas == null) {
            minutas = minutaDAO.listar();
        }
        if (minutasFiltradas != null && !minutasFiltradas.isEmpty()) {
            return minutasFiltradas;
        }
        return minutas;
    }

    public void setMinutasFiltradas(List<Minuta> minutasFiltradas) {
        this.minutasFiltradas = minutasFiltradas;
    }

    public List<GuardaSeguridad> getGuardas() {
        if (guardas == null) {
            guardas = guardaDAO.listar();
        }
        return guardas;
    }
    
    public List<Instructor> getInstructores() {
        if (instructores == null) {
            instructores = instructorDAO.listar();
        }
        return instructores;
    }

    public List<Ambiente> getAmbientes() {
        if (ambientes == null) {
            ambientes = ambienteDAO.listar();
        }
        return ambientes;
    }
    
    public Integer getIdMinutaEditar() {
        return idMinutaEditar;
    }
    
    public void setIdMinutaEditar(Integer idMinutaEditar) {
        this.idMinutaEditar = idMinutaEditar;
    }
    
    public boolean isModoEdicion() {
        return idMinutaEditar != null && idMinutaEditar > 0;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}
