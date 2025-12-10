package dao;

import modelo.Coordinacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoordinacionDAO {
    
    public List<Coordinacion> listar() {
        System.out.println("🔍 CoordinacionDAO.listar: Iniciando consulta de coordinaciones");
        List<Coordinacion> lista = new ArrayList<>();
        String sql = "SELECT id_coordinacion, nombre_coordinacion, correo_coordinacion FROM coordinacion ORDER BY nombre_coordinacion ASC";
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ CoordinacionDAO.listar: No se pudo establecer conexión");
                return lista;
            }
            System.out.println("✅ CoordinacionDAO.listar: Conexión establecida");
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                System.out.println("   - Ejecutando consulta...");
                int contador = 0;
                
                while (rs.next()) {
                    Coordinacion c = new Coordinacion();
                    c.setIdCoordinacion(rs.getInt("id_coordinacion"));
                    c.setNombreCoordinacion(rs.getString("nombre_coordinacion"));
                    c.setCorreoCoordinacion(rs.getString("correo_coordinacion"));
                    lista.add(c);
                    contador++;
                    System.out.println("   - Coordinación encontrada: ID=" + c.getIdCoordinacion() + ", Nombre=" + c.getNombreCoordinacion());
                }
                
                System.out.println("✅ CoordinacionDAO.listar: Total de coordinaciones encontradas: " + contador);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ CoordinacionDAO.listar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public Coordinacion buscarPorId(int id) {
        String sql = "SELECT id_coordinacion, nombre_coordinacion, correo_coordinacion FROM coordinacion WHERE id_coordinacion = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coordinacion c = new Coordinacion();
                    c.setIdCoordinacion(rs.getInt("id_coordinacion"));
                    c.setNombreCoordinacion(rs.getString("nombre_coordinacion"));
                    c.setCorreoCoordinacion(rs.getString("correo_coordinacion"));
                    return c;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}

