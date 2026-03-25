package beans;

import dao.GuardaSeguridadDAO;
import modelo.GuardaSeguridad;
import util.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "guardaSeguridadBean")
@ViewScoped
public class GuardaSeguridadBean implements Serializable {

    private final GuardaSeguridadDAO guardaSeguridadDAO = new GuardaSeguridadDAO();

    private GuardaSeguridad guardaSeguridad = new GuardaSeguridad();
    private Integer idUsuario;
    private List<String> turnos;

    public void init() {
<<<<<<< HEAD
        System.out.println("GuardaSeguridadBean.init: Inicializando bean");
        System.out.println("idUsuario recibido: " + idUsuario);
=======
        System.out.println("🔍 GuardaSeguridadBean.init: Inicializando bean");
        System.out.println("   - idUsuario recibido: " + idUsuario);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        
        turnos = new ArrayList<>();
        turnos.add("Mañana");
        turnos.add("Tarde");
        turnos.add("Noche");
        
<<<<<<< HEAD
        
=======
        // Si no hay idUsuario, intentar obtenerlo de la URL manualmente
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (idUsuario == null || idUsuario == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuario = Integer.parseInt(idParam);
<<<<<<< HEAD
                        System.out.println(" idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("GuardaSeguridadBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
=======
                        System.out.println("   - idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ GuardaSeguridadBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            }
        }
        
        if (idUsuario != null && idUsuario > 0) {
            GuardaSeguridad existente = guardaSeguridadDAO.buscarPorUsuario(idUsuario);
            if (existente != null) {
<<<<<<< HEAD
                System.out.println(" Guarda de seguridad existente encontrado");
                guardaSeguridad = existente;
            } else {
                System.out.println(" Creando nuevo guarda de seguridad para usuario ID: " + idUsuario);
=======
                System.out.println("   - Guarda de seguridad existente encontrado");
                guardaSeguridad = existente;
            } else {
                System.out.println("   - Creando nuevo guarda de seguridad para usuario ID: " + idUsuario);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                guardaSeguridad = new GuardaSeguridad();
                guardaSeguridad.setIdUsuario(idUsuario);
                guardaSeguridad.setEstado("Activo");
                guardaSeguridad.setFechaIngreso(new Date());
            }
        } else {
<<<<<<< HEAD
            System.err.println(" GuardaSeguridadBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
=======
            System.err.println("⚠️ GuardaSeguridadBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            guardaSeguridad.setEstado("Activo");
            guardaSeguridad.setFechaIngreso(new Date());
        }
    }

    public String guardar() {
<<<<<<< HEAD
        System.out.println("GuardaSeguridadBean.guardar: Iniciando guardado");
        System.out.println(" idUsuario en bean: " + idUsuario);
        System.out.println(" guardaSeguridad.getIdUsuario(): " + guardaSeguridad.getIdUsuario());
        System.out.println(" guardaSeguridad.getTurno(): " + guardaSeguridad.getTurno());
        System.out.println(" guardaSeguridad.getFechaIngreso(): " + guardaSeguridad.getFechaIngreso());
        System.out.println(" guardaSeguridad.getEstado(): " + guardaSeguridad.getEstado());
        
        
=======
        System.out.println("🔍 GuardaSeguridadBean.guardar: Iniciando guardado");
        System.out.println("   - idUsuario en bean: " + idUsuario);
        System.out.println("   - guardaSeguridad.getIdUsuario(): " + guardaSeguridad.getIdUsuario());
        System.out.println("   - guardaSeguridad.getTurno(): " + guardaSeguridad.getTurno());
        System.out.println("   - guardaSeguridad.getFechaIngreso(): " + guardaSeguridad.getFechaIngreso());
        System.out.println("   - guardaSeguridad.getEstado(): " + guardaSeguridad.getEstado());
        
        // Intentar obtener idUsuario de la URL si no está establecido
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if ((guardaSeguridad.getIdUsuario() == 0) && (idUsuario == null || idUsuario == 0)) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuario = Integer.parseInt(idParam);
<<<<<<< HEAD
                        System.out.println("idUsuario obtenido de URL en guardar(): " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("GuardaSeguridadBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
        if (guardaSeguridad.getIdUsuario() == 0 && idUsuario != null && idUsuario > 0) {
            System.out.println(" Estableciendo idUsuario desde variable: " + idUsuario);
=======
                        System.out.println("   - idUsuario obtenido de URL en guardar(): " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ GuardaSeguridadBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
        // Establecer idUsuario en guardaSeguridad si está disponible
        if (guardaSeguridad.getIdUsuario() == 0 && idUsuario != null && idUsuario > 0) {
            System.out.println("   - Estableciendo idUsuario desde variable: " + idUsuario);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            guardaSeguridad.setIdUsuario(idUsuario);
        }
        
        if (guardaSeguridad.getIdUsuario() == 0) {
<<<<<<< HEAD
            System.err.println("GuardaSeguridadBean.guardar: idUsuario es 0 después de todos los intentos");
=======
            System.err.println("❌ GuardaSeguridadBean.guardar: idUsuario es 0 después de todos los intentos");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Error: No se pudo identificar el usuario. Por favor, intente nuevamente.");
            return null;
        }
        
        if (guardaSeguridad.getTurno() == null || guardaSeguridad.getTurno().isEmpty()) {
<<<<<<< HEAD
            System.err.println("GuardaSeguridadBean.guardar: turno está vacío");
=======
            System.err.println("❌ GuardaSeguridadBean.guardar: turno está vacío");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Debe seleccionar un turno.");
            return null;
        }
        
        if (guardaSeguridad.getFechaIngreso() == null) {
<<<<<<< HEAD
            System.err.println("GuardaSeguridadBean.guardar: fechaIngreso es null");
=======
            System.err.println("❌ GuardaSeguridadBean.guardar: fechaIngreso es null");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Debe seleccionar una fecha de ingreso.");
            return null;
        }

        boolean guardado;
        GuardaSeguridad existente = guardaSeguridadDAO.buscarPorUsuario(guardaSeguridad.getIdUsuario());
        
        if (existente == null) {
<<<<<<< HEAD
            System.out.println("  Creando nuevo registro de guarda de seguridad");
            guardado = guardaSeguridadDAO.guardar(guardaSeguridad);
        } else {
            System.out.println("  Actualizando registro existente");
=======
            System.out.println("   - Creando nuevo registro de guarda de seguridad");
            guardado = guardaSeguridadDAO.guardar(guardaSeguridad);
        } else {
            System.out.println("   - Actualizando registro existente");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            guardado = guardaSeguridadDAO.actualizar(guardaSeguridad);
        }

        if (guardado) {
<<<<<<< HEAD
            System.out.println("GuardaSeguridadBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Guarda de seguridad registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("GuardaSeguridadBean.guardar: No se pudo guardar");
=======
            System.out.println("✅ GuardaSeguridadBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Guarda de seguridad registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("❌ GuardaSeguridadBean.guardar: No se pudo guardar");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("No fue posible guardar los datos del guarda de seguridad. Verifique los logs del servidor.");
            return null;
        }
    }

<<<<<<< HEAD
    
=======
    // GETTERS Y SETTERS
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf

    public GuardaSeguridad getGuardaSeguridad() {
        return guardaSeguridad;
    }

    public void setGuardaSeguridad(GuardaSeguridad guardaSeguridad) {
        this.guardaSeguridad = guardaSeguridad;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<String> getTurnos() {
        return turnos;
    }
}
