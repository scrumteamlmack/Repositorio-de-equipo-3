package dao;

import modelo.Incidente;
import util.ReportFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class IncidenteDAO {

    public List<Incidente> listar() {
        System.out.println("🔍 IncidenteDAO.listar: Iniciando consulta de incidentes");
        List<Incidente> incidentes = new ArrayList<>();
        String sql = "SELECT id_incidente, ambiente_id, tipo_inc_id, usuario_id_usuario, descripcion, fecha_incidente, hora_incidente "
                + "FROM registro_incidente ORDER BY fecha_incidente DESC, hora_incidente DESC";
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ IncidenteDAO.listar: No se pudo establecer conexión");
                return incidentes;
            }
            System.out.println("✅ IncidenteDAO.listar: Conexión establecida");
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                System.out.println("   - Ejecutando consulta...");
                int contador = 0;
                while (rs.next()) {
                    Incidente inc = mapRow(rs);
                    incidentes.add(inc);
                    contador++;
                    System.out.println("   - Incidente encontrado: ID=" + inc.getIdIncidente() + 
                        ", Desc=" + (inc.getDescripcion() != null ? inc.getDescripcion().substring(0, Math.min(30, inc.getDescripcion().length())) : "Sin descripción"));
                }
                System.out.println("✅ IncidenteDAO.listar: Total de incidentes encontrados: " + contador);
            }
        } catch (SQLException e) {
            System.err.println("❌ IncidenteDAO.listar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ IncidenteDAO.listar: Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🔍 IncidenteDAO.listar: Retornando lista con " + incidentes.size() + " incidentes");
        return incidentes;
    }

    public int guardar(Incidente incidente) {
        String sql = "INSERT INTO registro_incidente (descripcion, fecha_incidente, hora_incidente, ambiente_id, tipo_inc_id, usuario_id_usuario) "
                + "VALUES (?,?,?,?,?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, incidente.getDescripcion());
            ps.setDate(2, java.sql.Date.valueOf(incidente.getFecha() != null ? incidente.getFecha() : LocalDate.now()));
            ps.setTime(3, java.sql.Time.valueOf(incidente.getHora() != null ? incidente.getHora() : LocalTime.now()));
            ps.setInt(4, incidente.getIdAmbiente());
            ps.setInt(5, incidente.getIdTipoIncidente());
            ps.setInt(6, incidente.getIdReportador());
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

    public boolean actualizar(Incidente incidente) {
        String sql = "UPDATE registro_incidente SET descripcion=?, fecha_incidente=?, hora_incidente=?, ambiente_id=?, tipo_inc_id=? "
                + "WHERE id_incidente=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, incidente.getDescripcion());
            if (incidente.getFecha() != null) {
                ps.setDate(2, java.sql.Date.valueOf(incidente.getFecha()));
            } else {
                ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            }
            if (incidente.getHora() != null) {
                ps.setTime(3, java.sql.Time.valueOf(incidente.getHora()));
            } else {
                ps.setTime(3, java.sql.Time.valueOf(LocalTime.now()));
            }
            ps.setInt(4, incidente.getIdAmbiente());
            ps.setInt(5, incidente.getIdTipoIncidente());
            ps.setInt(6, incidente.getIdIncidente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Incidente buscarPorId(int id) {
        String sql = "SELECT id_incidente, ambiente_id, tipo_inc_id, usuario_id_usuario, descripcion, fecha_incidente, hora_incidente "
                + "FROM registro_incidente WHERE id_incidente=?";
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
        String sql = "DELETE FROM registro_incidente WHERE id_incidente=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Incidente> filtrar(ReportFilter filtro) {
        List<Incidente> incidentes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id_incidente, ambiente_id, tipo_inc_id, usuario_id_usuario, descripcion, fecha_incidente, hora_incidente "
                + "FROM registro_incidente WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (filtro.getAmbienteId() != null) {
            sql.append(" AND ambiente_id=?");
            params.add(filtro.getAmbienteId());
        }
        if (filtro.getTipoIncidenteId() != null) {
            sql.append(" AND tipo_inc_id=?");
            params.add(filtro.getTipoIncidenteId());
        }
        if (filtro.getUsuarioId() != null) {
            sql.append(" AND usuario_id_usuario=?");
            params.add(filtro.getUsuarioId());
        }
        if (filtro.getFechaDesde() != null) {
            sql.append(" AND fecha_incidente >= ?");
            params.add(java.sql.Date.valueOf(filtro.getFechaDesde()));
        }
        if (filtro.getFechaHasta() != null) {
            sql.append(" AND fecha_incidente <= ?");
            params.add(java.sql.Date.valueOf(filtro.getFechaHasta()));
        }

        sql.append(" ORDER BY fecha_incidente DESC, hora_incidente DESC");

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    incidentes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incidentes;
    }

    private Incidente mapRow(ResultSet rs) throws SQLException {
        Incidente incidente = new Incidente();
        incidente.setIdIncidente(rs.getInt("id_incidente"));
        incidente.setIdAmbiente(rs.getInt("ambiente_id"));
        incidente.setIdTipoIncidente(rs.getInt("tipo_inc_id"));
        incidente.setIdReportador(rs.getInt("usuario_id_usuario"));
        incidente.setDescripcion(rs.getString("descripcion"));
        java.sql.Date f = rs.getDate("fecha_incidente");
        if (f != null) {
            incidente.setFecha(f.toLocalDate());
        }
        java.sql.Time t = rs.getTime("hora_incidente");
        if (t != null) {
            incidente.setHora(t.toLocalTime());
        }
        return incidente;
    }
}

