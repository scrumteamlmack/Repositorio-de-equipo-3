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

    // Métodos para verificar rol específico (útiles en vistas)
    public boolean isEsAdmin() {
        return hasRol(1);
    }

    public boolean isEsInstructor() {
        return hasRol(2);
    }

    public boolean isEsAprendiz() {
        return hasRol(3);
    }

    public boolean isEsGuarda() {
        return hasRol(4);
    }

    public void verificarSesion(int... rolesPermitidos) {
        if (!isAutenticado()) {
            FacesUtils.redirect("/faces/login.xhtml");
            return;
        }
        if (rolesPermitidos != null && rolesPermitidos.length > 0) {
            for (int rol : rolesPermitidos) {
                if (hasRol(rol)) {
                    return;
                }
            }
            FacesUtils.redirect("/faces/sinacceso.xhtml");
        }
    }

    // Métodos específicos para verificación de sesión por rol
    public void verificarSesionInstructor() {
        verificarSesion(2); // Rol 2 = Instructor
    }

    public void verificarSesionAdmin() {
        verificarSesion(1); // Rol 1 = Administrador
    }

    public void verificarSesionAprendiz() {
        verificarSesion(3); // Rol 3 = Aprendiz
    }

    public void verificarSesionGuarda() {
        verificarSesion(4); // Rol 4 = Guarda de Seguridad
    }

    public void verificarSesionAdminInstructor() {
        verificarSesion(1, 2); // Rol 1 = Admin, Rol 2 = Instructor
    }

    public void verificarSesionAdminGuarda() {
        verificarSesion(1, 4); // Rol 1 = Admin, Rol 4 = Guarda
    }

    public String cerrarSesion() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().clear();
        usuarioAutenticado = null;
        roles.clear();
        credenciales = new Usuario();
        return "/login.xhtml?faces-redirect=true";
    }
}
