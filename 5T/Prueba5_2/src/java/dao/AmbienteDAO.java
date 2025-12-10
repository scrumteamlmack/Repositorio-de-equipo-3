package dao;

import modelo.Ambiente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AmbienteDAO {

    public List<Ambiente> listar() {
        List<Ambiente> ambientes = new ArrayList<>();
        String sql = "SELECT id_ambiente, num_ambiente, capacidad, tipo_ambiente, estado FROM ambiente ORDER BY num_ambiente";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ambientes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ambientes;
    }

    public Ambiente buscarPorId(int id) {
        String sql = "SELECT id_ambiente, num_ambiente, capacidad, tipo_ambiente, estado FROM ambiente WHERE id_ambiente=?";
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

    public int guardar(Ambiente ambiente) {
        String sql = "INSERT INTO ambiente (num_ambiente, capacidad, tipo_ambiente, estado) VALUES (?,?,?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ambiente.getNumero());
            ps.setInt(2, ambiente.getCapacidad());
            ps.setString(3, ambiente.getTipo());
            ps.setString(4, ambiente.getEstado());
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

    public boolean actualizar(Ambiente ambiente) {
        String sql = "UPDATE ambiente SET num_ambiente=?, capacidad=?, tipo_ambiente=?, estado=? WHERE id_ambiente=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ambiente.getNumero());
            ps.setInt(2, ambiente.getCapacidad());
            ps.setString(3, ambiente.getTipo());
            ps.setString(4, ambiente.getEstado());
            ps.setInt(5, ambiente.getIdAmbiente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM ambiente WHERE id_ambiente=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Ambiente mapRow(ResultSet rs) throws SQLException {
        Ambiente ambiente = new Ambiente();
        ambiente.setIdAmbiente(rs.getInt("id_ambiente"));
        ambiente.setNumero(rs.getInt("num_ambiente"));
        ambiente.setCapacidad(rs.getInt("capacidad"));
        ambiente.setTipo(rs.getString("tipo_ambiente"));
        ambiente.setEstado(rs.getString("estado"));
        return ambiente;
    }
}

