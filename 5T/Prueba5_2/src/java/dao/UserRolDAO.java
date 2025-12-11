package dao;

import modelo.UserRol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRolDAO {

    public List<UserRol> listarPorUsuario(int idUsuario) {
        List<UserRol> lista = new ArrayList<>();
        String sql = "SELECT id_user_rol, id_usuario, id_rol FROM user_rol WHERE id_usuario=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int guardar(UserRol userRol) {
        String sql = "INSERT INTO user_rol (id_usuario, id_rol) VALUES (?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userRol.getIdUsuario());
            ps.setInt(2, userRol.getIdRol());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean eliminarPorUsuario(int idUsuario) {
        String sql = "DELETE FROM user_rol WHERE id_usuario=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private UserRol mapRow(ResultSet rs) throws SQLException {
        UserRol ur = new UserRol();
        ur.setIdUserRol(rs.getInt("id_user_rol"));
        ur.setIdUsuario(rs.getInt("id_usuario"));
        ur.setIdRol(rs.getInt("id_rol"));
        return ur;
    }
}

