package dao;

import modelo.Recurso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {

    public List<Recurso> listar() {
        List<Recurso> recursos = new ArrayList<>();
        String sql = "SELECT id_recurso, tipo_recurso, ambiente_id, nombre_recurso, serial_recurso, num_recurso, estado, observacion "
                + "FROM recursos ORDER BY id_recurso ASC";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                recursos.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recursos;
    }

    public Recurso buscarPorId(int id) {
        String sql = "SELECT id_recurso, tipo_recurso, ambiente_id, nombre_recurso, serial_recurso, num_recurso, estado, observacion "
                + "FROM recursos WHERE id_recurso=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int guardar(Recurso recurso) {
        String sql = "INSERT INTO recursos (tipo_recurso, ambiente_id, nombre_recurso, serial_recurso, num_recurso, estado, observacion) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, recurso.getIdTipoRecurso());
            ps.setInt(2, recurso.getIdAmbiente());
            ps.setString(3, recurso.getNombre());
            ps.setString(4, recurso.getSerial());
            ps.setInt(5, recurso.getNumero());
            ps.setString(6, recurso.getEstado());
            ps.setString(7, recurso.getObservacion());
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

    public boolean actualizar(Recurso recurso) {
        String sql = "UPDATE recursos SET tipo_recurso=?, ambiente_id=?, nombre_recurso=?, serial_recurso=?, num_recurso=?, estado=?, observacion=? WHERE id_recurso=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recurso.getIdTipoRecurso());
            ps.setInt(2, recurso.getIdAmbiente());
            ps.setString(3, recurso.getNombre());
            ps.setString(4, recurso.getSerial());
            ps.setInt(5, recurso.getNumero());
            ps.setString(6, recurso.getEstado());
            ps.setString(7, recurso.getObservacion());
            ps.setInt(8, recurso.getIdRecurso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM recursos WHERE id_recurso=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Recurso mapRow(ResultSet rs) throws SQLException {
        Recurso recurso = new Recurso();
        recurso.setIdRecurso(rs.getInt("id_recurso"));
        recurso.setIdTipoRecurso(rs.getInt("tipo_recurso"));
        recurso.setIdAmbiente(rs.getInt("ambiente_id"));
        recurso.setNombre(rs.getString("nombre_recurso"));
        recurso.setSerial(rs.getString("serial_recurso"));
        recurso.setNumero(rs.getInt("num_recurso"));
        recurso.setEstado(rs.getString("estado"));
        recurso.setObservacion(rs.getString("observacion"));
        return recurso;
    }
}

