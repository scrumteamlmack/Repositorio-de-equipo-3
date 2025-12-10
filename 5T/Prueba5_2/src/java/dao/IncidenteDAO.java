package dao;

import modelo.Incidente;
import util.ReportFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncidenteDAO {

    public List<Incidente> listar() {
        System.out.println("🔍 IncidenteDAO.listar: Iniciando consulta de incidentes");
        List<Incidente> incidentes = new ArrayList<>();
        String sql = "SELECT id_incidente, descripcion, fecha_incidente, hora_incidente, ambiente_id, tipo_inc_id, usuario_id_usuario "
                + "FROM registro_incidente ORDER BY fecha_incidente ASC, hora_incidente ASC";
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
                    contador++;
                    System.out.println("   - Procesando registro #" + contador);
                    System.out.println("      - ID desde BD: " + rs.getInt("id_incidente"));
                    System.out.println("      - Ambiente desde BD: " + rs.getInt("ambiente_id"));
                    System.out.println("      - Tipo desde BD: " + rs.getInt("tipo_inc_id"));
                    System.out.println("      - Usuario desde BD: " + rs.getInt("usuario_id_usuario"));
                    System.out.println("      - Descripción desde BD: " + (rs.getString("descripcion") != null ? rs.getString("descripcion") : "NULL"));
                    System.out.println("      - Fecha desde BD: " + (rs.getDate("fecha_incidente") != null ? rs.getDate("fecha_incidente").toString() : "NULL"));
                    System.out.println("      - Hora desde BD: " + (rs.getTime("hora_incidente") != null ? rs.getTime("hora_incidente").toString() : "NULL"));
                    
                    Incidente inc = mapRow(rs);
                    System.out.println("      - Incidente mapeado - ID: " + inc.getIdIncidente() + 
                        ", Ambiente: " + inc.getIdAmbiente() +
                        ", Tipo: " + inc.getIdTipoIncidente() +
                        ", Usuario: " + inc.getIdReportador() +
                        ", Fecha: " + inc.getFecha() +
                        ", Hora: " + inc.getHora() +
                        ", Desc: " + (inc.getDescripcion() != null ? inc.getDescripcion().substring(0, Math.min(50, inc.getDescripcion().length())) : "NULL"));
                    
                    incidentes.add(inc);
                }
                System.out.println("✅ IncidenteDAO.listar: Total de incidentes encontrados y agregados: " + contador);
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
            ps.setDate(2, new java.sql.Date(incidente.getFecha() != null ? incidente.getFecha().getTime() : new Date().getTime()));
            ps.setString(3, incidente.getHora() != null ? incidente.getHora() : "00:00");
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
                ps.setDate(2, new java.sql.Date(incidente.getFecha().getTime()));
            } else {
                ps.setDate(2, new java.sql.Date(new Date().getTime()));
            }
            ps.setString(3, incidente.getHora() != null ? incidente.getHora() : "00:00");
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
        String sql = "SELECT id_incidente, descripcion, fecha_incidente, hora_incidente, ambiente_id, tipo_inc_id, usuario_id_usuario "
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
        StringBuilder sql = new StringBuilder("SELECT id_incidente, descripcion, fecha_incidente, hora_incidente, ambiente_id, tipo_inc_id, usuario_id_usuario "
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
            params.add(new java.sql.Date(filtro.getFechaDesde().getTime()));
        }
        if (filtro.getFechaHasta() != null) {
            sql.append(" AND fecha_incidente <= ?");
            params.add(new java.sql.Date(filtro.getFechaHasta().getTime()));
        }

        sql.append(" ORDER BY fecha_incidente ASC, hora_incidente ASC");

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
        try {
            incidente.setIdIncidente(rs.getInt("id_incidente"));
            System.out.println("      [mapRow] ID establecido: " + incidente.getIdIncidente());
        } catch (Exception e) {
            System.err.println("      [mapRow] Error al obtener id_incidente: " + e.getMessage());
        }
        
        try {
            incidente.setIdAmbiente(rs.getInt("ambiente_id"));
            System.out.println("      [mapRow] Ambiente establecido: " + incidente.getIdAmbiente());
        } catch (Exception e) {
            System.err.println("      [mapRow] Error al obtener ambiente_id: " + e.getMessage());
        }
        
        try {
            incidente.setIdTipoIncidente(rs.getInt("tipo_inc_id"));
            System.out.println("      [mapRow] Tipo establecido: " + incidente.getIdTipoIncidente());
        } catch (Exception e) {
            System.err.println("      [mapRow] Error al obtener tipo_inc_id: " + e.getMessage());
        }
        
        try {
            incidente.setIdReportador(rs.getInt("usuario_id_usuario"));
            System.out.println("      [mapRow] Reportador establecido: " + incidente.getIdReportador());
        } catch (Exception e) {
            System.err.println("      [mapRow] Error al obtener usuario_id_usuario: " + e.getMessage());
        }
        
        try {
            incidente.setDescripcion(rs.getString("descripcion"));
            System.out.println("      [mapRow] Descripción establecida: " + (incidente.getDescripcion() != null ? incidente.getDescripcion().substring(0, Math.min(30, incidente.getDescripcion().length())) : "NULL"));
        } catch (Exception e) {
            System.err.println("      [mapRow] Error al obtener descripcion: " + e.getMessage());
        }
        
        try {
            java.sql.Date f = rs.getDate("fecha_incidente");
            if (f != null) {
                incidente.setFecha(new Date(f.getTime()));
                System.out.println("      [mapRow] Fecha establecida: " + incidente.getFecha());
            } else {
                System.err.println("      [mapRow] fecha_incidente es NULL en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("      [mapRow] Error al obtener fecha_incidente: " + e.getMessage());
        }
        
        try {
            java.sql.Time t = rs.getTime("hora_incidente");
            if (t != null) {
                incidente.setHora(t.toString());
                System.out.println("      [mapRow] Hora establecida: " + incidente.getHora());
            } else {
                System.err.println("      [mapRow] hora_incidente es NULL en la base de datos");
            }
        } catch (Exception e) {
            System.err.println("      [mapRow] Error al obtener hora_incidente: " + e.getMessage());
        }
        
        return incidente;
    }
}

