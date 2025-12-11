package dao;

import modelo.Modalidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModalidadDAO {
    
    public List<Modalidad> listar() {
        System.out.println("🔍 ModalidadDAO.listar: Iniciando consulta de modalidades");
        List<Modalidad> lista = new ArrayList<>();
        String sql = "SELECT id_modalidad, nombre_modalidad FROM modalidad ORDER BY nombre_modalidad ASC";
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ ModalidadDAO.listar: No se pudo establecer conexión");
                return lista;
            }
            System.out.println("✅ ModalidadDAO.listar: Conexión establecida");
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                System.out.println("   - Ejecutando consulta...");
                int contador = 0;
                
                while (rs.next()) {
                    Modalidad m = new Modalidad();
                    m.setIdModalidad(rs.getInt("id_modalidad"));
                    m.setNombreModalidad(rs.getString("nombre_modalidad"));
                    lista.add(m);
                    contador++;
                    System.out.println("   - Modalidad encontrada: ID=" + m.getIdModalidad() + ", Nombre=" + m.getNombreModalidad());
                }
                
                System.out.println("✅ ModalidadDAO.listar: Total de modalidades encontradas: " + contador);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ModalidadDAO.listar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public Modalidad buscarPorId(int id) {
        String sql = "SELECT id_modalidad, nombre_modalidad FROM modalidad WHERE id_modalidad = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Modalidad m = new Modalidad();
                    m.setIdModalidad(rs.getInt("id_modalidad"));
                    m.setNombreModalidad(rs.getString("nombre_modalidad"));
                    return m;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ModalidadDAO.buscarPorId: Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}

