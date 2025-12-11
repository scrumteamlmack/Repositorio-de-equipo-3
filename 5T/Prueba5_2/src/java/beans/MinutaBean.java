package beans;

import dao.AmbienteDAO;
import dao.GuardaSeguridadDAO;
import dao.InstructorDAO;
import dao.MinutaDAO;
import dao.UsuarioDAO;
import modelo.Ambiente;
import modelo.GuardaSeguridad;
import modelo.Instructor;
import modelo.Minuta;
import modelo.Usuario;
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
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

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
    private List<Usuario> guardasUsuarios;
    private List<Usuario> instructoresUsuarios;

    private void cargarDatos() {
        try {
            System.out.println("🔍 MinutaBean.cargarDatos: Iniciando carga de datos");
            
            minutas = minutaDAO.listar();
            minutasFiltradas = null;
            System.out.println("   - Minutas cargadas: " + (minutas != null ? minutas.size() : 0));
            
            guardas = guardaDAO.listar();
            System.out.println("   - Guardas cargados: " + (guardas != null ? guardas.size() : 0));
            
            instructores = instructorDAO.listar();
            System.out.println("   - Instructores cargados: " + (instructores != null ? instructores.size() : 0));
            
            ambientes = ambienteDAO.listar();
            System.out.println("   - Ambientes cargados: " + (ambientes != null ? ambientes.size() : 0));
            
            // Cargar usuarios completos para mostrar nombres y apellidos
            guardasUsuarios = new java.util.ArrayList<>();
            for (GuardaSeguridad g : guardas) {
                Usuario u = usuarioDAO.buscarPorId(g.getIdUsuario());
                if (u != null) {
                    guardasUsuarios.add(u);
                }
            }
            System.out.println("   - Guardas usuarios cargados: " + (guardasUsuarios != null ? guardasUsuarios.size() : 0));
            
            instructoresUsuarios = new java.util.ArrayList<>();
            for (Instructor i : instructores) {
                Usuario u = usuarioDAO.buscarPorId(i.getIdUsuario());
                if (u != null) {
                    instructoresUsuarios.add(u);
                }
            }
            System.out.println("   - Instructores usuarios cargados: " + (instructoresUsuarios != null ? instructoresUsuarios.size() : 0));
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

    public String guardar() {
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
                return null;
            }
            if (minuta.getGuardaId() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un guarda de seguridad");
                return null;
            }
            if (minuta.getResponsableId() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un instructor responsable");
                return null;
            }
            
            if (minuta.getIdMinuta() == 0) {
                int id = minutaDAO.guardar(minuta);
                if (id > 0) {
                    FacesUtils.addInfoMessage("✅ Minuta registrada correctamente");
                } else {
                    FacesUtils.addErrorMessage("❌ No se pudo registrar la minuta");
                    return null;
                }
            } else {
                boolean exito = minutaDAO.actualizar(minuta);
                if (exito) {
                    FacesUtils.addInfoMessage("✅ Minuta actualizada correctamente");
                } else {
                    FacesUtils.addErrorMessage("❌ No se pudo actualizar la minuta");
                    return null;
                }
            }
            
            minutas = minutaDAO.listar();
            prepararNuevo();
            
            return "/pages/guarda/minutas/listarMinutas.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar la minuta: " + e.getMessage());
            System.err.println("❌ MinutaBean.guardar: Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String cargarMinutaParaEditar() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔍 MinutaBean.cargarMinutaParaEditar: INICIO");
        System.out.println("   - idMinutaEditar desde viewParam: " + idMinutaEditar);
        
        if (idMinutaEditar == null || idMinutaEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam == null || idParam.isEmpty()) {
                        idParam = facesContext.getExternalContext().getRequestParameterMap().get("idMinutaEditar");
                    }
                    System.out.println("   - Parámetro 'id' obtenido de URL: " + idParam);
                    if (idParam != null && !idParam.isEmpty()) {
                        idMinutaEditar = Integer.parseInt(idParam);
                        System.out.println("   ✅ idMinutaEditar parseado desde URL: " + idMinutaEditar);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ MinutaBean.cargarMinutaParaEditar: Error al obtener idMinutaEditar de URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        if (idMinutaEditar != null && idMinutaEditar > 0) {
            System.out.println("   - Llamando a minutaDAO.buscarPorId(" + idMinutaEditar + ")");
            Minuta encontrada = minutaDAO.buscarPorId(idMinutaEditar);
            
            if (encontrada != null) {
                System.out.println("   ✅ Minuta encontrada en BD:");
                System.out.println("      * ID: " + encontrada.getIdMinuta());
                System.out.println("      * Ambiente ID: " + encontrada.getAmbienteId());
                System.out.println("      * Guarda ID: " + encontrada.getGuardaId());
                System.out.println("      * Responsable ID: " + encontrada.getResponsableId());
                System.out.println("      * Estado: " + encontrada.getEstado());
                
                if (minuta == null) {
                    minuta = new Minuta();
                }
                minuta.setIdMinuta(encontrada.getIdMinuta());
                minuta.setAmbienteId(encontrada.getAmbienteId());
                minuta.setGuardaId(encontrada.getGuardaId());
                minuta.setResponsableId(encontrada.getResponsableId());
                minuta.setFechaRecibo(encontrada.getFechaRecibo());
                minuta.setFechaEntrega(encontrada.getFechaEntrega());
                minuta.setNovedad(encontrada.getNovedad());
                minuta.setDescripcion(encontrada.getDescripcion());
                minuta.setEstado(encontrada.getEstado());
                
                // Convertir LocalDateTime a Date para los calendarios
                if (minuta.getFechaRecibo() != null) {
                    fechaReciboDate = Date.from(minuta.getFechaRecibo().atZone(ZoneId.systemDefault()).toInstant());
                }
                if (minuta.getFechaEntrega() != null) {
                    fechaEntregaDate = Date.from(minuta.getFechaEntrega().atZone(ZoneId.systemDefault()).toInstant());
                }
                
                System.out.println("   ✅ Datos asignados al bean minuta");
                System.out.println("═══════════════════════════════════════════════════════");
            } else {
                System.err.println("   ❌ Minuta NO encontrada en BD con ID: " + idMinutaEditar);
                FacesUtils.addErrorMessage("Minuta no encontrada con ID: " + idMinutaEditar);
            }
        } else {
            System.err.println("   ❌ idMinutaEditar es null o 0 - No se puede cargar minuta");
            FacesUtils.addErrorMessage("No se proporcionó ID de minuta válido para editar.");
        }
        
        return null;
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
    
    // Métodos helper para filtros - aseguran que siempre haya un valor para filtrar
    public String getTextoAmbiente(Minuta min) {
        return min.getAmbienteNombre() != null ? min.getAmbienteNombre() : "Ambiente " + min.getAmbienteId();
    }
    
    public String getTextoGuarda(Minuta min) {
        return min.getGuardaNombre() != null ? min.getGuardaNombre() : "Guarda " + min.getGuardaId();
    }
    
    public String getTextoResponsable(Minuta min) {
        return min.getResponsableNombre() != null ? min.getResponsableNombre() : "Instructor " + min.getResponsableId();
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
        return minutas;
    }

    public List<Minuta> getMinutasFiltradas() {
        return minutasFiltradas;
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
    
    public List<Usuario> getGuardasUsuarios() {
        if (guardasUsuarios == null) {
            cargarDatos();
        }
        return guardasUsuarios;
    }
    
    public List<Usuario> getInstructoresUsuarios() {
        if (instructoresUsuarios == null) {
            cargarDatos();
        }
        return instructoresUsuarios;
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
