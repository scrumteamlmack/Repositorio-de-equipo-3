package dao;

import modelo.TipoIncidente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoIncidenteDAO {

    public List<TipoIncidente> listar() {
        List<TipoIncidente> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo_inc, tipo_incidente AS nombre, observacion_inc AS descripcion FROM tipo_incidente ORDER BY tipo_incidente";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tipos.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }

    public int guardar(TipoIncidente tipo) {
        String sql = "INSERT INTO tipo_incidente (tipo_incidente, observacion_inc) VALUES (?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());
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

    public boolean actualizar(TipoIncidente tipo) {
        String sql = "UPDATE tipo_incidente SET tipo_incidente=?, observacion_inc=? WHERE id_tipo_inc=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());
            ps.setInt(3, tipo.getIdTipoIncidente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tipo_incidente WHERE id_tipo_inc=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private TipoIncidente mapRow(ResultSet rs) throws SQLException {
        TipoIncidente tipo = new TipoIncidente();
        tipo.setIdTipoIncidente(rs.getInt("id_tipo_inc"));
        tipo.setNombre(rs.getString("nombre"));
        tipo.setDescripcion(rs.getString("descripcion"));
        return tipo;
    }
}

