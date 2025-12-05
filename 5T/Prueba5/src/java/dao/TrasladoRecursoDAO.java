package dao;

import modelo.TrasladoRecurso;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar los traslados de recursos entre ambientes
 */
public class TrasladoRecursoDAO {

    /**
     * Lista todos los traslados con información de recursos y ambientes
     */
    public List<TrasladoRecurso> listar() {
        List<TrasladoRecurso> traslados = new ArrayList<>();
        String sql = "SELECT t.id_traslado, t.recurso_id, t.ambiente_origen, t.ambiente_destino, "
                   + "t.fecha_traslado, t.observacion, r.nombre_recurso, "
                   + "ao.num_ambiente AS num_origen, ad.num_ambiente AS num_destino "
                   + "FROM traslado_recurso t "
                   + "INNER JOIN recursos r ON t.recurso_id = r.id_recurso "
                   + "INNER JOIN ambiente ao ON t.ambiente_origen = ao.id_ambiente "
                   + "INNER JOIN ambiente ad ON t.ambiente_destino = ad.id_ambiente "
                   + "ORDER BY t.fecha_traslado DESC";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                traslados.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.listar: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return traslados;
    }

    /**
     * Busca un traslado por su ID
     */
    public TrasladoRecurso buscarPorId(int id) {
        String sql = "SELECT t.id_traslado, t.recurso_id, t.ambiente_origen, t.ambiente_destino, "
                   + "t.fecha_traslado, t.observacion, r.nombre_recurso, "
                   + "ao.num_ambiente AS num_origen, ad.num_ambiente AS num_destino "
                   + "FROM traslado_recurso t "
                   + "INNER JOIN recursos r ON t.recurso_id = r.id_recurso "
                   + "INNER JOIN ambiente ao ON t.ambiente_origen = ao.id_ambiente "
                   + "INNER JOIN ambiente ad ON t.ambiente_destino = ad.id_ambiente "
                   + "WHERE t.id_traslado = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.buscarPorId: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Guarda un nuevo traslado y actualiza el ambiente del recurso
     */
    public int guardar(TrasladoRecurso traslado) {
        String sqlTraslado = "INSERT INTO traslado_recurso (recurso_id, ambiente_origen, ambiente_destino, fecha_traslado, observacion) "
                           + "VALUES (?, ?, ?, ?, ?)";
        String sqlActualizarRecurso = "UPDATE recursos SET ambiente_id = ? WHERE id_recurso = ?";
        
        Connection con = null;
        try {
            con = ConnBD.conectar();
            con.setAutoCommit(false);
            
            // Insertar traslado
            int idGenerado = -1;
            try (PreparedStatement ps = con.prepareStatement(sqlTraslado, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, traslado.getRecursoId());
                ps.setInt(2, traslado.getAmbienteOrigenId());
                ps.setInt(3, traslado.getAmbienteDestinoId());
                ps.setTimestamp(4, Timestamp.valueOf(traslado.getFechaTraslado()));
                ps.setString(5, traslado.getObservacion());
                ps.executeUpdate();
                
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        idGenerado = keys.getInt(1);
                    }
                }
            }
            
            // Actualizar ambiente del recurso
            try (PreparedStatement ps = con.prepareStatement(sqlActualizarRecurso)) {
                ps.setInt(1, traslado.getAmbienteDestinoId());
                ps.setInt(2, traslado.getRecursoId());
                ps.executeUpdate();
            }
            
            con.commit();
            System.out.println("✅ TrasladoRecursoDAO.guardar: Traslado guardado con ID: " + idGenerado);
            return idGenerado;
            
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.guardar: Error: " + e.getMessage());
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * Elimina un traslado (solo el registro, no revierte el cambio de ambiente)
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM traslado_recurso WHERE id_traslado = ?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean eliminado = ps.executeUpdate() > 0;
            if (eliminado) {
                System.out.println("✅ TrasladoRecursoDAO.eliminar: Traslado eliminado ID: " + id);
            }
            return eliminado;
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.eliminar: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtiene el ambiente actual de un recurso
     */
    public int obtenerAmbienteActualRecurso(int recursoId) {
        String sql = "SELECT ambiente_id FROM recursos WHERE id_recurso = ?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recursoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ambiente_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ TrasladoRecursoDAO.obtenerAmbienteActualRecurso: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private TrasladoRecurso mapRow(ResultSet rs) throws SQLException {
        TrasladoRecurso traslado = new TrasladoRecurso();
        traslado.setIdTraslado(rs.getInt("id_traslado"));
        traslado.setRecursoId(rs.getInt("recurso_id"));
        traslado.setAmbienteOrigenId(rs.getInt("ambiente_origen"));
        traslado.setAmbienteDestinoId(rs.getInt("ambiente_destino"));
        
        Timestamp ts = rs.getTimestamp("fecha_traslado");
        if (ts != null) {
            traslado.setFechaTraslado(ts.toLocalDateTime());
        }
        
        traslado.setObservacion(rs.getString("observacion"));
        traslado.setNombreRecurso(rs.getString("nombre_recurso"));
        traslado.setAmbienteOrigen("Ambiente " + rs.getInt("num_origen"));
        traslado.setAmbienteDestino("Ambiente " + rs.getInt("num_destino"));
        
        return traslado;
    }
}

