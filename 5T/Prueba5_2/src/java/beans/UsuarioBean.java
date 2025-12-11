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
    private final FichaDAO fichaDAO = new FichaDAO();
    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private final MinutaDAO minutaDAO = new MinutaDAO();
    private final IncidenteDAO incidenteDAO = new IncidenteDAO();

    // MODELOS
    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios;
    private List<Usuario> usuariosFiltrados;
    private List<Rol> roles;
    private int rolSeleccionado = 3; // aprendiz por defecto
    
    // FILTROS MULTICRITERIO
    private Integer filtroRolId;
    private String filtroTipoDocumento;

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
        System.out.println("✅ UsuarioBean.init (@PostConstruct): Roles cargados: " + (roles != null ? roles.size() : 0));
        if (roles != null && !roles.isEmpty()) {
            for (Rol r : roles) {
                System.out.println("   - Rol ID: " + r.getIdRol() + ", Nombre: " + r.getNombre());
            }
        } else {
            System.err.println("⚠️ UsuarioBean.init: No se encontraron roles en la base de datos");
        }
        usuarios = usuarioDAO.listar();
        usuariosFiltrados = null;
        
        cargarUsuarioSiEsNecesario();
    }
    
    private void cargarUsuarioSiEsNecesario() {
        // Intentar cargar usuario si hay un ID en la URL (para formulario de edición)
        try {
            javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
            if (facesContext != null) {
                String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        System.out.println("   - ID encontrado en URL en @PostConstruct: " + id);
                        // Si encontramos un ID en la URL, es probable que sea modo edición
                        if (id > 0 && (usuario == null || usuario.getIdUsuario() == 0)) {
                            Usuario encontrado = usuarioDAO.buscarPorId(id);
                            if (encontrado != null) {
                                idUsuarioEditar = id;
                                usuario = encontrado;
                                usuario.setPass("");
                                System.out.println("   ✅ Usuario cargado en @PostConstruct: " + usuario.getPNombre());
                            }
                        }
                    } catch (NumberFormatException e) {
                        // No es un número, no es un ID de edición
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ UsuarioBean.cargarUsuarioSiEsNecesario: Error: " + e.getMessage());
        }
    }
    
    private void cargarUsuarioSiEsEdicion() {
        System.out.println("🔍 UsuarioBean.cargarUsuarioSiEsEdicion: Verificando si hay usuario a editar");
        
        // Intentar obtener el ID de la URL
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
                System.err.println("⚠️ UsuarioBean.cargarUsuarioSiEsEdicion: Error al obtener idUsuarioEditar de URL: " + e.getMessage());
            }
        }
        
        // Si hay idUsuarioEditar, cargar los datos del usuario
        if (idUsuarioEditar != null && idUsuarioEditar > 0) {
            System.out.println("   - Modo EDICIÓN detectado. Cargando usuario ID: " + idUsuarioEditar);
            Usuario encontrado = usuarioDAO.buscarPorId(idUsuarioEditar);
            if (encontrado != null) {
                System.out.println("   ✅ Usuario encontrado: " + encontrado.getPNombre() + " " + encontrado.getPApellido());
                System.out.println("   - Datos cargados:");
                System.out.println("      * Correo: " + encontrado.getCorreo());
                System.out.println("      * TipoDoc: " + encontrado.getTipoDocumento());
                System.out.println("      * NumDoc: " + encontrado.getNumDocumento());
                System.out.println("      * PNombre: " + encontrado.getPNombre());
                System.out.println("      * PApellido: " + encontrado.getPApellido());
                
                usuario = encontrado;
                // Establecer el ID explícitamente
                usuario.setIdUsuario(idUsuarioEditar);
                // No establecer la contraseña para que el campo quede vacío
                usuario.setPass("");
                
                // Cargar el rol actual
                List<Integer> listaRoles = usuarioDAO.obtenerRolesIdsPorUsuarioId(idUsuarioEditar);
                rolSeleccionado = listaRoles.isEmpty() ? 0 : listaRoles.get(0);
                System.out.println("   - Rol actual cargado: " + rolSeleccionado);
            } else {
                System.err.println("⚠️ UsuarioBean.cargarUsuarioSiEsEdicion: Usuario no encontrado con ID: " + idUsuarioEditar);
                prepararNuevo();
            }
        } else {
            System.out.println("   - Modo CREACIÓN (no hay ID de usuario)");
            prepararNuevo();
        }
    }
    
    public void initForm() {
        System.out.println("🔍 UsuarioBean.initForm: Método llamado desde evento preRenderView");
        System.out.println("   - idUsuarioEditar desde viewParam: " + idUsuarioEditar);
        
        // Cargar datos del usuario si estamos en modo edición
        // Este método se ejecuta DESPUÉS de que el viewParam procese el parámetro
        cargarUsuarioSiEsEdicion();
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
        return "/pages/admin/editarUsuario.xhtml?id=" + idUsuario + "&faces-redirect=true";
    }
    
    public String cargarUsuarioParaEditar() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔍 UsuarioBean.cargarUsuarioParaEditar: INICIO");
        System.out.println("   - idUsuarioEditar desde viewParam: " + idUsuarioEditar);
        System.out.println("   - usuario actual: " + (usuario != null ? "ID=" + usuario.getIdUsuario() : "null"));
        
        // Verificar si ya tenemos los datos cargados (evitar cargar múltiples veces)
        if (usuario != null && usuario.getIdUsuario() > 0 && idUsuarioEditar != null && usuario.getIdUsuario() == idUsuarioEditar) {
            System.out.println("   ✅ Usuario ya está cargado, no es necesario recargar");
            System.out.println("═══════════════════════════════════════════════════════");
            return null;
        }
        
        // Intentar obtener el ID de la URL si no está establecido
        if (idUsuarioEditar == null || idUsuarioEditar == 0) {
            try {
                javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");
                    System.out.println("   - Parámetro 'id' obtenido de URL: " + idParam);
                    if (idParam != null && !idParam.isEmpty()) {
                        idUsuarioEditar = Integer.parseInt(idParam);
                        System.out.println("   ✅ idUsuarioEditar parseado desde URL: " + idUsuarioEditar);
                    } else {
                        System.err.println("   ❌ No se encontró parámetro 'id' en la URL");
                    }
                } else {
                    System.err.println("   ❌ FacesContext es null");
                }
            } catch (Exception e) {
                System.err.println("⚠️ UsuarioBean.cargarUsuarioParaEditar: Error al obtener idUsuarioEditar de URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("   - idUsuarioEditar FINAL: " + idUsuarioEditar);
        
        // Si hay idUsuarioEditar, cargar los datos del usuario
        if (idUsuarioEditar != null && idUsuarioEditar > 0) {
            System.out.println("   - Llamando a usuarioDAO.buscarPorId(" + idUsuarioEditar + ")");
            Usuario encontrado = usuarioDAO.buscarPorId(idUsuarioEditar);
            
            if (encontrado != null) {
                System.out.println("   ✅ Usuario encontrado en BD:");
                System.out.println("      * ID: " + encontrado.getIdUsuario());
                System.out.println("      * PNombre: " + encontrado.getPNombre());
                System.out.println("      * SNombre: " + encontrado.getSNombre());
                System.out.println("      * PApellido: " + encontrado.getPApellido());
                System.out.println("      * SApellido: " + encontrado.getSApellido());
                System.out.println("      * Correo: " + encontrado.getCorreo());
                System.out.println("      * TipoDoc: " + encontrado.getTipoDocumento());
                System.out.println("      * NumDoc: " + encontrado.getNumDocumento());
                
                // CRÍTICO: Asignar los datos al objeto usuario del bean
                // Crear una copia completa para asegurar que todos los campos estén asignados
                if (usuario == null) {
                    usuario = new Usuario();
                }
                usuario.setIdUsuario(encontrado.getIdUsuario());
                usuario.setPNombre(encontrado.getPNombre());
                usuario.setSNombre(encontrado.getSNombre());
                usuario.setPApellido(encontrado.getPApellido());
                usuario.setSApellido(encontrado.getSApellido());
                usuario.setTipoDocumento(encontrado.getTipoDocumento());
                usuario.setNumDocumento(encontrado.getNumDocumento());
                usuario.setCorreo(encontrado.getCorreo());
                usuario.setContrasena(encontrado.getContrasena()); // Mantener la contraseña encriptada
                usuario.setPass(""); // Limpiar el campo de texto de contraseña
                
                System.out.println("   ✅ Datos asignados al bean usuario:");
                System.out.println("      * usuario.getIdUsuario(): " + usuario.getIdUsuario());
                System.out.println("      * usuario.getPNombre(): " + usuario.getPNombre());
                System.out.println("      * usuario.getSNombre(): " + usuario.getSNombre());
                System.out.println("      * usuario.getPApellido(): " + usuario.getPApellido());
                System.out.println("      * usuario.getSApellido(): " + usuario.getSApellido());
                System.out.println("      * usuario.getCorreo(): " + usuario.getCorreo());
                System.out.println("      * usuario.getTipoDocumento(): " + usuario.getTipoDocumento());
                System.out.println("      * usuario.getNumDocumento(): " + usuario.getNumDocumento());
                System.out.println("═══════════════════════════════════════════════════════");
            } else {
                System.err.println("   ❌ Usuario NO encontrado en BD con ID: " + idUsuarioEditar);
                FacesUtils.addErrorMessage("Usuario no encontrado con ID: " + idUsuarioEditar);
            }
        } else {
            System.err.println("   ❌ idUsuarioEditar es null o 0 - No se puede cargar usuario");
            System.err.println("   - Esto puede indicar que el viewParam no funcionó correctamente");
            FacesUtils.addErrorMessage("No se proporcionó ID de usuario válido para editar.");
        }
        
        return null; // No redirigir, mostrar la vista actual
    }
    
    public String actualizarUsuario() {
        System.out.println("🔍 UsuarioBean.actualizarUsuario: Actualizando usuario");
        System.out.println("   - ID Usuario: " + usuario.getIdUsuario());
        
        if (usuario.getIdUsuario() == 0) {
            FacesUtils.addErrorMessage("Error: No se puede actualizar un usuario sin ID.");
            return null;
        }
        
        // Validaciones básicas
        if (usuario.getPNombre() == null || usuario.getPNombre().trim().isEmpty()) {
            FacesUtils.addErrorMessage("El primer nombre es obligatorio.");
            return null;
        }
        
        if (usuario.getPApellido() == null || usuario.getPApellido().trim().isEmpty()) {
            FacesUtils.addErrorMessage("El primer apellido es obligatorio.");
            return null;
        }
        
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            FacesUtils.addErrorMessage("El correo es obligatorio.");
            return null;
        }
        
        // Solo actualizar contraseña si se proporcionó una nueva
        if (usuario.getPass() != null && !usuario.getPass().isEmpty()) {
            usuario.setContrasena(PasswordUtil.hash(usuario.getPass()));
            System.out.println("   - Nueva contraseña establecida");
        } else {
            // Mantener la contraseña actual si no se proporcionó una nueva
            Usuario usuarioActual = usuarioDAO.buscarPorId(usuario.getIdUsuario());
            if (usuarioActual != null) {
                usuario.setContrasena(usuarioActual.getContrasena());
                System.out.println("   - Manteniendo contraseña actual");
            }
        }
        
        boolean actualizado = usuarioDAO.actualizar(usuario);
        
        if (actualizado) {
            System.out.println("✅ UsuarioBean.actualizarUsuario: Usuario actualizado correctamente");
            FacesUtils.addInfoMessage("Usuario actualizado correctamente.");
            return "/pages/admin/listarUsuarios.xhtml?faces-redirect=true";
        } else {
            System.err.println("❌ UsuarioBean.actualizarUsuario: No se pudo actualizar el usuario");
            FacesUtils.addErrorMessage("No se pudo actualizar el usuario.");
            return null;
        }
    }

    public void eliminar(int idUsuario) {
        System.out.println("🔍 UsuarioBean.eliminar: Eliminando usuario ID: " + idUsuario);
        
        try {
            // 1. SIEMPRE intentar eliminar referencias relacionadas con instructor
            // (incluso si el usuario ya no tiene rol, puede tener registro en tabla instructor)
            // Esto es necesario porque las tablas ficha, registro_inasistencia y registro_minuta
            // tienen claves foráneas que referencian a instructor con ON DELETE NO ACTION
            Instructor instructor = instructorDAO.buscarPorUsuario(idUsuario);
            if (instructor != null) {
                System.out.println("   - Usuario tiene registro en tabla instructor, eliminando referencias relacionadas...");
                // Eliminar fichas asociadas al instructor
                System.out.println("   - Eliminando fichas...");
                fichaDAO.eliminarPorInstructor(idUsuario);
                // Eliminar registros de asistencia asociados al instructor
                System.out.println("   - Eliminando asistencias...");
                asistenciaDAO.eliminarPorInstructor(idUsuario);
                // Eliminar minutas asociadas al instructor (como responsable)
                System.out.println("   - Eliminando minutas...");
                minutaDAO.eliminarPorInstructor(idUsuario);
            } else {
                System.out.println("   - Usuario no tiene registro en tabla instructor");
            }
            
            // 2. Eliminar perfiles (intentar todos, algunos pueden no existir)
            System.out.println("   - Eliminando perfiles...");
            aprendizDAO.eliminarPorUsuario(idUsuario);
            instructorDAO.eliminarPorUsuario(idUsuario);
            coordinadorDAO.eliminarPorUsuario(idUsuario);
            guardaDAO.eliminarPorUsuario(idUsuario);

            // 3. Eliminar incidentes asociados directamente al usuario
            // (registro_incidente tiene usuario_id_usuario que referencia directamente a usuario)
            System.out.println("   - Eliminando incidentes del usuario...");
            incidenteDAO.eliminarPorUsuario(idUsuario);
            
            // 4. Eliminar roles
            System.out.println("   - Eliminando roles...");
            usuarioDAO.quitarRoles(idUsuario);

            // 5. Eliminar usuario
            System.out.println("   - Eliminando usuario...");
            boolean eliminado = usuarioDAO.eliminar(idUsuario);
            
            if (eliminado) {
                System.out.println("✅ UsuarioBean.eliminar: Usuario eliminado correctamente");
                FacesUtils.addInfoMessage("Usuario eliminado correctamente.");
            } else {
                System.err.println("❌ UsuarioBean.eliminar: No se pudo eliminar el usuario (posible restricción de clave foránea)");
                FacesUtils.addErrorMessage("No se pudo eliminar el usuario. Verifique la consola del servidor para más detalles.");
            }
            
            refrescar();
        } catch (Exception e) {
            System.err.println("❌ UsuarioBean.eliminar: Error al eliminar usuario: " + e.getMessage());
            System.err.println("   - Tipo de error: " + e.getClass().getName());
            e.printStackTrace();
            FacesUtils.addErrorMessage("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    public void filtrar() {
        boolean hayFiltros = (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) ||
                             (filtroRolId != null && filtroRolId > 0) ||
                             (filtroTipoDocumento != null && !filtroTipoDocumento.trim().isEmpty());
        
        if (!hayFiltros) {
            usuarios = usuarioDAO.listar();
            usuariosFiltrados = null;
            return;
        }
        
        List<Usuario> listaCompleta = usuarioDAO.listar();
        List<Usuario> resultado = new java.util.ArrayList<>(listaCompleta);
        
        if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
            resultado = usuarioDAO.filtrar(textoBusqueda, null);
        }
        
        if (filtroRolId != null && filtroRolId > 0) {
            List<Usuario> usuariosFiltradosPorRol = new java.util.ArrayList<>();
            for (Usuario u : resultado) {
                List<Integer> rolesUsuario = usuarioDAO.obtenerRolesIdsPorUsuarioId(u.getIdUsuario());
                if (rolesUsuario != null && rolesUsuario.contains(filtroRolId)) {
                    usuariosFiltradosPorRol.add(u);
                }
            }
            resultado = usuariosFiltradosPorRol;
        }
        
        if (filtroTipoDocumento != null && !filtroTipoDocumento.trim().isEmpty()) {
            List<Usuario> usuariosFiltradosPorTipo = new java.util.ArrayList<>();
            for (Usuario u : resultado) {
                if (filtroTipoDocumento.equals(u.getTipoDocumento())) {
                    usuariosFiltradosPorTipo.add(u);
                }
            }
            resultado = usuariosFiltradosPorTipo;
        }
        
        usuarios = resultado;
        usuariosFiltrados = null;
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
    public List<Usuario> getUsuarios() { 
        if (usuarios == null) {
            usuarios = usuarioDAO.listar();
        }
        return usuarios; 
    }
    public List<Usuario> getUsuariosFiltrados() { return usuariosFiltrados; }
    public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) { 
        this.usuariosFiltrados = usuariosFiltrados; 
    }
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
    
    public Integer getFiltroRolId() { return filtroRolId; }
    public void setFiltroRolId(Integer filtroRolId) { this.filtroRolId = filtroRolId; }
    
    public String getFiltroTipoDocumento() { return filtroTipoDocumento; }
    public void setFiltroTipoDocumento(String filtroTipoDocumento) { this.filtroTipoDocumento = filtroTipoDocumento; }
    
    public void limpiarFiltros() {
        filtroRolId = null;
        filtroTipoDocumento = null;
        textoBusqueda = null;
        usuariosFiltrados = null;
        usuarios = usuarioDAO.listar();
        FacesUtils.addInfoMessage("Filtros limpiados. Mostrando todos los usuarios.");
    }

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
