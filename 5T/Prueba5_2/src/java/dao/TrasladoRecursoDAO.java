package dao;

import modelo.TrasladoRecurso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrasladoRecursoDAO {

    public List<TrasladoRecurso> listar() {
        List<TrasladoRecurso> traslados = new ArrayList<>();
        String sql = "SELECT tr.id_traslado, tr.recurso_id, tr.ambiente_origen, tr.ambiente_destino, tr.fecha_traslado, tr.observacion, "
                + "r.nombre_recurso AS recurso_nombre, "
                + "CONCAT(ao.num_ambiente, ' - ', ao.tipo_ambiente) AS ambiente_origen_nombre, "
                + "CONCAT(ad.num_ambiente, ' - ', ad.tipo_ambiente) AS ambiente_destino_nombre "
                + "FROM traslado_recurso tr "
                + "LEFT JOIN recursos r ON tr.recurso_id = r.id_recurso "
                + "LEFT JOIN ambiente ao ON tr.ambiente_origen = ao.id_ambiente "
                + "LEFT JOIN ambiente ad ON tr.ambiente_destino = ad.id_ambiente "
                + "ORDER BY tr.id_traslado ASC";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                traslados.add(mapRowWithNames(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.listar: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return traslados;
    }

    public int guardar(TrasladoRecurso traslado) {
        String sql = "INSERT INTO traslado_recurso (recurso_id, ambiente_origen, ambiente_destino, fecha_traslado, observacion) "
                + "VALUES (?,?,?,?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, traslado.getRecursoId());
            ps.setInt(2, traslado.getAmbienteOrigen());
            ps.setInt(3, traslado.getAmbienteDestino());
            ps.setTimestamp(4, Timestamp.valueOf(traslado.getFechaTraslado() != null ? traslado.getFechaTraslado() : LocalDateTime.now()));
            ps.setString(5, traslado.getObservacion());
            ps.executeUpdate();
            
            // Actualizar el ambiente del recurso al destino
            String sqlUpdateRecurso = "UPDATE recursos SET ambiente_id=? WHERE id_recurso=?";
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdateRecurso)) {
                psUpdate.setInt(1, traslado.getAmbienteDestino());
                psUpdate.setInt(2, traslado.getRecursoId());
                psUpdate.executeUpdate();
            }
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.guardar: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean actualizar(TrasladoRecurso traslado) {
        String sql = "UPDATE traslado_recurso SET recurso_id=?, ambiente_origen=?, ambiente_destino=?, fecha_traslado=?, observacion=? "
                + "WHERE id_traslado=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, traslado.getRecursoId());
            ps.setInt(2, traslado.getAmbienteOrigen());
            ps.setInt(3, traslado.getAmbienteDestino());
            ps.setTimestamp(4, Timestamp.valueOf(traslado.getFechaTraslado() != null ? traslado.getFechaTraslado() : LocalDateTime.now()));
            ps.setString(5, traslado.getObservacion());
            ps.setInt(6, traslado.getIdTraslado());
            
            int rows = ps.executeUpdate();
            
            // Actualizar el ambiente del recurso al destino
            if (rows > 0) {
                String sqlUpdateRecurso = "UPDATE recursos SET ambiente_id=? WHERE id_recurso=?";
                try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdateRecurso)) {
                    psUpdate.setInt(1, traslado.getAmbienteDestino());
                    psUpdate.setInt(2, traslado.getRecursoId());
                    psUpdate.executeUpdate();
                }
            }
            
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.actualizar: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int idTraslado) {
        String sql = "DELETE FROM traslado_recurso WHERE id_traslado=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTraslado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.eliminar: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public TrasladoRecurso buscarPorId(int idTraslado) {
        String sql = "SELECT tr.id_traslado, tr.recurso_id, tr.ambiente_origen, tr.ambiente_destino, tr.fecha_traslado, tr.observacion, "
                + "r.nombre_recurso AS recurso_nombre, "
                + "CONCAT(ao.num_ambiente, ' - ', ao.tipo_ambiente) AS ambiente_origen_nombre, "
                + "CONCAT(ad.num_ambiente, ' - ', ad.tipo_ambiente) AS ambiente_destino_nombre "
                + "FROM traslado_recurso tr "
                + "LEFT JOIN recursos r ON tr.recurso_id = r.id_recurso "
                + "LEFT JOIN ambiente ao ON tr.ambiente_origen = ao.id_ambiente "
                + "LEFT JOIN ambiente ad ON tr.ambiente_destino = ad.id_ambiente "
                + "WHERE tr.id_traslado=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTraslado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowWithNames(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.buscarPorId: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private TrasladoRecurso mapRowWithNames(ResultSet rs) throws SQLException {
        TrasladoRecurso traslado = new TrasladoRecurso();
        traslado.setIdTraslado(rs.getInt("id_traslado"));
        traslado.setRecursoId(rs.getInt("recurso_id"));
        traslado.setAmbienteOrigen(rs.getInt("ambiente_origen"));
        traslado.setAmbienteDestino(rs.getInt("ambiente_destino"));
        Timestamp ts = rs.getTimestamp("fecha_traslado");
        if (ts != null) {
            traslado.setFechaTraslado(ts.toLocalDateTime());
        }
        traslado.setObservacion(rs.getString("observacion"));
        traslado.setRecursoNombre(rs.getString("recurso_nombre"));
        traslado.setAmbienteOrigenNombre(rs.getString("ambiente_origen_nombre"));
        traslado.setAmbienteDestinoNombre(rs.getString("ambiente_destino_nombre"));
        return traslado;
    }
}

