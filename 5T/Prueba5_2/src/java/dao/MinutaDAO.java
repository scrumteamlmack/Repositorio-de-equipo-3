package dao;

import modelo.Minuta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MinutaDAO {

    public List<Minuta> listar() {
        List<Minuta> minutas = new ArrayList<>();
        String sql = "SELECT rm.id_minuta, rm.fecha_hora_recibo, rm.fecha_hora_entrega, rm.novedad, rm.descripcion_min, rm.estado, "
                + "rm.ambiente_id, rm.guarda_seguridad_Usuario_id_usuario, rm.responsable_id, "
                + "CONCAT(a.num_ambiente, ' - ', a.tipo_ambiente) AS ambiente_nombre, "
                + "CONCAT(ug.p_nombre, ' ', ug.p_apellido) AS guarda_nombre, "
                + "CONCAT(ui.p_nombre, ' ', ui.p_apellido) AS responsable_nombre "
                + "FROM registro_minuta rm "
                + "LEFT JOIN ambiente a ON rm.ambiente_id = a.id_ambiente "
                + "LEFT JOIN usuario ug ON rm.guarda_seguridad_Usuario_id_usuario = ug.id_usuario "
                + "LEFT JOIN usuario ui ON rm.responsable_id = ui.id_usuario "
                + "ORDER BY rm.id_minuta ASC";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                minutas.add(mapRowWithNames(rs));
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
            ps.setTimestamp(1, Timestamp.valueOf(minuta.getFechaRecibo() != null ? minuta.getFechaRecibo() : LocalDateTime.now()));
            ps.setTimestamp(2, Timestamp.valueOf(minuta.getFechaEntrega() != null ? minuta.getFechaEntrega() : LocalDateTime.now()));
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
                ps.setTimestamp(1, Timestamp.valueOf(minuta.getFechaRecibo()));
            } else {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            }
            if (minuta.getFechaEntrega() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(minuta.getFechaEntrega()));
            } else {
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
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
            minuta.setFechaRecibo(recibo.toLocalDateTime());
        }
        Timestamp entrega = rs.getTimestamp("fecha_hora_entrega");
        if (entrega != null) {
            minuta.setFechaEntrega(entrega.toLocalDateTime());
        }
        minuta.setNovedad(rs.getString("novedad"));
        minuta.setDescripcion(rs.getString("descripcion_min"));
        minuta.setEstado(rs.getString("estado"));
        minuta.setAmbienteId(rs.getInt("ambiente_id"));
        minuta.setGuardaId(rs.getInt("guarda_seguridad_Usuario_id_usuario"));
        minuta.setResponsableId(rs.getInt("responsable_id"));
        return minuta;
    }
    
    private Minuta mapRowWithNames(ResultSet rs) throws SQLException {
        Minuta minuta = mapRow(rs);
        minuta.setAmbienteNombre(rs.getString("ambiente_nombre"));
        minuta.setGuardaNombre(rs.getString("guarda_nombre"));
        minuta.setResponsableNombre(rs.getString("responsable_nombre"));
        return minuta;
    }
}

