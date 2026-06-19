package dao;

import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String BASE_SELECT = "SELECT id_usuario, p_nombre, s_nombre, p_apellido, s_apellido, "
            + "tipo_documento, num_documento, correo, `contraseña` AS contrasena "
            + "FROM usuario";

    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(BASE_SELECT);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public int guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario (p_nombre, s_nombre, p_apellido, s_apellido, tipo_documento, "
                + "num_documento, correo, `contraseña`) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getPNombre());
            ps.setString(2, usuario.getSNombre());
            ps.setString(3, usuario.getPApellido());
            ps.setString(4, usuario.getSApellido());
            ps.setString(5, usuario.getTipoDocumento());
            ps.setInt(6, usuario.getNumDocumento());
            ps.setString(7, usuario.getCorreo());
            ps.setString(8, usuario.getContrasena());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET p_nombre=?, s_nombre=?, p_apellido=?, s_apellido=?, tipo_documento=?, "
                + "num_documento=?, correo=?, `contraseña`=? WHERE id_usuario=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getPNombre());
            ps.setString(2, usuario.getSNombre());
            ps.setString(3, usuario.getPApellido());
            ps.setString(4, usuario.getSApellido());
            ps.setString(5, usuario.getTipoDocumento());
            ps.setInt(6, usuario.getNumDocumento());
            ps.setString(7, usuario.getCorreo());
            ps.setString(8, usuario.getContrasena());
            ps.setInt(9, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Usuario buscarPorId(int id) {
        String sql = BASE_SELECT + " WHERE id_usuario=?";
<<<<<<< HEAD
        System.out.println("UsuarioDAO.buscarPorId: Buscando usuario con ID: " + id);
        System.out.println("SQL: " + sql);
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("UsuarioDAO.buscarPorId: No se pudo establecer conexión");
=======
        System.out.println("🔍 UsuarioDAO.buscarPorId: Buscando usuario con ID: " + id);
        System.out.println("   - SQL: " + sql);
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ UsuarioDAO.buscarPorId: No se pudo establecer conexión");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                return null;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Usuario u = mapRow(rs);
<<<<<<< HEAD
                        System.out.println(" UsuarioDAO.buscarPorId: Usuario encontrado:");
                        System.out.println(" ID: " + u.getIdUsuario());
                        System.out.println(" Nombre: " + u.getPNombre() + " " + u.getPApellido());
                        System.out.println(" Correo: " + u.getCorreo());
                        System.out.println(" TipoDoc: " + u.getTipoDocumento());
                        System.out.println(" NumDoc: " + u.getNumDocumento());
                        return u;
                    } else {
                        System.err.println("UsuarioDAO.buscarPorId: No se encontró usuario con ID: " + id);
=======
                        System.out.println("   ✅ UsuarioDAO.buscarPorId: Usuario encontrado:");
                        System.out.println("      * ID: " + u.getIdUsuario());
                        System.out.println("      * Nombre: " + u.getPNombre() + " " + u.getPApellido());
                        System.out.println("      * Correo: " + u.getCorreo());
                        System.out.println("      * TipoDoc: " + u.getTipoDocumento());
                        System.out.println("      * NumDoc: " + u.getNumDocumento());
                        return u;
                    } else {
                        System.err.println("   ❌ UsuarioDAO.buscarPorId: No se encontró usuario con ID: " + id);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                    }
                }
            }
        } catch (SQLException e) {
<<<<<<< HEAD
            System.err.println("UsuarioDAO.buscarPorId: Error SQL: " + e.getMessage());
=======
            System.err.println("❌ UsuarioDAO.buscarPorId: Error SQL: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return null;
    }

    public Usuario buscarPorDocumento(int documento) {
        String sql = BASE_SELECT + " WHERE num_documento=?";
<<<<<<< HEAD
        System.out.println("UsuarioDAO: Buscando usuario con documento: " + documento);
        System.out.println("UsuarioDAO: SQL: " + sql);
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("UsuarioDAO: No se pudo establecer conexión a la base de datos");
                return null;
            }
            System.out.println("UsuarioDAO: Conexión establecida");
=======
        System.out.println("🔍 UsuarioDAO: Buscando usuario con documento: " + documento);
        System.out.println("🔍 UsuarioDAO: SQL: " + sql);
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ UsuarioDAO: No se pudo establecer conexión a la base de datos");
                return null;
            }
            System.out.println("✅ UsuarioDAO: Conexión establecida");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, documento);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Usuario usuario = mapRow(rs);
