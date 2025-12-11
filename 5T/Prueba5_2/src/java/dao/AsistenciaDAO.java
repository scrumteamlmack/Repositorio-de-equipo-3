package dao;

import modelo.Asistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {

    public List<Asistencia> listar() {
        List<Asistencia> asistencias = new ArrayList<>();
        String sql = "SELECT ri.id_inasistencia, ri.aprendiz_Usuario_id_usuario, ri.instructor_Usuario_id_usuario, "
                + "ri.jornada_id, ri.estado_inasistencia, ri.fecha_inasistencia, "
                + "TRIM(CONCAT(IFNULL(ua.p_nombre, ''), ' ', IFNULL(ua.p_apellido, ''))) AS aprendiz_nombre, "
                + "TRIM(CONCAT(IFNULL(ui.p_nombre, ''), ' ', IFNULL(ui.p_apellido, ''))) AS instructor_nombre, "
                + "j.nombre_jornada AS jornada_nombre "
                + "FROM registro_inasistencia ri "
                + "LEFT JOIN usuario ua ON ri.aprendiz_Usuario_id_usuario = ua.id_usuario "
                + "LEFT JOIN usuario ui ON ri.instructor_Usuario_id_usuario = ui.id_usuario "
                + "LEFT JOIN jornada j ON ri.jornada_id = j.id_jornada "
                + "ORDER BY ri.id_inasistencia ASC";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                asistencias.add(mapRowWithNames(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ AsistenciaDAO.listar: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return asistencias;
    }
    
    public List<Asistencia> listarPorAprendiz(int idAprendiz) {
        List<Asistencia> asistencias = new ArrayList<>();
        String sql = "SELECT ri.id_inasistencia, ri.aprendiz_Usuario_id_usuario, ri.instructor_Usuario_id_usuario, ri.jornada_id, ri.estado_inasistencia, ri.fecha_inasistencia, "
                + "TRIM(CONCAT(IFNULL(ua.p_nombre, ''), ' ', IFNULL(ua.p_apellido, ''))) AS aprendiz_nombre, "
                + "TRIM(CONCAT(IFNULL(ui.p_nombre, ''), ' ', IFNULL(ui.p_apellido, ''))) AS instructor_nombre, "
                + "j.nombre_jornada AS jornada_nombre "
                + "FROM registro_inasistencia ri "
                + "LEFT JOIN usuario ua ON ri.aprendiz_Usuario_id_usuario = ua.id_usuario "
                + "LEFT JOIN usuario ui ON ri.instructor_Usuario_id_usuario = ui.id_usuario "
                + "LEFT JOIN jornada j ON ri.jornada_id = j.id_jornada "
                + "WHERE ri.aprendiz_Usuario_id_usuario = ? "
                + "ORDER BY ri.id_inasistencia ASC";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAprendiz);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asistencias.add(mapRowWithNames(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ AsistenciaDAO.listarPorAprendiz: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return asistencias;
    }

    public int guardar(Asistencia asistencia) {
        String sql = "INSERT INTO registro_inasistencia (aprendiz_Usuario_id_usuario, instructor_Usuario_id_usuario, jornada_id, estado_inasistencia, fecha_inasistencia) "
                + "VALUES (?,?,?,?,?)";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, asistencia.getAprendizUsuarioId());
            ps.setInt(2, asistencia.getInstructorUsuarioId());
            ps.setInt(3, asistencia.getJornadaId());
            ps.setString(4, asistencia.getEstado());
            ps.setDate(5, Date.valueOf(asistencia.getFecha() != null ? asistencia.getFecha() : LocalDate.now()));
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

    public boolean actualizar(Asistencia asistencia) {
        String sql = "UPDATE registro_inasistencia SET aprendiz_Usuario_id_usuario=?, instructor_Usuario_id_usuario=?, jornada_id=?, estado_inasistencia=?, fecha_inasistencia=? "
                + "WHERE id_inasistencia=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, asistencia.getAprendizUsuarioId());
            ps.setInt(2, asistencia.getInstructorUsuarioId());
            ps.setInt(3, asistencia.getJornadaId());
            ps.setString(4, asistencia.getEstado());
            if (asistencia.getFecha() != null) {
                ps.setDate(5, Date.valueOf(asistencia.getFecha()));
            } else {
                ps.setDate(5, Date.valueOf(LocalDate.now()));
            }
            ps.setInt(6, asistencia.getIdAsistencia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Asistencia buscarPorId(int id) {
        String sql = "SELECT ri.id_inasistencia, ri.aprendiz_Usuario_id_usuario, ri.instructor_Usuario_id_usuario, "
                + "ri.jornada_id, ri.estado_inasistencia, ri.fecha_inasistencia, "
                + "TRIM(CONCAT(IFNULL(ua.p_nombre, ''), ' ', IFNULL(ua.p_apellido, ''))) AS aprendiz_nombre, "
                + "TRIM(CONCAT(IFNULL(ui.p_nombre, ''), ' ', IFNULL(ui.p_apellido, ''))) AS instructor_nombre, "
                + "j.nombre_jornada AS jornada_nombre "
                + "FROM registro_inasistencia ri "
                + "LEFT JOIN usuario ua ON ri.aprendiz_Usuario_id_usuario = ua.id_usuario "
                + "LEFT JOIN usuario ui ON ri.instructor_Usuario_id_usuario = ui.id_usuario "
                + "LEFT JOIN jornada j ON ri.jornada_id = j.id_jornada "
                + "WHERE ri.id_inasistencia=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowWithNames(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ AsistenciaDAO.buscarPorId: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM registro_inasistencia WHERE id_inasistencia=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean eliminarPorInstructor(int instructorId) {
        String sql = "DELETE FROM registro_inasistencia WHERE instructor_Usuario_id_usuario = ?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, instructorId);
            int filas = ps.executeUpdate();
            System.out.println("✅ AsistenciaDAO.eliminarPorInstructor: Eliminados " + filas + " registros de asistencia del instructor ID: " + instructorId);
            return true;
        } catch (SQLException e) {
            System.err.println("❌ AsistenciaDAO.eliminarPorInstructor: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Asistencia mapRow(ResultSet rs) throws SQLException {
        Asistencia asistencia = new Asistencia();
        asistencia.setIdAsistencia(rs.getInt("id_inasistencia"));
        asistencia.setAprendizUsuarioId(rs.getInt("aprendiz_Usuario_id_usuario"));
        asistencia.setInstructorUsuarioId(rs.getInt("instructor_Usuario_id_usuario"));
        asistencia.setJornadaId(rs.getInt("jornada_id"));
        Date fecha = rs.getDate("fecha_inasistencia");
        if (fecha != null) {
            asistencia.setFecha(fecha.toLocalDate());
        }
        asistencia.setEstado(rs.getString("estado_inasistencia"));
        return asistencia;
    }
    
    private Asistencia mapRowWithNames(ResultSet rs) throws SQLException {
        Asistencia asistencia = mapRow(rs);
        try {
            String aprendizNombre = rs.getString("aprendiz_nombre");
            if (aprendizNombre != null && !aprendizNombre.trim().isEmpty()) {
                asistencia.setAprendizNombre(aprendizNombre.trim());
            }
            
            String instructorNombre = rs.getString("instructor_nombre");
            if (instructorNombre != null && !instructorNombre.trim().isEmpty()) {
                asistencia.setInstructorNombre(instructorNombre.trim());
            }
            
            String jornadaNombre = rs.getString("jornada_nombre");
            if (jornadaNombre != null && !jornadaNombre.trim().isEmpty()) {
                asistencia.setJornadaNombre(jornadaNombre.trim());
            }
        } catch (SQLException e) {
            System.err.println("⚠️ AsistenciaDAO.mapRowWithNames: Error al obtener nombres: " + e.getMessage());
            // Si las columnas no existen, simplemente no las asignamos
        }
        return asistencia;
    }
}

