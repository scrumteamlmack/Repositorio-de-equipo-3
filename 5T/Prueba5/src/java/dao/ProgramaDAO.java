package dao;

import modelo.Programa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {

    private static final String BASE_SELECT = "SELECT id_programas, nombre_programa, nivel_formacion, duracion, jornada_id, modalidad_id, coordinacion_id FROM programas";

    public List<Programa> listar() {
        System.out.println("🔍 ProgramaDAO.listar: Iniciando consulta de programas");
        List<Programa> lista = new ArrayList<>();
        String sql = BASE_SELECT + " ORDER BY nombre_programa ASC";
        System.out.println("   - SQL: " + sql);

        try (Connection con = ConnBD.conectar()) {
            if (con == null) {
                System.err.println("❌ ProgramaDAO.listar: No se pudo establecer conexión");
                return lista;
            }
            System.out.println("✅ ProgramaDAO.listar: Conexión establecida");

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("   - Ejecutando consulta...");
                int contador = 0;
                
                while (rs.next()) {
                    Programa p = mapRow(rs);
                    lista.add(p);
                    contador++;
                    System.out.println("   - Programa encontrado: ID=" + p.getIdProgramas() + ", Nombre=" + p.getNombrePrograma());
                }
                
                System.out.println("✅ ProgramaDAO.listar: Total de programas encontrados: " + contador);

            }

        } catch (SQLException e) {
            System.err.println("❌ ProgramaDAO.listar: Error SQL: " + e.getMessage());
            System.err.println("   - SQL State: " + e.getSQLState());
            System.err.println("   - Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ ProgramaDAO.listar: Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("🔍 ProgramaDAO.listar: Retornando lista con " + lista.size() + " programas");
        return lista;
    }

    public Programa buscarPorId(int idPrograma) {
        String sql = BASE_SELECT + " WHERE id_programas = ?";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrograma);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ ProgramaDAO.buscarPorId: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public int guardar(Programa programa) {
        String sql = "INSERT INTO programas (nombre_programa, nivel_formacion, duracion, jornada_id, modalidad_id, coordinacion_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, programa.getNombrePrograma());
            ps.setString(2, programa.getNivelFormacion());
            ps.setString(3, programa.getDuracion());
            ps.setInt(4, programa.getJornadaId());
            ps.setInt(5, programa.getModalidadId());
            ps.setInt(6, programa.getCoordinacionId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ ProgramaDAO.guardar: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public boolean actualizar(Programa programa) {
        String sql = "UPDATE programas SET nombre_programa = ?, nivel_formacion = ?, duracion = ?, jornada_id = ?, modalidad_id = ?, coordinacion_id = ? WHERE id_programas = ?";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, programa.getNombrePrograma());
            ps.setString(2, programa.getNivelFormacion());
            ps.setString(3, programa.getDuracion());
            ps.setInt(4, programa.getJornadaId());
            ps.setInt(5, programa.getModalidadId());
            ps.setInt(6, programa.getCoordinacionId());
            ps.setInt(7, programa.getIdProgramas());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ ProgramaDAO.actualizar: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminar(int idPrograma) {
        String sql = "DELETE FROM programas WHERE id_programas = ?";

        try (Connection con = ConnBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrograma);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ ProgramaDAO.eliminar: Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private Programa mapRow(ResultSet rs) throws SQLException {
        Programa p = new Programa();
        p.setIdProgramas(rs.getInt("id_programas"));
        p.setNombrePrograma(rs.getString("nombre_programa"));
        p.setNivelFormacion(rs.getString("nivel_formacion"));
        p.setDuracion(rs.getString("duracion"));
        p.setJornadaId(rs.getInt("jornada_id"));
        p.setModalidadId(rs.getInt("modalidad_id"));
        p.setCoordinacionId(rs.getInt("coordinacion_id"));
        return p;
    }
}
