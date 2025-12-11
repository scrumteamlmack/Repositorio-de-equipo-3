package beans;

import dao.AmbienteDAO;
import dao.RecursoDAO;
import dao.TrasladoRecursoDAO;
import modelo.Ambiente;
import modelo.Recurso;
import modelo.TrasladoRecurso;
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

@ManagedBean(name = "trasladoRecursoBean")
@ViewScoped
public class TrasladoRecursoBean implements Serializable {

    private final TrasladoRecursoDAO trasladoDAO = new TrasladoRecursoDAO();
    private final RecursoDAO recursoDAO = new RecursoDAO();
    private final AmbienteDAO ambienteDAO = new AmbienteDAO();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private TrasladoRecurso traslado = new TrasladoRecurso();
    private Integer idTrasladoEditar;
    
    // Propiedad Date para el calendario (conversión Date <-> LocalDateTime)
    private Date fechaTrasladoDate;
    
    private boolean inicializado = false;

    private List<TrasladoRecurso> traslados;
    private List<TrasladoRecurso> trasladosFiltrados;
    private List<Recurso> recursos;
    private List<Ambiente> ambientes;

    private void cargarDatos() {
        try {
            System.out.println("🔍 TrasladoRecursoBean.cargarDatos: Iniciando carga de datos");
            
            traslados = trasladoDAO.listar();
            trasladosFiltrados = null;
            System.out.println("   - Traslados cargados: " + (traslados != null ? traslados.size() : 0));
            
            recursos = recursoDAO.listar();
            System.out.println("   - Recursos cargados: " + (recursos != null ? recursos.size() : 0));
            
            ambientes = ambienteDAO.listar();
            System.out.println("   - Ambientes cargados: " + (ambientes != null ? ambientes.size() : 0));
        } catch (Exception e) {
            System.err.println("❌ TrasladoRecursoBean.cargarDatos: Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("🔍 TrasladoRecursoBean.init: @PostConstruct ejecutado");
        System.out.println("   - idTrasladoEditar recibido: " + idTrasladoEditar);
        
        cargarDatos();
        
        // Si no hay idTrasladoEditar, intentar obtenerlo de la URL manualmente
        if (idTrasladoEditar == null || idTrasladoEditar == 0) {
            try {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("idTrasladoEditar");
                    if (idParam == null || idParam.isEmpty()) {
                        idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    }
                    if (idParam != null && !idParam.isEmpty()) {
                        idTrasladoEditar = Integer.parseInt(idParam);
                        System.out.println("   - idTrasladoEditar obtenido de URL: " + idTrasladoEditar);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ TrasladoRecursoBean.init: Error al obtener idTrasladoEditar de URL: " + e.getMessage());
            }
        }
        
        if (idTrasladoEditar != null && idTrasladoEditar > 0) {
            // Modo edición: cargar el traslado
            TrasladoRecurso encontrado = trasladoDAO.buscarPorId(idTrasladoEditar);
            if (encontrado != null) {
                traslado = encontrado;
                // Convertir LocalDateTime a Date para el calendario
                if (traslado.getFechaTraslado() != null) {
                    fechaTrasladoDate = Date.from(traslado.getFechaTraslado().atZone(ZoneId.systemDefault()).toInstant());
                }
                System.out.println("   ✅ Traslado cargado para edición: ID " + idTrasladoEditar);
            } else {
                System.err.println("   ⚠️ No se encontró traslado con ID: " + idTrasladoEditar);
                prepararNuevo();
            }
        } else {
            // Modo creación: preparar nuevo traslado
            prepararNuevo();
        }
        
        inicializado = true;
    }

    public void prepararNuevo() {
        traslado = new TrasladoRecurso();
        traslado.setFechaTraslado(LocalDateTime.now());
        fechaTrasladoDate = new Date();
        traslado.setRecursoId(0);
        traslado.setAmbienteOrigen(0);
        traslado.setAmbienteDestino(0);
        idTrasladoEditar = null;
    }

    public void guardar() {
        try {
            // Convertir Date a LocalDateTime antes de guardar
            if (fechaTrasladoDate != null) {
                traslado.setFechaTraslado(fechaTrasladoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            
            // Validar campos requeridos
            if (traslado.getRecursoId() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un recurso");
                return;
            }
            if (traslado.getAmbienteOrigen() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un ambiente de origen");
                return;
            }
            if (traslado.getAmbienteDestino() == 0) {
                FacesUtils.addErrorMessage("⚠️ Debe seleccionar un ambiente de destino");
                return;
            }
            if (traslado.getAmbienteOrigen() == traslado.getAmbienteDestino()) {
                FacesUtils.addErrorMessage("⚠️ El ambiente de origen y destino no pueden ser el mismo");
                return;
            }
            
            String mensaje;
            if (traslado.getIdTraslado() == 0) {
                int id = trasladoDAO.guardar(traslado);
                if (id > 0) {
                    mensaje = "✅ Traslado registrado correctamente";
                } else {
                    FacesUtils.addErrorMessage("❌ No se pudo registrar el traslado");
                    return;
                }
            } else {
                boolean exito = trasladoDAO.actualizar(traslado);
                if (exito) {
                    mensaje = "✅ Traslado actualizado correctamente";
                } else {
                    FacesUtils.addErrorMessage("❌ No se pudo actualizar el traslado");
                    return;
                }
            }
            
            traslados = trasladoDAO.listar();
            prepararNuevo();
            
            // Guardar mensaje en Flash para que sobreviva la redirección
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().getFlash().put("mensaje", mensaje);
            
            // Detectar si estamos en el contexto de instructor o guarda
            String currentView = facesContext.getViewRoot().getViewId();
            if (currentView != null && currentView.contains("/instructor/")) {
                FacesUtils.redirect("/faces/pages/instructor/traslados/listarTraslados.xhtml");
            } else {
                FacesUtils.redirect("/faces/pages/guarda/traslados/listarTraslados.xhtml");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al guardar el traslado: " + e.getMessage());
            System.err.println("❌ TrasladoRecursoBean.guardar: Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String editar(int idTraslado) {
        // Detectar si estamos en el contexto de instructor o guarda
        String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        String currentView = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        
        if (currentView != null && currentView.contains("/instructor/")) {
            return "/pages/instructor/traslados/formTraslado.xhtml?faces-redirect=true&idTrasladoEditar=" + idTraslado;
        } else {
            return "/pages/guarda/traslados/formTraslado.xhtml?faces-redirect=true&idTrasladoEditar=" + idTraslado;
        }
    }

    public void eliminar(int idTraslado) {
        try {
            boolean exito = trasladoDAO.eliminar(idTraslado);
            if (exito) {
                traslados = trasladoDAO.listar();
                FacesUtils.addInfoMessage("✅ Traslado eliminado correctamente");
            } else {
                FacesUtils.addErrorMessage("❌ No se pudo eliminar el traslado");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al eliminar el traslado: " + e.getMessage());
            System.err.println("❌ TrasladoRecursoBean.eliminar: Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método helper para formatear fechas
    public String formatearFecha(LocalDateTime fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    // Métodos helper para filtros - aseguran que siempre haya un valor para filtrar
    public String getTextoRecurso(TrasladoRecurso tras) {
        return tras.getRecursoNombre() != null ? tras.getRecursoNombre() : "Recurso " + tras.getRecursoId();
    }
    
    public String getTextoAmbienteOrigen(TrasladoRecurso tras) {
        return tras.getAmbienteOrigenNombre() != null ? tras.getAmbienteOrigenNombre() : "Ambiente " + tras.getAmbienteOrigen();
    }
    
    public String getTextoAmbienteDestino(TrasladoRecurso tras) {
        return tras.getAmbienteDestinoNombre() != null ? tras.getAmbienteDestinoNombre() : "Ambiente " + tras.getAmbienteDestino();
    }
    
    // Getter y Setter para propiedad Date
    public Date getFechaTrasladoDate() {
        if (fechaTrasladoDate == null && traslado.getFechaTraslado() != null) {
            fechaTrasladoDate = Date.from(traslado.getFechaTraslado().atZone(ZoneId.systemDefault()).toInstant());
        }
        return fechaTrasladoDate;
    }
    
    public void setFechaTrasladoDate(Date fechaTrasladoDate) {
        this.fechaTrasladoDate = fechaTrasladoDate;
        if (fechaTrasladoDate != null) {
            traslado.setFechaTraslado(fechaTrasladoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
    }

    // Getters y Setters
    public TrasladoRecurso getTraslado() {
        if (traslado == null) {
            traslado = new TrasladoRecurso();
        }
        return traslado;
    }

    public void setTraslado(TrasladoRecurso traslado) {
        this.traslado = traslado;
    }

    public List<TrasladoRecurso> getTraslados() {
        if (traslados == null) {
            traslados = trasladoDAO.listar();
        }
        return traslados;
    }

    public List<TrasladoRecurso> getTrasladosFiltrados() {
        return trasladosFiltrados;
    }

    public void setTrasladosFiltrados(List<TrasladoRecurso> trasladosFiltrados) {
        this.trasladosFiltrados = trasladosFiltrados;
    }

    public List<Recurso> getRecursos() {
        if (recursos == null) {
            recursos = recursoDAO.listar();
        }
        return recursos;
    }

    public List<Ambiente> getAmbientes() {
        if (ambientes == null) {
            ambientes = ambienteDAO.listar();
        }
        return ambientes;
    }
    
    public Integer getIdTrasladoEditar() {
        return idTrasladoEditar;
    }
    
    public void setIdTrasladoEditar(Integer idTrasladoEditar) {
        this.idTrasladoEditar = idTrasladoEditar;
    }
    
    public boolean isModoEdicion() {
        return idTrasladoEditar != null && idTrasladoEditar > 0;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}