<<<<<<< HEAD
                        System.out.println("UsuarioDAO: Usuario encontrado - ID: " + usuario.getIdUsuario() + ", Nombre: " + usuario.getPNombre());
                        return usuario;
                    } else {
                        System.out.println("UsuarioDAO: No se encontró ningún usuario con documento: " + documento);
=======
                        System.out.println("✅ UsuarioDAO: Usuario encontrado - ID: " + usuario.getIdUsuario() + ", Nombre: " + usuario.getPNombre());
                        return usuario;
                    } else {
                        System.out.println("⚠️ UsuarioDAO: No se encontró ningún usuario con documento: " + documento);
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                    }
                }
            }
        } catch (SQLException e) {
<<<<<<< HEAD
            System.err.println("UsuarioDAO: Error SQL al buscar usuario: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("UsuarioDAO: Error inesperado: " + e.getMessage());
=======
            System.err.println("❌ UsuarioDAO: Error SQL al buscar usuario: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ UsuarioDAO: Error inesperado: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return null;
    }

    public boolean existeDocumento(int documento, Integer excluyendoId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM usuario WHERE num_documento=?");
        if (excluyendoId != null) {
            sql.append(" AND id_usuario <> ?");
        }
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setInt(1, documento);
            if (excluyendoId != null) {
                ps.setInt(2, excluyendoId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            int filas = ps.executeUpdate();
<<<<<<< HEAD
            System.out.println("UsuarioDAO.eliminar: Filas afectadas: " + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("UsuarioDAO.eliminar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
=======
            System.out.println("🔍 UsuarioDAO.eliminar: Filas afectadas: " + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ UsuarioDAO.eliminar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> obtenerRolesIdsPorUsuarioId(int userId) {
        List<Integer> roles = new ArrayList<>();
        String sql = "SELECT id_rol FROM user_rol WHERE id_usuario = ?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getInt("id_rol"));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return roles;
    }

    public boolean asignarRol(int userId, int roleId) {
        String sql = "INSERT INTO user_rol (id_usuario, id_rol) VALUES (?, ?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean quitarRoles(int userId) {
        String sql = "DELETE FROM user_rol WHERE id_usuario = ?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<Usuario> filtrar(String criterio, Integer rolId) {
        List<Usuario> usuarios = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> params = new ArrayList<>();

        if (rolId != null) {
            sql.append(" JOIN user_rol ur ON ur.id_usuario = usuario.id_usuario WHERE ur.id_rol=?");
            params.add(rolId);
        } else {
            sql.append(" WHERE 1=1");
        }

        if (criterio != null && !criterio.isEmpty()) {
            sql.append(" AND (LOWER(p_nombre) LIKE ? OR LOWER(p_apellido) LIKE ? OR num_documento LIKE ?)");
            String pattern = "%" + criterio.toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add("%" + criterio + "%");
        }

        sql.append(" ORDER BY id_usuario DESC");

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public List<Usuario> listarPorRol(String nombreRol) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT DISTINCT u.id_usuario, u.p_nombre, u.s_nombre, u.p_apellido, u.s_apellido, "
                + "u.tipo_documento, u.num_documento, u.correo, u.`contraseña` AS contrasena "
                + "FROM usuario u "
                + "INNER JOIN user_rol ur ON u.id_usuario = ur.id_usuario "
                + "INNER JOIN rol r ON ur.id_rol = r.id_rol "
                + "WHERE r.nombre_rol = ? "
                + "ORDER BY u.p_nombre, u.p_apellido";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreRol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setPNombre(rs.getString("p_nombre"));
        u.setSNombre(rs.getString("s_nombre"));
        u.setPApellido(rs.getString("p_apellido"));
        u.setSApellido(rs.getString("s_apellido"));
        u.setTipoDocumento(rs.getString("tipo_documento"));
        u.setNumDocumento(rs.getInt("num_documento"));
        u.setCorreo(rs.getString("correo"));
        u.setContrasena(rs.getString("contrasena"));
        return u;
    }
}
