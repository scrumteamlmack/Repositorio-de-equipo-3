package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnBD {

    private static final String URL = "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection conectar() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
<<<<<<< HEAD
            System.out.println("ConnBD: Conexión establecida correctamente");

        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: No se encontró el driver MySQL.");
            System.out.println("Asegúrate de copiar 'mysql-connector-java-5.1.xx.jar' en:");
=======
            System.out.println("✔️ ConnBD: Conexión establecida correctamente");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ ERROR: No se encontró el driver MySQL.");
            System.out.println("➡ Asegúrate de copiar 'mysql-connector-java-5.1.xx.jar' en:");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            System.out.println("   glassfish/domains/domain1/lib/");
            e.printStackTrace();

        } catch (SQLException e) {
<<<<<<< HEAD
            System.out.println("ConnBD: ERROR DE CONEXIÓN A LA BD");
=======
            System.out.println("❌ ConnBD: ERROR DE CONEXIÓN A LA BD");
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
            e.printStackTrace();
        }

        return conn;
    }
}
