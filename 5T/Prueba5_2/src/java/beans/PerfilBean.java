package beans;

import dao.AprendizDAO;
import dao.FichaDAO;
import dao.ProgramaDAO;
import dao.UsuarioDAO;
import modelo.Aprendiz;
import modelo.Ficha;
import modelo.Programa;
import modelo.Usuario;
import util.FacesUtils;
import util.PasswordUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "perfilBean")
@ViewScoped
public class PerfilBean implements Serializable {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final AprendizDAO aprendizDAO = new AprendizDAO();
    private final ProgramaDAO programaDAO = new ProgramaDAO();
    private final FichaDAO fichaDAO = new FichaDAO();
    private Usuario usuario;
    private Aprendiz aprendiz;
    private Programa programa;
    private Ficha ficha;
    private String nuevaContrasena;
    private String confirmarContrasena;
    private boolean modoEdicion = false;
    private boolean esAprendiz = false;

    public void init() {
<<<<<<< HEAD
        System.out.println("PerfilBean.init: Inicializando perfil");
        

        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            System.err.println("PerfilBean.init: FacesContext es null");
=======
        System.out.println("🔍 PerfilBean.init: Inicializando perfil");
        
        // Obtener el usuario autenticado desde LoginBean
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            System.err.println("❌ PerfilBean.init: FacesContext es null");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            return;
        }

        try {
<<<<<<< HEAD
           
=======
            // Intentar obtener desde LoginBean
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            javax.el.ELContext elContext = facesContext.getELContext();
            javax.el.ExpressionFactory factory = facesContext.getApplication().getExpressionFactory();
            javax.el.ValueExpression ve = factory.createValueExpression(elContext, "#{loginBean.usuarioAutenticado}", Usuario.class);
            usuario = (Usuario) ve.getValue(elContext);
            
            if (usuario == null) {
<<<<<<< HEAD
=======
                // Si no se puede obtener desde LoginBean, intentar desde la sesión
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                Object userIdObj = facesContext.getExternalContext().getSessionMap().get("userId");
                if (userIdObj != null) {
                    int userId = (Integer) userIdObj;
                    usuario = usuarioDAO.buscarPorId(userId);
<<<<<<< HEAD
                    System.out.println("PerfilBean.init: Usuario cargado desde sesión - ID: " + userId);
                } else {
                    System.err.println("PerfilBean.init: No se pudo obtener el usuario autenticado");
=======
                    System.out.println("✅ PerfilBean.init: Usuario cargado desde sesión - ID: " + userId);
                } else {
                    System.err.println("❌ PerfilBean.init: No se pudo obtener el usuario autenticado");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                    FacesUtils.addErrorMessage("No se pudo cargar la información del usuario");
                    return;
                }
            } else {
<<<<<<< HEAD
                System.out.println("PerfilBean.init: Usuario cargado desde LoginBean - ID: " + usuario.getIdUsuario());
            }
=======
                System.out.println("✅ PerfilBean.init: Usuario cargado desde LoginBean - ID: " + usuario.getIdUsuario());
            }
            
            // Crear una copia para edición (para no modificar directamente el objeto de sesión)
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            if (usuario != null) {
                usuario = usuarioDAO.buscarPorId(usuario.getIdUsuario());
                nuevaContrasena = "";
                confirmarContrasena = "";
                
<<<<<<< HEAD
                
                List<Integer> roles = (List<Integer>) facesContext.getExternalContext().getSessionMap().get("roles");
                if (roles != null && roles.contains(3)) { 
=======
                // Verificar si el usuario es un aprendiz
                List<Integer> roles = (List<Integer>) facesContext.getExternalContext().getSessionMap().get("roles");
                if (roles != null && roles.contains(3)) { // Rol 3 = Aprendiz
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                    esAprendiz = true;
                    cargarDatosAprendiz();
                }
            }
        } catch (Exception e) {
<<<<<<< HEAD
            System.err.println("PerfilBean.init: Error al cargar usuario: " + e.getMessage());
=======
            System.err.println("❌ PerfilBean.init: Error al cargar usuario: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
            FacesUtils.addErrorMessage("Error al cargar la información del perfil");
        }
    }

