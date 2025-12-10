package beans;

import dao.UsuarioDAO;
import modelo.Usuario;
import util.FacesUtils;
import util.PasswordUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario credenciales = new Usuario();
    private Usuario usuarioAutenticado;
    private List<Integer> roles = new ArrayList<>();

    public Usuario getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(Usuario credenciales) {
        this.credenciales = credenciales;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void setUsuarioAutenticado(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    public boolean isAutenticado() {
        return usuarioAutenticado != null;
    }

    public String autenticar() {
        if (credenciales == null) {
            FacesUtils.addErrorMessage("Error: credenciales nulas");
            return null;
        }
        
        int documento = credenciales.getDoc();
        if (documento == 0) {
            FacesUtils.addErrorMessage("Debe ingresar un documento válido");
            return null;
        }

        System.out.println("🔍 LoginBean: Buscando usuario con documento: " + documento);

        Usuario usuarioBD = null;
        try {
            usuarioBD = usuarioDAO.buscarPorDocumento(documento);
            System.out.println("🔍 LoginBean: Resultado de búsqueda: " + (usuarioBD != null ? "Usuario encontrado (ID: " + usuarioBD.getIdUsuario() + ")" : "Usuario no encontrado"));
        } catch (Exception e) {
            System.err.println("❌ LoginBean: Error al buscar usuario: " + e.getMessage());
            e.printStackTrace();
            FacesUtils.addErrorMessage("Error al conectarse a la base de datos: " + e.getMessage());
            return null;
        }

        if (usuarioBD == null) {
            System.out.println("❌ LoginBean: Usuario no encontrado con documento: " + documento);
            FacesUtils.addErrorMessage("Usuario no encontrado. Verifique su número de documento.");
            return null;
        }

        String passwordIngresada = credenciales.getPass();
        if (passwordIngresada == null || passwordIngresada.isEmpty()) {
            FacesUtils.addErrorMessage("Debe ingresar una contraseña");
            return null;
        }

        if (!PasswordUtil.matches(passwordIngresada, usuarioBD.getContrasena())) {
            System.out.println("❌ LoginBean: Contraseña incorrecta para usuario: " + documento);
            FacesUtils.addErrorMessage("Contraseña incorrecta");
            return null;
        }

        // Login exitoso
        System.out.println("✅ LoginBean: Autenticación exitosa para usuario: " + usuarioBD.getPNombre() + " (ID: " + usuarioBD.getIdUsuario() + ")");
        usuarioAutenticado = usuarioBD;
        roles = usuarioDAO.obtenerRolesIdsPorUsuarioId(usuarioBD.getIdUsuario());
        System.out.println("✅ LoginBean: Roles asignados: " + roles);
        guardarEnSesion();
        FacesUtils.addInfoMessage("Bienvenido " + usuarioBD.getPNombre());

        return redireccionSegunRol();
    }


    private void guardarEnSesion() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("user", usuarioAutenticado.getNombre());
        externalContext.getSessionMap().put("userId", usuarioAutenticado.getIdUsuario());
        externalContext.getSessionMap().put("roles", roles);
    }

    private String redireccionSegunRol() {
        System.out.println("🔍 LoginBean.redireccionSegunRol: Roles del usuario: " + roles);
        
        if (hasRol(1)) { // Administrador/Coordinador
            System.out.println("   → Redirigiendo a Admin");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        } else if (hasRol(2)) { // Instructor
            System.out.println("   → Redirigiendo a Instructor");
            return "/pages/instructor/index.xhtml?faces-redirect=true";
        } else if (hasRol(3)) { // Aprendiz
            System.out.println("   → Redirigiendo a Aprendiz");
            return "/pages/aprendiz/index.xhtml?faces-redirect=true";
        } else if (hasRol(4)) { // Guarda de Seguridad
            System.out.println("   → Redirigiendo a Guarda");
            return "/pages/guarda/index.xhtml?faces-redirect=true";
        } else {
            System.out.println("   ⚠️ No se encontró rol válido, redirigiendo a Admin por defecto");
            return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        }
    }

    public boolean hasRol(int roleId) {
        return roles != null && roles.contains(roleId);
    }

    public void verificarSesion(int... rolesPermitidos) {
        // Verificar si hay sesión activa
        if (!isAutenticado()) {
            System.out.println("⚠️ LoginBean.verificarSesion: Usuario no autenticado, redirigiendo al aviso de logout");
            FacesUtils.redirect("/faces/aviso-logout.xhtml");
            return;
        }
        
        // Verificar roles si se especificaron
        if (rolesPermitidos != null && rolesPermitidos.length > 0) {
            boolean tieneRol = false;
            for (int rol : rolesPermitidos) {
                if (hasRol(rol)) {
                    tieneRol = true;
                    break;
                }
            }
            if (!tieneRol) {
                System.out.println("⚠️ LoginBean.verificarSesion: Usuario no tiene rol permitido, redirigiendo");
                FacesUtils.redirect("/faces/sinacceso.xhtml");
            }
        }
    }
    
    // Método wrapper para verificar sesión sin restricciones de rol (para perfil)
    public String verificarSesionCualquiera() {
        if (!isAutenticado()) {
            System.out.println("⚠️ LoginBean.verificarSesionCualquiera: Usuario no autenticado, redirigiendo al aviso de logout");
            FacesUtils.redirect("/faces/aviso-logout.xhtml");
            return null;
        }
        return null;
    }
    
    // Métodos wrapper para f:viewAction que retornan String
    public String verificarSesionAdmin() {
        verificarSesion(1);
        return null;
    }
    
    public String verificarSesionInstructor() {
        verificarSesion(2);
        return null;
    }
    
    public String verificarSesionAprendiz() {
        verificarSesion(3);
        return null;
    }
    
    public String verificarSesionGuarda() {
        verificarSesion(4);
        return null;
    }
    
    public String verificarSesionAdminInstructor() {
        verificarSesion(1, 2);
        return null;
    }
    
    public String verificarSesionAdminGuarda() {
        verificarSesion(1, 4);
        return null;
    }

    public String cerrarSesion() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            
            // Limpiar todas las variables de sesión
            externalContext.getSessionMap().clear();
            usuarioAutenticado = null;
            roles.clear();
            credenciales = new Usuario();
            
            // Invalidar la sesión HTTP completamente
            javax.servlet.http.HttpSession session = (javax.servlet.http.HttpSession) externalContext.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            
            System.out.println("✅ LoginBean.cerrarSesion: Sesión cerrada e invalidada correctamente");
            
            // Redirigir al formulario de inicio de sesión
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.err.println("❌ LoginBean.cerrarSesion: Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
            // Aún así redirigir al login
            return "/login.xhtml?faces-redirect=true";
        }
    }
}
