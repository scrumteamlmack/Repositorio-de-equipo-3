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
        System.out.println("🔍 GuardaSeguridadBean.init: Inicializando bean");
        System.out.println("   - idUsuario recibido: " + idUsuario);
        
        turnos = new ArrayList<>();
        turnos.add("Mañana");
        turnos.add("Tarde");
        turnos.add("Noche");
        
        // Si no hay idUsuario, intentar obtenerlo de la URL manualmente
        if (idUsuario == null || idUsuario == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuario = Integer.parseInt(idParam);
                        System.out.println("   - idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ GuardaSeguridadBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
        if (idUsuario != null && idUsuario > 0) {
            GuardaSeguridad existente = guardaSeguridadDAO.buscarPorUsuario(idUsuario);
            if (existente != null) {
                System.out.println("   - Guarda de seguridad existente encontrado");
                guardaSeguridad = existente;
            } else {
                System.out.println("   - Creando nuevo guarda de seguridad para usuario ID: " + idUsuario);
                guardaSeguridad = new GuardaSeguridad();
                guardaSeguridad.setIdUsuario(idUsuario);
                guardaSeguridad.setEstado("Activo");
                guardaSeguridad.setFechaIngreso(new Date());
            }
        } else {
            System.err.println("⚠️ GuardaSeguridadBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
            guardaSeguridad.setEstado("Activo");
            guardaSeguridad.setFechaIngreso(new Date());
        }
    }

    public String guardar() {
        System.out.println("🔍 GuardaSeguridadBean.guardar: Iniciando guardado");
        System.out.println("   - idUsuario en bean: " + idUsuario);
        System.out.println("   - guardaSeguridad.getIdUsuario(): " + guardaSeguridad.getIdUsuario());
        System.out.println("   - guardaSeguridad.getTurno(): " + guardaSeguridad.getTurno());
        System.out.println("   - guardaSeguridad.getFechaIngreso(): " + guardaSeguridad.getFechaIngreso());
        System.out.println("   - guardaSeguridad.getEstado(): " + guardaSeguridad.getEstado());
        
        // Intentar obtener idUsuario de la URL si no está establecido
        if ((guardaSeguridad.getIdUsuario() == 0) && (idUsuario == null || idUsuario == 0)) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuario = Integer.parseInt(idParam);
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
            guardaSeguridad.setIdUsuario(idUsuario);
        }
        
        if (guardaSeguridad.getIdUsuario() == 0) {
            System.err.println("❌ GuardaSeguridadBean.guardar: idUsuario es 0 después de todos los intentos");
            FacesUtils.addErrorMessage("Error: No se pudo identificar el usuario. Por favor, intente nuevamente.");
            return null;
        }
        
        if (guardaSeguridad.getTurno() == null || guardaSeguridad.getTurno().isEmpty()) {
            System.err.println("❌ GuardaSeguridadBean.guardar: turno está vacío");
            FacesUtils.addErrorMessage("Debe seleccionar un turno.");
            return null;
        }
        
        if (guardaSeguridad.getFechaIngreso() == null) {
            System.err.println("❌ GuardaSeguridadBean.guardar: fechaIngreso es null");
            FacesUtils.addErrorMessage("Debe seleccionar una fecha de ingreso.");
            return null;
        }

        boolean guardado;
        GuardaSeguridad existente = guardaSeguridadDAO.buscarPorUsuario(guardaSeguridad.getIdUsuario());
        
        if (existente == null) {
            System.out.println("   - Creando nuevo registro de guarda de seguridad");
            guardado = guardaSeguridadDAO.guardar(guardaSeguridad);
        } else {
            System.out.println("   - Actualizando registro existente");
            guardado = guardaSeguridadDAO.actualizar(guardaSeguridad);
        }

        if (guardado) {
            System.out.println("✅ GuardaSeguridadBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Guarda de seguridad registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("❌ GuardaSeguridadBean.guardar: No se pudo guardar");
            FacesUtils.addErrorMessage("No fue posible guardar los datos del guarda de seguridad. Verifique los logs del servidor.");
            return null;
        }
    }

    // GETTERS Y SETTERS

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