    private void cargarDatosAprendiz() {
<<<<<<< HEAD
        System.out.println("PerfilBean.cargarDatosAprendiz: Cargando datos del aprendiz");
        
        if (usuario == null) {
            System.err.println("PerfilBean.cargarDatosAprendiz: Usuario es null");
=======
        System.out.println("🔍 PerfilBean.cargarDatosAprendiz: Cargando datos del aprendiz");
        
        if (usuario == null) {
            System.err.println("❌ PerfilBean.cargarDatosAprendiz: Usuario es null");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            return;
        }
        
        try {
<<<<<<< HEAD
           
            aprendiz = aprendizDAO.buscarPorUsuario(usuario.getIdUsuario());
            
            if (aprendiz != null) {
                System.out.println("PerfilBean.cargarDatosAprendiz: Aprendiz encontrado");
                System.out.println("Programa ID: " + aprendiz.getProgramaId());
                System.out.println("Ficha ID: " + aprendiz.getFichaId());
                
                
                if (aprendiz.getProgramaId() > 0) {
                    programa = programaDAO.buscarPorId(aprendiz.getProgramaId());
                    if (programa != null) {
                        System.out.println("Programa: " + programa.getNombrePrograma());
                    } else {
                        System.err.println("PerfilBean.cargarDatosAprendiz: No se encontró el programa con ID: " + aprendiz.getProgramaId());
                    }
                }
                
                
                if (aprendiz.getFichaId() > 0) {
                    ficha = fichaDAO.buscarPorId(aprendiz.getFichaId());
                    if (ficha != null) {
                        System.out.println(" Ficha: " + ficha.getNumFicha());
                    } else {
                        System.err.println("PerfilBean.cargarDatosAprendiz: No se encontró la ficha con ID: " + aprendiz.getFichaId());
                    }
                }
            } else {
                System.err.println("PerfilBean.cargarDatosAprendiz: No se encontró registro de aprendiz para el usuario ID: " + usuario.getIdUsuario());
            }
        } catch (Exception e) {
            System.err.println("PerfilBean.cargarDatosAprendiz: Error al cargar datos del aprendiz: " + e.getMessage());
=======
            // Buscar el registro del aprendiz
            aprendiz = aprendizDAO.buscarPorUsuario(usuario.getIdUsuario());
            
            if (aprendiz != null) {
                System.out.println("✅ PerfilBean.cargarDatosAprendiz: Aprendiz encontrado");
                System.out.println("   - Programa ID: " + aprendiz.getProgramaId());
                System.out.println("   - Ficha ID: " + aprendiz.getFichaId());
                
                // Cargar el programa
                if (aprendiz.getProgramaId() > 0) {
                    programa = programaDAO.buscarPorId(aprendiz.getProgramaId());
                    if (programa != null) {
                        System.out.println("   - Programa: " + programa.getNombrePrograma());
                    } else {
                        System.err.println("⚠️ PerfilBean.cargarDatosAprendiz: No se encontró el programa con ID: " + aprendiz.getProgramaId());
                    }
                }
                
                // Cargar la ficha
                if (aprendiz.getFichaId() > 0) {
                    ficha = fichaDAO.buscarPorId(aprendiz.getFichaId());
                    if (ficha != null) {
                        System.out.println("   - Ficha: " + ficha.getNumFicha());
                    } else {
                        System.err.println("⚠️ PerfilBean.cargarDatosAprendiz: No se encontró la ficha con ID: " + aprendiz.getFichaId());
                    }
                }
            } else {
                System.err.println("⚠️ PerfilBean.cargarDatosAprendiz: No se encontró registro de aprendiz para el usuario ID: " + usuario.getIdUsuario());
            }
        } catch (Exception e) {
            System.err.println("❌ PerfilBean.cargarDatosAprendiz: Error al cargar datos del aprendiz: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            init();
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }

    public String getConfirmarContrasena() {
        return confirmarContrasena;
    }

    public void setConfirmarContrasena(String confirmarContrasena) {
        this.confirmarContrasena = confirmarContrasena;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    public void activarEdicion() {
        modoEdicion = true;
<<<<<<< HEAD
        System.out.println("PerfilBean.activarEdicion: Modo edición activado");
=======
        System.out.println("🔍 PerfilBean.activarEdicion: Modo edición activado");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
    }

    public void cancelarEdicion() {
        modoEdicion = false;
<<<<<<< HEAD
        
        if (usuario != null) {
            usuario = usuarioDAO.buscarPorId(usuario.getIdUsuario());
        
=======
        // Recargar los datos originales
        if (usuario != null) {
            usuario = usuarioDAO.buscarPorId(usuario.getIdUsuario());
            // Si es aprendiz, recargar también los datos del aprendiz
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            if (esAprendiz) {
                cargarDatosAprendiz();
            }
        }
        nuevaContrasena = "";
        confirmarContrasena = "";
<<<<<<< HEAD
        System.out.println("PerfilBean.cancelarEdicion: Edición cancelada, datos recargados");
    }

    public String guardarPerfil() {
        System.out.println("PerfilBean.guardarPerfil: Guardando cambios del perfil");
=======
        System.out.println("🔍 PerfilBean.cancelarEdicion: Edición cancelada, datos recargados");
    }

    public String guardarPerfil() {
        System.out.println("🔍 PerfilBean.guardarPerfil: Guardando cambios del perfil");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        
        if (usuario == null) {
            FacesUtils.addErrorMessage("No se pudo cargar la información del usuario");
            return null;
        }

<<<<<<< HEAD

=======
        // Validar que el documento no esté duplicado (excluyendo el usuario actual)
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (usuarioDAO.existeDocumento(usuario.getNumDocumento(), usuario.getIdUsuario())) {
            FacesUtils.addErrorMessage("El número de documento ya está registrado por otro usuario");
            return null;
        }

<<<<<<< HEAD
  
=======
        // Validar contraseña si se proporcionó una nueva
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
            if (!nuevaContrasena.equals(confirmarContrasena)) {
                FacesUtils.addErrorMessage("Las contraseñas no coinciden");
                return null;
            }
            if (nuevaContrasena.length() < 4) {
                FacesUtils.addErrorMessage("La contraseña debe tener al menos 4 caracteres");
                return null;
            }
            usuario.setContrasena(PasswordUtil.hash(nuevaContrasena));
        } else {
<<<<<<< HEAD

=======
            // Mantener la contraseña actual si no se proporcionó una nueva
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            Usuario usuarioActual = usuarioDAO.buscarPorId(usuario.getIdUsuario());
            if (usuarioActual != null) {
                usuario.setContrasena(usuarioActual.getContrasena());
            }
        }

<<<<<<< HEAD

        boolean actualizado = usuarioDAO.actualizar(usuario);
        
        if (actualizado) {
  
=======
        // Actualizar el usuario
        boolean actualizado = usuarioDAO.actualizar(usuario);
        
        if (actualizado) {
            // Actualizar el usuario en LoginBean
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            try {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                javax.el.ELContext elContext = facesContext.getELContext();
                javax.el.ExpressionFactory factory = facesContext.getApplication().getExpressionFactory();
                javax.el.ValueExpression ve = factory.createValueExpression(elContext, "#{loginBean}", LoginBean.class);
                LoginBean loginBean = (LoginBean) ve.getValue(elContext);
                
                if (loginBean != null && loginBean.getUsuarioAutenticado() != null 
                    && loginBean.getUsuarioAutenticado().getIdUsuario() == usuario.getIdUsuario()) {
<<<<<<< HEAD

                    Usuario usuarioActualizado = usuarioDAO.buscarPorId(usuario.getIdUsuario());
                    loginBean.setUsuarioAutenticado(usuarioActualizado);
                    System.out.println("PerfilBean.guardarPerfil: Usuario actualizado en LoginBean");
                }
            } catch (Exception e) {
                System.err.println(" PerfilBean.guardarPerfil: No se pudo actualizar LoginBean, pero el usuario se actualizó correctamente: " + e.getMessage());
                e.printStackTrace();
            }
            
            
            usuario = usuarioDAO.buscarPorId(usuario.getIdUsuario());
            
=======
                    // Actualizar el objeto en LoginBean
                    Usuario usuarioActualizado = usuarioDAO.buscarPorId(usuario.getIdUsuario());
                    loginBean.setUsuarioAutenticado(usuarioActualizado);
                    System.out.println("✅ PerfilBean.guardarPerfil: Usuario actualizado en LoginBean");
                }
            } catch (Exception e) {
                System.err.println("⚠️ PerfilBean.guardarPerfil: No se pudo actualizar LoginBean, pero el usuario se actualizó correctamente: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Recargar el usuario para mostrar los datos actualizados
            usuario = usuarioDAO.buscarPorId(usuario.getIdUsuario());
            // Si es aprendiz, recargar también los datos del aprendiz
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            if (esAprendiz) {
                cargarDatosAprendiz();
            }
            modoEdicion = false;
            nuevaContrasena = "";
            confirmarContrasena = "";
            FacesUtils.addInfoMessage("Perfil actualizado correctamente");
<<<<<<< HEAD
            System.out.println("PerfilBean.guardarPerfil: Perfil actualizado exitosamente");
            return null; 
        } else {
            FacesUtils.addErrorMessage("No se pudo actualizar el perfil. Intente nuevamente.");
            System.err.println("PerfilBean.guardarPerfil: Error al actualizar el perfil");
=======
            System.out.println("✅ PerfilBean.guardarPerfil: Perfil actualizado exitosamente");
            return null; // Permanecer en la misma página
        } else {
            FacesUtils.addErrorMessage("No se pudo actualizar el perfil. Intente nuevamente.");
            System.err.println("❌ PerfilBean.guardarPerfil: Error al actualizar el perfil");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            return null;
        }
    }

    public String getNombreCompleto() {
        if (usuario == null) {
            return "";
        }
        return usuario.getNombre();
    }

    public boolean isEsAprendiz() {
        return esAprendiz;
    }

    public void setEsAprendiz(boolean esAprendiz) {
        this.esAprendiz = esAprendiz;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public String getNombrePrograma() {
        if (programa != null) {
            return programa.getNombrePrograma();
        }
        return "No asignado";
    }

    public String getNumeroFicha() {
        if (ficha != null) {
            return String.valueOf(ficha.getNumFicha());
        }
        return "No asignada";
    }

<<<<<<< HEAD
   
=======
    /**
     * Verifica si el usuario tiene un rol específico
     */
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
    public boolean esRol(int rol) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext == null) {
                return false;
            }
            
            List<Integer> roles = (List<Integer>) facesContext.getExternalContext().getSessionMap().get("roles");
            if (roles == null || roles.isEmpty()) {
                return false;
            }
            
            return roles.contains(rol);
        } catch (Exception e) {
<<<<<<< HEAD
            System.err.println("PerfilBean.esRol: Error al verificar rol: " + e.getMessage());
=======
            System.err.println("❌ PerfilBean.esRol: Error al verificar rol: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            return false;
        }
    }
    
<<<<<<< HEAD
    
    public String volverAlPanel() {
        System.out.println("PerfilBean.volverAlPanel: Redirigiendo al panel según rol");
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            System.err.println("PerfilBean.volverAlPanel: FacesContext es null");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true"; 
        }
        
        try {
            
            List<Integer> roles = (List<Integer>) facesContext.getExternalContext().getSessionMap().get("roles");
            
            if (roles == null || roles.isEmpty()) {
                System.err.println("PerfilBean.volverAlPanel: No se encontraron roles, redirigiendo a Admin por defecto");
                return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
            }
            
            System.out.println("PerfilBean.volverAlPanel: Roles del usuario: " + roles);
            
            
            if (roles.contains(1)) { 
                System.out.println("   → Redirigiendo a Admin");
                return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
            } else if (roles.contains(2)) { 
                System.out.println("   → Redirigiendo a Instructor");
                return "/pages/instructor/index.xhtml?faces-redirect=true";
            } else if (roles.contains(3)) { 
                System.out.println("   → Redirigiendo a Aprendiz");
                return "/pages/aprendiz/index.xhtml?faces-redirect=true";
            } else if (roles.contains(4)) { 
                System.out.println("  Redirigiendo a Guarda");
                return "/pages/guarda/index.xhtml?faces-redirect=true";
            } else {
                System.out.println("   No se encontró rol válido, redirigiendo a Admin por defecto");
                return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
            }
        } catch (Exception e) {
            System.err.println("PerfilBean.volverAlPanel: Error al determinar el panel: " + e.getMessage());
=======
    /**
     * Redirige al panel correspondiente según el rol del usuario
     */
    public String volverAlPanel() {
        System.out.println("🔍 PerfilBean.volverAlPanel: Redirigiendo al panel según rol");
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            System.err.println("❌ PerfilBean.volverAlPanel: FacesContext es null");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true"; // Por defecto
        }
        
        try {
            // Obtener los roles del usuario desde la sesión
            List<Integer> roles = (List<Integer>) facesContext.getExternalContext().getSessionMap().get("roles");
            
            if (roles == null || roles.isEmpty()) {
                System.err.println("⚠️ PerfilBean.volverAlPanel: No se encontraron roles, redirigiendo a Admin por defecto");
                return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
            }
            
            System.out.println("🔍 PerfilBean.volverAlPanel: Roles del usuario: " + roles);
            
            // Determinar el panel según el rol (misma lógica que LoginBean)
            if (roles.contains(1)) { // Administrador/Coordinador
                System.out.println("   → Redirigiendo a Admin");
                return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
            } else if (roles.contains(2)) { // Instructor
                System.out.println("   → Redirigiendo a Instructor");
                return "/pages/instructor/index.xhtml?faces-redirect=true";
            } else if (roles.contains(3)) { // Aprendiz
                System.out.println("   → Redirigiendo a Aprendiz");
                return "/pages/aprendiz/index.xhtml?faces-redirect=true";
            } else if (roles.contains(4)) { // Guarda de Seguridad
                System.out.println("   → Redirigiendo a Guarda");
                return "/pages/guarda/index.xhtml?faces-redirect=true";
            } else {
                System.out.println("   ⚠️ No se encontró rol válido, redirigiendo a Admin por defecto");
                return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
            }
        } catch (Exception e) {
            System.err.println("❌ PerfilBean.volverAlPanel: Error al determinar el panel: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true"; // Por defecto
        }
    }
}

