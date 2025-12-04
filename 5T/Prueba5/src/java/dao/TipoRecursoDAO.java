package dao;

import modelo.TipoRecurso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoRecursoDAO {

    public List<TipoRecurso> listar() {
        List<TipoRecurso> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo_recurso, recurso_tipo AS nombre, descripcion_tipo AS descripcion FROM tipo_recurso ORDER BY recurso_tipo";
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

    public int guardar(TipoRecurso tipo) {
        String sql = "INSERT INTO tipo_recurso (recurso_tipo, descripcion_tipo) VALUES (?, ?)";
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

    public boolean actualizar(TipoRecurso tipo) {
        String sql = "UPDATE tipo_recurso SET recurso_tipo=?, descripcion_tipo=? WHERE id_tipo_recurso=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());
            ps.setInt(3, tipo.getIdTipoRecurso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tipo_recurso WHERE id_tipo_recurso=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private TipoRecurso mapRow(ResultSet rs) throws SQLException {
        TipoRecurso tipo = new TipoRecurso();
        tipo.setIdTipoRecurso(rs.getInt("id_tipo_recurso"));
        tipo.setNombre(rs.getString("nombre"));
        tipo.setDescripcion(rs.getString("descripcion"));
        return tipo;
    }
}

