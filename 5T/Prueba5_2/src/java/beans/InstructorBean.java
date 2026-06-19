package beans;

import dao.InstructorDAO;
import dao.CoordinacionDAO;
import dao.UsuarioDAO;
import modelo.Instructor;
import modelo.Coordinacion;
import modelo.Usuario;
import util.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "instructorBean")
@ViewScoped
public class InstructorBean implements Serializable {

    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final CoordinacionDAO coordinacionDAO = new CoordinacionDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private Instructor instructor = new Instructor();
    private Integer idUsuario;
    private List<Coordinacion> coordinaciones;

    public void init() {
<<<<<<< HEAD
        System.out.println("InstructorBean.init: Inicializando bean");
        System.out.println(" idUsuario recibido: " + idUsuario);
        
        coordinaciones = coordinacionDAO.listar();
        System.out.println("  Coordinaciones cargadas: " + (coordinaciones != null ? coordinaciones.size() : 0));
        
        
=======
        System.out.println("🔍 InstructorBean.init: Inicializando bean");
        System.out.println("   - idUsuario recibido: " + idUsuario);
        
        coordinaciones = coordinacionDAO.listar();
        System.out.println("   - Coordinaciones cargadas: " + (coordinaciones != null ? coordinaciones.size() : 0));
        
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
                System.err.println(" InstructorBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
=======
                        System.out.println("   - idUsuario obtenido de parámetro URL: " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ InstructorBean.init: Error al obtener idUsuario de URL: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            }
        }
        
        if (idUsuario != null && idUsuario > 0) {
            Instructor existente = instructorDAO.buscarPorUsuario(idUsuario);
            if (existente != null) {
<<<<<<< HEAD
                System.out.println(" Instructor existente encontrado");
                instructor = existente;
            } else {
                System.out.println(" Creando nuevo instructor para usuario ID: " + idUsuario);
=======
                System.out.println("   - Instructor existente encontrado");
                instructor = existente;
            } else {
                System.out.println("   - Creando nuevo instructor para usuario ID: " + idUsuario);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                instructor = new Instructor();
                instructor.setIdUsuario(idUsuario);
                instructor.setEstado("Activo");
                
<<<<<<< HEAD
                
=======
                // Cargar el correo del usuario desde la base de datos
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
                if (usuario != null && usuario.getCorreo() != null && !usuario.getCorreo().isEmpty()) {
                    instructor.setEmail(usuario.getCorreo());
                    System.out.println("   - Correo del usuario cargado: " + usuario.getCorreo());
                } else {
<<<<<<< HEAD
                    System.err.println("InstructorBean.init: No se pudo cargar el correo del usuario");
                }
            }
        } else {
            System.err.println(" InstructorBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
=======
                    System.err.println("⚠️ InstructorBean.init: No se pudo cargar el correo del usuario");
                }
            }
        } else {
            System.err.println("⚠️ InstructorBean.init: idUsuario es null o 0 - no se pudo obtener de la URL");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            instructor.setEstado("Activo");
        }
    }

    public String guardar() {
<<<<<<< HEAD
        System.out.println("InstructorBean.guardar: Iniciando guardado");
        System.out.println("idUsuario en bean: " + idUsuario);
        System.out.println("instructor.getIdUsuario(): " + instructor.getIdUsuario());
        System.out.println("instructor.getEmail(): " + instructor.getEmail());
        System.out.println("instructor.getTelefono(): " + instructor.getTelefono());
        System.out.println("instructor.getCoordinacionId(): " + instructor.getCoordinacionId());
        
       
=======
        System.out.println("🔍 InstructorBean.guardar: Iniciando guardado");
        System.out.println("   - idUsuario en bean: " + idUsuario);
        System.out.println("   - instructor.getIdUsuario(): " + instructor.getIdUsuario());
        System.out.println("   - instructor.getEmail(): " + instructor.getEmail());
        System.out.println("   - instructor.getTelefono(): " + instructor.getTelefono());
        System.out.println("   - instructor.getCoordinacionId(): " + instructor.getCoordinacionId());
        
        // Intentar obtener idUsuario de la URL si no está establecido
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if ((instructor.getIdUsuario() == 0) && (idUsuario == null || idUsuario == 0)) {
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
                System.err.println("InstructorBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
        
=======
                        System.out.println("   - idUsuario obtenido de URL en guardar(): " + idUsuario);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ InstructorBean.guardar: Error al obtener idUsuario de URL: " + e.getMessage());
            }
        }
        
        // Establecer idUsuario en instructor si está disponible
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (instructor.getIdUsuario() == 0 && idUsuario != null && idUsuario > 0) {
            System.out.println("   - Estableciendo idUsuario desde variable: " + idUsuario);
            instructor.setIdUsuario(idUsuario);
        }
        
        if (instructor.getIdUsuario() == 0) {
<<<<<<< HEAD
            System.err.println("InstructorBean.guardar: idUsuario es 0 después de todos los intentos");
=======
            System.err.println("❌ InstructorBean.guardar: idUsuario es 0 después de todos los intentos");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Error: No se pudo identificar el usuario. Por favor, intente nuevamente.");
            return null;
        }
        
        if (instructor.getEmail() == null || instructor.getEmail().isEmpty()) {
<<<<<<< HEAD
            System.err.println("InstructorBean.guardar: email está vacío");
=======
            System.err.println("❌ InstructorBean.guardar: email está vacío");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Debe ingresar un correo electrónico.");
            return null;
        }
        
        if (instructor.getTelefono() == null || instructor.getTelefono().isEmpty()) {
<<<<<<< HEAD
            System.err.println("InstructorBean.guardar: telefono está vacío");
=======
            System.err.println("❌ InstructorBean.guardar: telefono está vacío");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("Debe ingresar un teléfono.");
            return null;
        }
        
        if (instructor.getCoordinacionId() == 0) {
            System.err.println("❌ InstructorBean.guardar: coordinacionId es 0");
            FacesUtils.addErrorMessage("Debe seleccionar una coordinación.");
            return null;
        }

        boolean guardado;
        Instructor existente = instructorDAO.buscarPorUsuario(instructor.getIdUsuario());
        
        if (existente == null) {
            System.out.println("   - Creando nuevo registro de instructor");
            guardado = instructorDAO.guardar(instructor);
        } else {
            System.out.println("   - Actualizando registro existente");
            guardado = instructorDAO.actualizar(instructor);
        }

        if (guardado) {
<<<<<<< HEAD
            System.out.println("InstructorBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Instructor registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("InstructorBean.guardar: No se pudo guardar");
=======
            System.out.println("✅ InstructorBean.guardar: Guardado exitoso");
            FacesUtils.addInfoMessage("Instructor registrado correctamente.");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else {
            System.err.println("❌ InstructorBean.guardar: No se pudo guardar");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            FacesUtils.addErrorMessage("No fue posible guardar los datos del instructor. Verifique los logs del servidor.");
            return null;
        }
    }

<<<<<<< HEAD
    
=======
    // GETTERS Y SETTERS
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
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
