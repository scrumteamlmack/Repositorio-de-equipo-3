package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

<<<<<<< HEAD
=======
/**
 * Utilidad centralizada para el manejo de contraseñas:
 *  - genera hashes con SHA-256
 *  - compara contraseñas planas contra el hash almacenado.
 *
 * Se mantiene libre de dependencias externas para facilitar las pruebas.
 */
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String plain) {
        if (plain == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No fue posible inicializar SHA-256", e);
        }
    }

    public static boolean matches(String plain, String hashed) {
        if (plain == null || hashed == null) {
            return false;
        }
        return hash(plain).equals(hashed);
    }
}

