package dao;

import modelo.Asistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsistenciaDAO {

    public List<Asistencia> listar() {
        List<Asistencia> asistencias = new ArrayList<>();
        String sql = "SELECT id_inasistencia, aprendiz_Usuario_id_usuario, instructor_Usuario_id_usuario, jornada_id, estado_inasistencia, fecha_inasistencia "
                + "FROM registro_inasistencia ORDER BY fecha_inasistencia DESC";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                asistencias.add(mapRow(rs));
            }
        } catch (SQLException e) {
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
            ps.setDate(5, new java.sql.Date(asistencia.getFecha() != null ? asistencia.getFecha().getTime() : new Date().getTime()));
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
                ps.setDate(5, new java.sql.Date(asistencia.getFecha().getTime()));
            } else {
                ps.setDate(5, new java.sql.Date(new Date().getTime()));
            }
            ps.setInt(6, asistencia.getIdAsistencia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Asistencia buscarPorId(int id) {
        String sql = "SELECT id_inasistencia, aprendiz_Usuario_id_usuario, instructor_Usuario_id_usuario, jornada_id, estado_inasistencia, fecha_inasistencia "
                + "FROM registro_inasistencia WHERE id_inasistencia=?";
        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
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

    private Asistencia mapRow(ResultSet rs) throws SQLException {
        Asistencia asistencia = new Asistencia();
        asistencia.setIdAsistencia(rs.getInt("id_inasistencia"));
        asistencia.setAprendizUsuarioId(rs.getInt("aprendiz_Usuario_id_usuario"));
        asistencia.setInstructorUsuarioId(rs.getInt("instructor_Usuario_id_usuario"));
        asistencia.setJornadaId(rs.getInt("jornada_id"));
        java.sql.Date fecha = rs.getDate("fecha_inasistencia");
        if (fecha != null) {
            asistencia.setFecha(new Date(fecha.getTime()));
        }
        asistencia.setEstado(rs.getString("estado_inasistencia"));
        return asistencia;
    }
}

