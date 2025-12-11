package beans;

import util.PasswordUtil;

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
