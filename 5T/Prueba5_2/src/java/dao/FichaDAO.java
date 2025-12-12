package dao;

import modelo.Ficha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FichaDAO {

    private static final String BASE_SELECT = "SELECT idficha, Num_ficha, instructor_Usuario_id_usuario FROM ficha";

    public List<Ficha> listar() {
        List<Ficha> lista = new ArrayList<>();
        String sql = BASE_SELECT + " ORDER BY Num_ficha ASC";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ficha f = mapRow(rs);
                lista.add(f);
            }

        } catch (SQLException e) {
            System.err.println("FichaDAO.listar: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    public Ficha buscarPorId(int idFicha) {
        String sql = BASE_SELECT + " WHERE idficha = ?";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFicha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("FichaDAO.buscarPorId: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<Ficha> buscarPorNumero(int numFicha) {
        List<Ficha> lista = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE Num_ficha = ? ORDER BY idficha ASC";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numFicha);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("FichaDAO.buscarPorNumero: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    public List<Ficha> buscarPorInstructor(int instructorId) {
        System.out.println("FichaDAO.buscarPorInstructor: Buscando fichas para instructor ID: " + instructorId);
        List<Ficha> lista = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE instructor_Usuario_id_usuario = ? ORDER BY Num_ficha ASC";
        System.out.println(" SQL: " + sql);

        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("FichaDAO.buscarPorInstructor: No se pudo establecer conexión");
                return lista;
            }
            System.out.println("Conexión establecida");
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, instructorId);
                System.out.println("Parámetro instructorId: " + instructorId);

                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("Ejecutando consulta...");
                    int contador = 0;
                    while (rs.next()) {
                        Ficha f = mapRow(rs);
                        lista.add(f);
                        contador++;
                        System.out.println("Ficha encontrada: ID=" + f.getIdFicha() + ", Num=" + f.getNumFicha());
                    }
                    System.out.println("Total fichas encontradas: " + contador);
                }
            }

        } catch (SQLException e) {
            System.err.println("FichaDAO.buscarPorInstructor: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }

        return lista;
    }

    public int guardar(Ficha ficha) {
       
        int siguienteId = obtenerSiguienteId();
        if (siguienteId <= 0) {
            System.err.println("FichaDAO.guardar: No se pudo obtener el siguiente ID");
            return 0;
        }

        String sql = "INSERT INTO ficha (idficha, Num_ficha, instructor_Usuario_id_usuario) VALUES (?, ?, ?)";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, siguienteId);
            ps.setInt(2, ficha.getNumFicha());
            ps.setInt(3, ficha.getInstructorUsuarioId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("FichaDAO.guardar: Ficha guardada con ID: " + siguienteId);
                return siguienteId;
            } else {
                System.err.println("FichaDAO.guardar: No se insertaron filas");
            }

        } catch (SQLException e) {
            System.err.println("FichaDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }

        return 0;
    }

    private int obtenerSiguienteId() {
        String sql = "SELECT COALESCE(MAX(idficha), 0) + 1 AS siguiente_id FROM ficha";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int siguienteId = rs.getInt("siguiente_id");
                System.out.println("FichaDAO.obtenerSiguienteId: Siguiente ID: " + siguienteId);
                return siguienteId;
            }
            
        } catch (SQLException e) {
            System.err.println("FichaDAO.obtenerSiguienteId: Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }

    public boolean actualizar(Ficha ficha) {
        String sql = "UPDATE ficha SET Num_ficha = ?, instructor_Usuario_id_usuario = ? WHERE idficha = ?";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ficha.getNumFicha());
            ps.setInt(2, ficha.getInstructorUsuarioId());
            ps.setInt(3, ficha.getIdFicha());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("FichaDAO.actualizar: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminar(int idFicha) {
        String sql = "DELETE FROM ficha WHERE idficha = ?";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFicha);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("FichaDAO.eliminar: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminarPorNumero(int numFicha) {
        String sql = "DELETE FROM ficha WHERE Num_ficha = ?";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numFicha);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("FichaDAO.eliminarPorNumero: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean eliminarPorInstructor(int instructorId) {
        String sql = "DELETE FROM ficha WHERE instructor_Usuario_id_usuario = ?";
        
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, instructorId);
            int filas = ps.executeUpdate();
            System.out.println("FichaDAO.eliminarPorInstructor: Eliminadas " + filas + " fichas del instructor ID: " + instructorId);
            return true;
            
        } catch (SQLException e) {
            System.err.println("FichaDAO.eliminarPorInstructor: Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private Ficha mapRow(ResultSet rs) throws SQLException {
        Ficha f = new Ficha();
        f.setIdFicha(rs.getInt("idficha"));
        f.setNumFicha(rs.getInt("Num_ficha"));
        f.setInstructorUsuarioId(rs.getInt("instructor_Usuario_id_usuario"));
        return f;
    }
}
