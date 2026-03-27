package dao;

import modelo.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {
    
    public List<Rol> listar() {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre_rol FROM rol ORDER BY nombre_rol ASC";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Rol r = new Rol();
                r.setIdRol(rs.getInt("id_rol"));
                r.setNombre(rs.getString("nombre_rol"));
                lista.add(r);
            }
            
        } catch (SQLException e) {
<<<<<<< HEAD
            System.err.println("RolDAO: Error al listar roles: " + e.getMessage());
=======
            System.err.println("❌ RolDAO: Error al listar roles: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public Rol buscarPorId(int id) {
        String sql = "SELECT id_rol, nombre_rol FROM rol WHERE id_rol = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rol r = new Rol();
                    r.setIdRol(rs.getInt("id_rol"));
                    r.setNombre(rs.getString("nombre_rol"));
                    return r;
                }
            }
            
        } catch (SQLException e) {
<<<<<<< HEAD
            System.err.println("RolDAO: Error al buscar rol por ID: " + e.getMessage());
=======
            System.err.println("❌ RolDAO: Error al buscar rol por ID: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        
        return null;
    }
}
