package dao;

import modelo.Jornada;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JornadaDAO {
    
    public List<Jornada> listar() {
        System.out.println("JornadaDAO.listar: Iniciando consulta de jornadas");
        List<Jornada> jornadas = new ArrayList<>();
        String sql = "SELECT id_jornada, nombre_jornada FROM jornada ORDER BY nombre_jornada";
        System.out.println("SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("JornadaDAO.listar: No se pudo establecer conexión");
                return jornadas;
            }
            System.out.println("JornadaDAO.listar: Conexión establecida");
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                System.out.println("Ejecutando consulta...");
                int contador = 0;
                
                while (rs.next()) {
                    Jornada j = new Jornada();
                    j.setIdJornada(rs.getInt("id_jornada"));
                    j.setNombreJornada(rs.getString("nombre_jornada"));
                    jornadas.add(j);
                    contador++;
                    System.out.println("Jornada encontrada: ID=" + j.getIdJornada() + ", Nombre=" + j.getNombreJornada());
                }
                
                System.out.println("JornadaDAO.listar: Total de jornadas encontradas: " + contador);
            }
            
        } catch (SQLException e) {
            System.err.println("JornadaDAO.listar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        
        return jornadas;
    }
}

