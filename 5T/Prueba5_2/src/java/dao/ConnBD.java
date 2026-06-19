package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnBD {

    private static final String URL = "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = ""; // SIN CONTRASEÑA

    public static Connection conectar() {
        Connection conn = null;

        try {
            // *** Cargar el driver de MySQL (OBLIGATORIO en GlassFish) ***
            // Usar el driver moderno de MySQL Connector/J
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Intentar la conexión
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✔️ ConnBD: Conexión establecida correctamente");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ ERROR: No se encontró el driver MySQL.");
            System.out.println("➡ Asegúrate de copiar 'mysql-connector-java-8.x.x.jar' o superior en:");
            System.out.println("   glassfish/domains/domain1/lib/");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("❌ ConnBD: ERROR DE CONEXIÓN A LA BD");
            e.printStackTrace();
        }

        return conn;
    }
}
