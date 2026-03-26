package dao;

import modelo.Aprendiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AprendizDAO {

    public Aprendiz buscarPorUsuario(int idUsuario) {
        String sql = "SELECT Usuario_id_usuario, programas_id_programas, ficha_idficha "
                + "FROM aprendiz WHERE Usuario_id_usuario=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
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

    public boolean guardar(Aprendiz aprendiz) {
        String sql = "INSERT INTO aprendiz (Usuario_id_usuario, programas_id_programas, ficha_idficha) "
                + "VALUES (?,?,?)";
        
<<<<<<< HEAD
        System.out.println("AprendizDAO.guardar: Intentando guardar aprendiz");
        System.out.println("ID Usuario: " + aprendiz.getIdUsuario());
        System.out.println("ID Programa: " + aprendiz.getProgramaId());
        System.out.println("ID Ficha: " + aprendiz.getFichaId());
        System.out.println("SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("AprendizDAO.guardar: No se pudo establecer conexión");
=======
        System.out.println("🔍 AprendizDAO.guardar: Intentando guardar aprendiz");
        System.out.println("   - ID Usuario: " + aprendiz.getIdUsuario());
        System.out.println("   - ID Programa: " + aprendiz.getProgramaId());
        System.out.println("   - ID Ficha: " + aprendiz.getFichaId());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ AprendizDAO.guardar: No se pudo establecer conexión");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, aprendiz.getIdUsuario());
                ps.setInt(2, aprendiz.getProgramaId());
                ps.setInt(3, aprendiz.getFichaId());
                
                int filas = ps.executeUpdate();
<<<<<<< HEAD
                System.out.println("AprendizDAO.guardar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("AprendizDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("AprendizDAO.guardar: Error inesperado: " + e.getMessage());
=======
                System.out.println("✅ AprendizDAO.guardar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ AprendizDAO.guardar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ AprendizDAO.guardar: Error inesperado: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizar(Aprendiz aprendiz) {
        String sql = "UPDATE aprendiz SET programas_id_programas=?, ficha_idficha=? WHERE Usuario_id_usuario=?";
        
<<<<<<< HEAD
        System.out.println("AprendizDAO.actualizar: Intentando actualizar aprendiz");
        System.out.println("ID Usuario: " + aprendiz.getIdUsuario());
        System.out.println("ID Programa: " + aprendiz.getProgramaId());
        System.out.println("ID Ficha: " + aprendiz.getFichaId());
        System.out.println("SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("AprendizDAO.actualizar: No se pudo establecer conexión");
=======
        System.out.println("🔍 AprendizDAO.actualizar: Intentando actualizar aprendiz");
        System.out.println("   - ID Usuario: " + aprendiz.getIdUsuario());
        System.out.println("   - ID Programa: " + aprendiz.getProgramaId());
        System.out.println("   - ID Ficha: " + aprendiz.getFichaId());
        System.out.println("   - SQL: " + sql);
        
        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ AprendizDAO.actualizar: No se pudo establecer conexión");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
                return false;
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, aprendiz.getProgramaId());
                ps.setInt(2, aprendiz.getFichaId());
                ps.setInt(3, aprendiz.getIdUsuario());
                
                int filas = ps.executeUpdate();
<<<<<<< HEAD
                System.out.println("AprendizDAO.actualizar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("AprendizDAO.actualizar: Error SQL: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("AprendizDAO.actualizar: Error inesperado: " + e.getMessage());
=======
                System.out.println("✅ AprendizDAO.actualizar: Filas afectadas: " + filas);
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ AprendizDAO.actualizar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ AprendizDAO.actualizar: Error inesperado: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminarPorUsuario(int idUsuario) {
        String sql = "DELETE FROM aprendiz WHERE Usuario_id_usuario=?";
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
    
    public java.util.List<Aprendiz> listar() {
        java.util.List<Aprendiz> aprendices = new java.util.ArrayList<>();
        String sql = "SELECT Usuario_id_usuario, programas_id_programas, ficha_idficha FROM aprendiz";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                aprendices.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aprendices;
    }
    
    public java.util.List<Aprendiz> buscarPorFicha(int idFicha) {
        java.util.List<Aprendiz> aprendices = new java.util.ArrayList<>();
        String sql = "SELECT Usuario_id_usuario, programas_id_programas, ficha_idficha FROM aprendiz WHERE ficha_idficha = ?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFicha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    aprendices.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
<<<<<<< HEAD
            System.err.println("AprendizDAO.buscarPorFicha: Error: " + e.getMessage());
=======
            System.err.println("❌ AprendizDAO.buscarPorFicha: Error: " + e.getMessage());
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }
        return aprendices;
    }

    private Aprendiz mapRow(ResultSet rs) throws SQLException {
        Aprendiz a = new Aprendiz();
        a.setIdUsuario(rs.getInt("Usuario_id_usuario"));
        a.setProgramaId(rs.getInt("programas_id_programas"));
        a.setFichaId(rs.getInt("ficha_idficha"));
        return a;
    }
}

