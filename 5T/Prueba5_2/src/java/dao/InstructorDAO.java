package dao;

import modelo.Instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstructorDAO {
    
    public boolean guardar(Instructor instructor) {
        String sql = "INSERT INTO instructor (Usuario_id_usuario, email, telefono, coordinacion_id_coordinacion, estado) VALUES (?, ?, ?, ?, ?)";
        
<<<<<<< HEAD
        System.out.println("InstructorDAO.guardar: Intentando guardar instructor");
        System.out.println("ID Usuario: " + instructor.getIdUsuario());
        System.out.println("Email: " + instructor.getEmail());
        System.out.println("Teléfono: " + instructor.getTelefono());
        System.out.println("ID Coordinación: " + instructor.getCoordinacionId());
        System.out.println("Estado: " + instructor.getEstado());
        System.out.println("SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("InstructorDAO.guardar: No se pudo establecer conexión");
=======
        System.out.println("🔍 InstructorDAO.guardar: Intentando guardar instructor");
        System.out.println("   - ID Usuario: " + instructor.getIdUsuario());
        System.out.println("   - Email: " + instructor.getEmail());
        System.out.println("   - Teléfono: " + instructor.getTelefono());
        System.out.println("   - ID Coordinación: " + instructor.getCoordinacionId());
        System.out.println("   - Estado: " + instructor.getEstado());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ InstructorDAO.guardar: No se pudo establecer conexión");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, instructor.getIdUsuario());
                ps.setString(2, instructor.getEmail());
                ps.setString(3, instructor.getTelefono());
                ps.setInt(4, instructor.getCoordinacionId());
                ps.setString(5, instructor.getEstado());
                
                int filas = ps.executeUpdate();
<<<<<<< HEAD
                System.out.println("InstructorDAO.guardar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("InstructorDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("InstructorDAO.guardar: Error inesperado: " + e.getMessage());
=======
                System.out.println("✅ InstructorDAO.guardar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ InstructorDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ InstructorDAO.guardar: Error inesperado: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean actualizar(Instructor instructor) {
        String sql = "UPDATE instructor SET email=?, telefono=?, coordinacion_id_coordinacion=?, estado=? WHERE Usuario_id_usuario=?";
        
<<<<<<< HEAD
        System.out.println("InstructorDAO.actualizar: Intentando actualizar instructor");
        System.out.println("ID Usuario: " + instructor.getIdUsuario());
        System.out.println("Email: " + instructor.getEmail());
        System.out.println("Teléfono: " + instructor.getTelefono());
        System.out.println("ID Coordinación: " + instructor.getCoordinacionId());
        System.out.println("Estado: " + instructor.getEstado());
        System.out.println("SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("InstructorDAO.actualizar: No se pudo establecer conexión");
=======
        System.out.println("🔍 InstructorDAO.actualizar: Intentando actualizar instructor");
        System.out.println("   - ID Usuario: " + instructor.getIdUsuario());
        System.out.println("   - Email: " + instructor.getEmail());
        System.out.println("   - Teléfono: " + instructor.getTelefono());
        System.out.println("   - ID Coordinación: " + instructor.getCoordinacionId());
        System.out.println("   - Estado: " + instructor.getEstado());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ InstructorDAO.actualizar: No se pudo establecer conexión");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, instructor.getEmail());
                ps.setString(2, instructor.getTelefono());
                ps.setInt(3, instructor.getCoordinacionId());
                ps.setString(4, instructor.getEstado());
                ps.setInt(5, instructor.getIdUsuario());
                
                int filas = ps.executeUpdate();
<<<<<<< HEAD
                System.out.println("InstructorDAO.actualizar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("InstructorDAO.actualizar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("InstructorDAO.actualizar: Error inesperado: " + e.getMessage());
=======
                System.out.println("✅ InstructorDAO.actualizar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ InstructorDAO.actualizar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ InstructorDAO.actualizar: Error inesperado: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return false;
    }
    
    public Instructor buscarPorUsuario(int idUsuario) {
        String sql = "SELECT Usuario_id_usuario, email, telefono, coordinacion_id_coordinacion, estado FROM instructor WHERE Usuario_id_usuario = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Instructor i = new Instructor();
                    i.setIdUsuario(rs.getInt("Usuario_id_usuario"));
                    i.setEmail(rs.getString("email"));
                    i.setTelefono(rs.getString("telefono"));
                    i.setCoordinacionId(rs.getInt("coordinacion_id_coordinacion"));
                    i.setEstado(rs.getString("estado"));
                    return i;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean eliminarPorUsuario(int idUsuario) {
        String sql = "DELETE FROM instructor WHERE Usuario_id_usuario = ?";
        
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
    
    public java.util.List<Instructor> listar() {
        java.util.List<Instructor> instructores = new java.util.ArrayList<>();
        String sql = "SELECT Usuario_id_usuario, email, telefono, coordinacion_id_coordinacion, estado FROM instructor";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Instructor i = new Instructor();
                i.setIdUsuario(rs.getInt("Usuario_id_usuario"));
                i.setEmail(rs.getString("email"));
                i.setTelefono(rs.getString("telefono"));
                i.setCoordinacionId(rs.getInt("coordinacion_id_coordinacion"));
                i.setEstado(rs.getString("estado"));
                instructores.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructores;
    }
}
