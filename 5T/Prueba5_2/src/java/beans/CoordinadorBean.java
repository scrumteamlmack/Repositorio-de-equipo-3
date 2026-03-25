package beans;

import dao.CoordinadorDAO;
import dao.CoordinacionDAO;
import modelo.Coordinador;
import modelo.Coordinacion;
import util.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "coordinadorBean")
@ViewScoped
public class CoordinadorBean implements Serializable {

    private final CoordinadorDAO coordinadorDAO = new CoordinadorDAO();
    private final CoordinacionDAO coordinacionDAO = new CoordinacionDAO();

    private Coordinador coordinador = new Coordinador();
    private Integer idUsuario;
    private List<Coordinacion> coordinaciones;

    public void init() {
<<<<<<< HEAD
        System.out.println("CoordinadorBean.init: Inicializando bean");
        System.out.println(" idUsuario recibido: " + idUsuario);
=======
        System.out.println("🔍 CoordinadorBean.init: Inicializando bean");
        System.out.println("   - idUsuario recibido: " + idUsuario);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        
        coordinaciones = coordinacionDAO.listar();
        System.out.println("   - Coordinaciones cargadas: " + (coordinaciones != null ? coordinaciones.size() : 0));
        
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
                        System.out.println("idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("CoordinadorBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
=======
                        System.out.println("   - idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ CoordinadorBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            }
        }
        
        if (idUsuario != null && idUsuario > 0) {
            Coordinador existente = coordinadorDAO.buscarPorUsuario(idUsuario);
            if (existente != null) {
<<<<<<< HEAD
                System.out.println(" Coordinador existente encontrado");
                coordinador = existente;
            } else {
                System.out.println(" Creando nuevo coordinador para usuario ID: " + idUsuario);
=======
                System.out.println("   - Coordinador existente encontrado");
                coordinador = existente;
            } else {
                System.out.println("   - Creando nuevo coordinador para usuario ID: " + idUsuario);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                coordinador = new Coordinador();
                coordinador.setIdUsuario(idUsuario);
            }
        } else {
<<<<<<< HEAD
            System.err.println("CoordinadorBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
=======
            System.err.println("⚠️ CoordinadorBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        }
    }

    public String guardar() {
<<<<<<< HEAD
        System.out.println("CoordinadorBean.guardar: Iniciando guardado");
        System.out.println(" idUsuario en bean: " + idUsuario);
        System.out.println(" coordinador.getIdUsuario(): " + coordinador.getIdUsuario());
        System.out.println(" coordinador.getCoordinacionId(): " + coordinador.getCoordinacionId());
        
        
=======
        System.out.println("🔍 CoordinadorBean.guardar: Iniciando guardado");
        System.out.println("   - idUsuario en bean: " + idUsuario);
        System.out.println("   - coordinador.getIdUsuario(): " + coordinador.getIdUsuario());
        System.out.println("   - coordinador.getCoordinacionId(): " + coordinador.getCoordinacionId());
        
        // Intentar obtener idUsuario de la URL si no está establecido
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if ((coordinador.getIdUsuario() == 0) && (idUsuario == null || idUsuario == 0)) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuario = Integer.parseInt(idParam);
<<<<<<< HEAD
                        System.out.println(" idUsuario obtenido de URL en guardar(): " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("CoordinadorBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
    
=======
                        System.out.println("   - idUsuario obtenido de URL en guardar(): " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ CoordinadorBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
        // Establecer idUsuario en coordinador si está disponible
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (coordinador.getIdUsuario() == 0 && idUsuario != null && idUsuario > 0) {
            System.out.println("   - Estableciendo idUsuario desde variable: " + idUsuario);
            coordinador.setIdUsuario(idUsuario);
        }
        
        if (coordinador.getIdUsuario() == 0) {
<<<<<<< HEAD
            System.err.println("CoordinadorBean.guardar: idUsuario es 0 después de todos los intentos");
=======
            System.err.println("❌ CoordinadorBean.guardar: idUsuario es 0 después de todos los intentos");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Error: No se pudo identificar el usuario. Por favor, intente nuevamente.");
            return null;
        }
        
        if (coordinador.getCoordinacionId() == 0) {
<<<<<<< HEAD
            System.err.println("CoordinadorBean.guardar: coordinacionId es 0");
=======
            System.err.println("❌ CoordinadorBean.guardar: coordinacionId es 0");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Debe seleccionar una coordinación.");
            return null;
        }

        boolean guardado;
        Coordinador existente = coordinadorDAO.buscarPorUsuario(coordinador.getIdUsuario());
        
        if (existente == null) {
<<<<<<< HEAD
            System.out.println(" Creando nuevo registro de coordinador");
            guardado = coordinadorDAO.guardar(coordinador);
        } else {
            System.out.println(" Actualizando registro existente");
=======
            System.out.println("   - Creando nuevo registro de coordinador");
            guardado = coordinadorDAO.guardar(coordinador);
        } else {
            System.out.println("   - Actualizando registro existente");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            guardado = coordinadorDAO.actualizar(coordinador);
        }

        if (guardado) {
<<<<<<< HEAD
            System.out.println("CoordinadorBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Coordinador registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("CoordinadorBean.guardar: No se pudo guardar");
=======
            System.out.println("✅ CoordinadorBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Coordinador registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("❌ CoordinadorBean.guardar: No se pudo guardar");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("No fue posible guardar los datos del coordinador. Verifique los logs del servidor.");
            return null;
        }
    }

<<<<<<< HEAD

=======
    // GETTERS Y SETTERS
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Coordinacion> getCoordinaciones() {
        return coordinaciones;
    }
}
