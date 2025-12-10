package beans;

import dao.*;
import modelo.*;
import util.FacesUtils;
import util.PasswordUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RolDAO rolDAO = new RolDAO();
    private final AprendizDAO aprendizDAO = new AprendizDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final CoordinadorDAO coordinadorDAO = new CoordinadorDAO();
    private final GuardaSeguridadDAO guardaDAO = new GuardaSeguridadDAO();

    // MODELOS
    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios;
    private List<Rol> roles;
    private int rolSeleccionado = 3; // aprendiz por defecto

    // Objetos específicos según el rol
    private Aprendiz aprendiz = new Aprendiz();
    private Instructor instructor = new Instructor();
    private Coordinador coordinador = new Coordinador();
    private GuardaSeguridad guardaSeguridad = new GuardaSeguridad();

    // CONTROL DE VISTAS PRINCIPALES
    private boolean mostrarBienvenida = true;
    private boolean mostrarFormularioUsuario = false;
    private boolean mostrarTablaUsuarios = false;

    private String textoBusqueda;
    private Integer idUsuarioEditar;

    // ----------------------------------------------------------
    // INIT
    // ----------------------------------------------------------

    @PostConstruct
    public void init() {
        roles = rolDAO.listar();
        System.out.println("✅ UsuarioBean.init: Roles cargados: " + (roles != null ? roles.size() : 0));
        if (roles != null && !roles.isEmpty()) {
            for (Rol r : roles) {
                System.out.println("   - Rol ID: " + r.getIdRol() + ", Nombre: " + r.getNombre());
            }
        } else {
            System.err.println("⚠️ UsuarioBean.init: No se encontraron roles en la base de datos");
        }
        usuarios = usuarioDAO.listar();
    }
    
    public void initForm() {
        System.out.println("🔍 UsuarioBean.initForm: Inicializando formulario");
        System.out.println("   - idUsuarioEditar: " + idUsuarioEditar);
        
        // Si no hay idUsuarioEditar, intentar obtenerlo de la URL
        if (idUsuarioEditar == null || idUsuarioEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuarioEditar = Integer.parseInt(idParam);
                        System.out.println("   - idUsuarioEditar obtenido de URL: " + idUsuarioEditar);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ UsuarioBean.initForm: Error al obtener idUsuarioEditar de URL: " + e.getMessage());
            }
        }
        
        // Si hay idUsuarioEditar, cargar los datos del usuario
        if (idUsuarioEditar != null && idUsuarioEditar > 0) {
            Usuario encontrado = usuarioDAO.buscarPorId(idUsuarioEditar);
            if (encontrado != null) {
                System.out.println("   - Usuario encontrado: " + encontrado.getPNombre() + " " + encontrado.getPApellido());
                usuario = encontrado;
                // No establecer la contraseña para que el campo quede vacío
                usuario.setPass("");
                
                // Cargar el rol actual
                List<Integer> listaRoles = usuarioDAO.obtenerRolesIdsPorUsuarioId(idUsuarioEditar);
                rolSeleccionado = listaRoles.isEmpty() ? 0 : listaRoles.get(0);
                System.out.println("   - Rol actual: " + rolSeleccionado);
            } else {
                System.err.println("⚠️ UsuarioBean.initForm: Usuario no encontrado con ID: " + idUsuarioEditar);
                prepararNuevo();
            }
        } else {
            
            // Modo creación
            prepararNuevo();
        }
    }
    
    public boolean isModoEdicion() {
        return idUsuarioEditar != null && idUsuarioEditar > 0 && usuario != null && usuario.getIdUsuario() > 0;
    }

    public void refrescar() {
        usuarios = usuarioDAO.listar();
    }

    // ----------------------------------------------------------
    // CRUD USUARIOS
    // ----------------------------------------------------------

    public void prepararNuevo() {
        usuario = new Usuario();
        aprendiz = new Aprendiz();
        instructor = new Instructor();
        coordinador = new Coordinador();
        guardaSeguridad = new GuardaSeguridad();

        rolSeleccionado = 3; // aprendiz por defecto
    }

    public String guardarUsuario() {

        // Validaciones
        if (usuarioDAO.existeDocumento(usuario.getNumDocumento(),
                usuario.getIdUsuario() == 0 ? null : usuario.getIdUsuario())) {

            FacesUtils.addWarnMessage("El documento ya se encuentra registrado.");
            return null;
        }

        // NUEVO USUARIO
        if (usuario.getIdUsuario() == 0) {
            // Validar contraseña solo para nuevos usuarios
            if (usuario.getPass() == null || usuario.getPass().isEmpty()) {
                FacesUtils.addWarnMessage("Debe definir una contraseña.");
                return null;
            }
            usuario.setContrasena(PasswordUtil.hash(usuario.getPass()));

            int id = usuarioDAO.guardar(usuario);

            if (id <= 0) {
                FacesUtils.addErrorMessage("No fue posible guardar el usuario.");
                return null;
            }

            usuario.setIdUsuario(id);
            usuarioDAO.asignarRol(id, rolSeleccionado);
            FacesUtils.addInfoMessage("Usuario registrado correctamente. Complete la información del rol.");

            // 🔥 REDIRECCIÓN SEGÚN ROL
            return redireccionarAlFormularioRol(id, rolSeleccionado);

        } else {
            // ACTUALIZAR USUARIO
            // Solo actualizar contraseña si se proporcionó una nueva
            if (usuario.getPass() != null && !usuario.getPass().isEmpty()) {
                usuario.setContrasena(PasswordUtil.hash(usuario.getPass()));
            } else {
                // Mantener la contraseña actual si no se proporcionó una nueva
                Usuario usuarioActual = usuarioDAO.buscarPorId(usuario.getIdUsuario());
                if (usuarioActual != null) {
                    usuario.setContrasena(usuarioActual.getContrasena());
                }
            }
            
            usuarioDAO.actualizar(usuario);
            // NO cambiar el rol en modo edición (el rol se mantiene igual)
            // usuarioDAO.quitarRoles(usuario.getIdUsuario());
            // usuarioDAO.asignarRol(usuario.getIdUsuario(), rolSeleccionado);
            FacesUtils.addInfoMessage("Usuario actualizado correctamente.");
            return "/pages/admin/listarUsuarios.xhtml?faces-redirect=true";
        }
    }
    
    private String redireccionarAlFormularioRol(int idUsuario, int rol) {

        switch (rol) {

            case 1: // Coordinador
                return "/pages/admin/formCoordinador.xhtml?id=" + idUsuario + "&faces-redirect=true";

            case 2: // Instructor
                return "/pages/admin/formInstructor.xhtml?id=" + idUsuario + "&faces-redirect=true";

            case 3: // Aprendiz
                return "/pages/aprendiz/formAprendiz.xhtml?id=" + idUsuario + "&faces-redirect=true";

            case 4: // Guarda seguridad
                return "/pages/admin/formGuarda.xhtml?id=" + idUsuario + "&faces-redirect=true";

            default:
                return "/pages/admin/indexAdmin.xhtml?faces-redirect=true";
        }
    }


    public String editar(int idUsuario) {
        System.out.println("🔍 UsuarioBean.editar: Editando usuario ID: " + idUsuario);
        return "/pages/admin/formUsuario.xhtml?id=" + idUsuario + "&faces-redirect=true";
    }

    public void eliminar(int idUsuario) {

        // 1. Eliminar perfiles
        aprendizDAO.eliminarPorUsuario(idUsuario);
        instructorDAO.eliminarPorUsuario(idUsuario);
        coordinadorDAO.eliminarPorUsuario(idUsuario);
        guardaDAO.eliminarPorUsuario(idUsuario);

        // 2. Eliminar roles
        usuarioDAO.quitarRoles(idUsuario);

        // 3. Eliminar usuario
        usuarioDAO.eliminar(idUsuario);

        FacesUtils.addInfoMessage("Usuario eliminado correctamente.");
        refrescar();
    }

    public void filtrar() {
        usuarios = usuarioDAO.filtrar(textoBusqueda, null);
    }

    // ----------------------------------------------------------
    // VISTAS DEL DASHBOARD
    // ----------------------------------------------------------

    public void mostrarFormulario() {
        prepararNuevo();
        mostrarBienvenida = false;
        mostrarTablaUsuarios = false;
        mostrarFormularioUsuario = true;
    }

    public void mostrarTabla() {
        refrescar();
        mostrarBienvenida = false;
        mostrarFormularioUsuario = false;
        mostrarTablaUsuarios = true;
    }

    public void mostrarDashboard() {
        mostrarBienvenida = true;
        mostrarFormularioUsuario = false;
        mostrarTablaUsuarios = false;
    }

    // ----------------------------------------------------------
    // GETTERS & SETTERS
    // ----------------------------------------------------------

    public Usuario getUsuario() { return usuario; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Rol> getRoles() { return roles; }

    public int getRolSeleccionado() { return rolSeleccionado; }
    public void setRolSeleccionado(int rolSeleccionado) { this.rolSeleccionado = rolSeleccionado; }

    public boolean isMostrarBienvenida() { return mostrarBienvenida; }
    public boolean isMostrarFormularioUsuario() { return mostrarFormularioUsuario; }
    public boolean isMostrarTablaUsuarios() { return mostrarTablaUsuarios; }

    public String getTextoBusqueda() { return textoBusqueda; }
    public void setTextoBusqueda(String textoBusqueda) { this.textoBusqueda = textoBusqueda; }
    
    public Integer getIdUsuarioEditar() { return idUsuarioEditar; }
    public void setIdUsuarioEditar(Integer idUsuarioEditar) { this.idUsuarioEditar = idUsuarioEditar; }

    // Obtener nombre del rol para la tabla
    public String obtenerNombreRol(int idUsuario) {
        List<Integer> r = usuarioDAO.obtenerRolesIdsPorUsuarioId(idUsuario);
        if (r == null || r.isEmpty()) return "Sin rol";

        int idRol = r.get(0);

        for (Rol rol : roles) {
            if (rol.getIdRol() == idRol)
                return rol.getNombre();
        }

        return "Sin rol";
    }
}
