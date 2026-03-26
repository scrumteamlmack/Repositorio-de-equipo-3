package dao;

import modelo.Coordinador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoordinadorDAO {
    
    public boolean guardar(Coordinador coordinador) {
        String sql = "INSERT INTO coordinador (Usuario_id_usuario, coordinacion_id_coordinacion) VALUES (?, ?)";
        
<<<<<<< HEAD
        System.out.println("CoordinadorDAO.guardar: Intentando guardar coordinador");
        System.out.println("ID Usuario: " + coordinador.getIdUsuario());
        System.out.println("ID Coordinación: " + coordinador.getCoordinacionId());
        System.out.println("SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("CoordinadorDAO.guardar: No se pudo establecer conexión");
=======
        System.out.println("🔍 CoordinadorDAO.guardar: Intentando guardar coordinador");
        System.out.println("   - ID Usuario: " + coordinador.getIdUsuario());
        System.out.println("   - ID Coordinación: " + coordinador.getCoordinacionId());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ CoordinadorDAO.guardar: No se pudo establecer conexión");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, coordinador.getIdUsuario());
                ps.setInt(2, coordinador.getCoordinacionId());
                
                int filas = ps.executeUpdate();
<<<<<<< HEAD
                System.out.println("CoordinadorDAO.guardar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("CoordinadorDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CoordinadorDAO.guardar: Error inesperado: " + e.getMessage());
=======
                System.out.println("✅ CoordinadorDAO.guardar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ CoordinadorDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ CoordinadorDAO.guardar: Error inesperado: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean actualizar(Coordinador coordinador) {
        String sql = "UPDATE coordinador SET coordinacion_id_coordinacion=? WHERE Usuario_id_usuario=?";
        
<<<<<<< HEAD
        System.out.println("CoordinadorDAO.actualizar: Intentando actualizar coordinador");
        System.out.println("ID Usuario: " + coordinador.getIdUsuario());
        System.out.println("ID Coordinación: " + coordinador.getCoordinacionId());
        System.out.println("SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("CoordinadorDAO.actualizar: No se pudo establecer conexión");
=======
        System.out.println("🔍 CoordinadorDAO.actualizar: Intentando actualizar coordinador");
        System.out.println("   - ID Usuario: " + coordinador.getIdUsuario());
        System.out.println("   - ID Coordinación: " + coordinador.getCoordinacionId());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ CoordinadorDAO.actualizar: No se pudo establecer conexión");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, coordinador.getCoordinacionId());
                ps.setInt(2, coordinador.getIdUsuario());
                
                int filas = ps.executeUpdate();
<<<<<<< HEAD
                System.out.println("CoordinadorDAO.actualizar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("CoordinadorDAO.actualizar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CoordinadorDAO.actualizar: Error inesperado: " + e.getMessage());
=======
                System.out.println("✅ CoordinadorDAO.actualizar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ CoordinadorDAO.actualizar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ CoordinadorDAO.actualizar: Error inesperado: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return false;
    }
    
    public Coordinador buscarPorUsuario(int idUsuario) {
        String sql = "SELECT Usuario_id_usuario, coordinacion_id_coordinacion FROM coordinador WHERE Usuario_id_usuario = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coordinador c = new Coordinador();
                    c.setIdUsuario(rs.getInt("Usuario_id_usuario"));
                    c.setCoordinacionId(rs.getInt("coordinacion_id_coordinacion"));
                    return c;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean eliminarPorUsuario(int idUsuario) {
        String sql = "DELETE FROM coordinador WHERE Usuario_id_usuario = ?";
        
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
}
