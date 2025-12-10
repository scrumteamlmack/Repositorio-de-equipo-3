package dao;

import modelo.GuardaSeguridad;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuardaSeguridadDAO {
    
    public boolean guardar(GuardaSeguridad guardaSeguridad) {
        String sql = "INSERT INTO guarda_seguridad (Usuario_id_usuario, turno, fecha_ingreso, estado) VALUES (?, ?, ?, ?)";
        
        System.out.println("🔍 GuardaSeguridadDAO.guardar: Intentando guardar guarda de seguridad");
        System.out.println("   - ID Usuario: " + guardaSeguridad.getIdUsuario());
        System.out.println("   - Turno: " + guardaSeguridad.getTurno());
        System.out.println("   - Fecha Ingreso: " + guardaSeguridad.getFechaIngreso());
        System.out.println("   - Estado: " + guardaSeguridad.getEstado());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ GuardaSeguridadDAO.guardar: No se pudo establecer conexión");
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, guardaSeguridad.getIdUsuario());
                ps.setString(2, guardaSeguridad.getTurno());
                ps.setDate(3, new Date(guardaSeguridad.getFechaIngreso().getTime()));
                ps.setString(4, guardaSeguridad.getEstado());
                
                int filas = ps.executeUpdate();
                System.out.println("✅ GuardaSeguridadDAO.guardar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ GuardaSeguridadDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ GuardaSeguridadDAO.guardar: Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean actualizar(GuardaSeguridad guardaSeguridad) {
        String sql = "UPDATE guarda_seguridad SET turno=?, fecha_ingreso=?, estado=? WHERE Usuario_id_usuario=?";
        
        System.out.println("🔍 GuardaSeguridadDAO.actualizar: Intentando actualizar guarda de seguridad");
        System.out.println("   - ID Usuario: " + guardaSeguridad.getIdUsuario());
        System.out.println("   - Turno: " + guardaSeguridad.getTurno());
        System.out.println("   - Fecha Ingreso: " + guardaSeguridad.getFechaIngreso());
        System.out.println("   - Estado: " + guardaSeguridad.getEstado());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ GuardaSeguridadDAO.actualizar: No se pudo establecer conexión");
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, guardaSeguridad.getTurno());
                ps.setDate(2, new Date(guardaSeguridad.getFechaIngreso().getTime()));
                ps.setString(3, guardaSeguridad.getEstado());
                ps.setInt(4, guardaSeguridad.getIdUsuario());
                
                int filas = ps.executeUpdate();
                System.out.println("✅ GuardaSeguridadDAO.actualizar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ GuardaSeguridadDAO.actualizar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ GuardaSeguridadDAO.actualizar: Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public GuardaSeguridad buscarPorUsuario(int idUsuario) {
        String sql = "SELECT Usuario_id_usuario, turno, fecha_ingreso, estado FROM guarda_seguridad WHERE Usuario_id_usuario = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    GuardaSeguridad gs = new GuardaSeguridad();
                    gs.setIdUsuario(rs.getInt("Usuario_id_usuario"));
                    gs.setTurno(rs.getString("turno"));
                    if (rs.getDate("fecha_ingreso") != null) {
                        gs.setFechaIngreso(new java.util.Date(rs.getDate("fecha_ingreso").getTime()));
                    }
                    gs.setEstado(rs.getString("estado"));
                    return gs;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean eliminarPorUsuario(int idUsuario) {
        String sql = "DELETE FROM guarda_seguridad WHERE Usuario_id_usuario = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public java.util.List<GuardaSeguridad> listar() {
        java.util.List<GuardaSeguridad> guardas = new java.util.ArrayList<>();
        String sql = "SELECT Usuario_id_usuario, turno, fecha_ingreso, estado FROM guarda_seguridad";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GuardaSeguridad gs = new GuardaSeguridad();
                gs.setIdUsuario(rs.getInt("Usuario_id_usuario"));
                gs.setTurno(rs.getString("turno"));
                if (rs.getDate("fecha_ingreso") != null) {
                    gs.setFechaIngreso(new java.util.Date(rs.getDate("fecha_ingreso").getTime()));
                }
                gs.setEstado(rs.getString("estado"));
                guardas.add(gs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guardas;
    }
}
