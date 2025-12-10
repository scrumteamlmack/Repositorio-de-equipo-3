package dao;

import modelo.Minuta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MinutaDAO {

    public List<Minuta> listar() {
        List<Minuta> minutas = new ArrayList<>();
        String sql = "SELECT id_minuta, fecha_hora_recibo, fecha_hora_entrega, novedad, descripcion_min, estado, ambiente_id, guarda_seguridad_Usuario_id_usuario, responsable_id "
                + "FROM registro_minuta ORDER BY fecha_hora_recibo DESC";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                minutas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return minutas;
    }

    public int guardar(Minuta minuta) {
        String sql = "INSERT INTO registro_minuta (fecha_hora_recibo, fecha_hora_entrega, novedad, descripcion_min, estado, ambiente_id, guarda_seguridad_Usuario_id_usuario, responsable_id) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(minuta.getFechaRecibo() != null ? minuta.getFechaRecibo().getTime() : new Date().getTime()));
            ps.setTimestamp(2, new Timestamp(minuta.getFechaEntrega() != null ? minuta.getFechaEntrega().getTime() : new Date().getTime()));
            ps.setString(3, minuta.getNovedad());
            ps.setString(4, minuta.getDescripcion());
            ps.setString(5, minuta.getEstado());
            ps.setInt(6, minuta.getAmbienteId());
            ps.setInt(7, minuta.getGuardaId());
            ps.setInt(8, minuta.getResponsableId());
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

    public boolean actualizar(Minuta minuta) {
        String sql = "UPDATE registro_minuta SET fecha_hora_recibo=?, fecha_hora_entrega=?, novedad=?, descripcion_min=?, estado=?, ambiente_id=?, guarda_seguridad_Usuario_id_usuario=?, responsable_id=? "
                + "WHERE id_minuta=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (minuta.getFechaRecibo() != null) {
                ps.setTimestamp(1, new Timestamp(minuta.getFechaRecibo().getTime()));
            } else {
                ps.setTimestamp(1, new Timestamp(new Date().getTime()));
            }
            if (minuta.getFechaEntrega() != null) {
                ps.setTimestamp(2, new Timestamp(minuta.getFechaEntrega().getTime()));
            } else {
                ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            }
            ps.setString(3, minuta.getNovedad());
            ps.setString(4, minuta.getDescripcion());
            ps.setString(5, minuta.getEstado());
            ps.setInt(6, minuta.getAmbienteId());
            ps.setInt(7, minuta.getGuardaId());
            ps.setInt(8, minuta.getResponsableId());
            ps.setInt(9, minuta.getIdMinuta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Minuta buscarPorId(int id) {
        String sql = "SELECT id_minuta, fecha_hora_recibo, fecha_hora_entrega, novedad, descripcion_min, estado, ambiente_id, guarda_seguridad_Usuario_id_usuario, responsable_id "
                + "FROM registro_minuta WHERE id_minuta=?";
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
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM registro_minuta WHERE id_minuta=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Minuta mapRow(ResultSet rs) throws SQLException {
        Minuta minuta = new Minuta();
        minuta.setIdMinuta(rs.getInt("id_minuta"));
        Timestamp recibo = rs.getTimestamp("fecha_hora_recibo");
        if (recibo != null) {
            minuta.setFechaRecibo(new Date(recibo.getTime()));
        }
        Timestamp entrega = rs.getTimestamp("fecha_hora_entrega");
        if (entrega != null) {
            minuta.setFechaEntrega(new Date(entrega.getTime()));
        }
        minuta.setNovedad(rs.getString("novedad"));
        minuta.setDescripcion(rs.getString("descripcion_min"));
        minuta.setEstado(rs.getString("estado"));
        minuta.setAmbienteId(rs.getInt("ambiente_id"));
        minuta.setGuardaId(rs.getInt("guarda_seguridad_Usuario_id_usuario"));
        minuta.setResponsableId(rs.getInt("responsable_id"));
        return minuta;
    }
}

