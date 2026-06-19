package beans;

import util.PasswordUtil;

<<<<<<< HEAD
=======
/**
 * @deprecated Mantenido únicamente para compatibilidad con código existente.
 *             Utilizar {@link PasswordUtil}.
 */
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
@Deprecated
public final class Utils {

    private Utils() {
    }

    public static String encriptar(String plain) {
        return PasswordUtil.hash(plain);
    }

    public static boolean compararConHash(String plain, String hash) {
        return PasswordUtil.matches(plain, hash);
    }
}
