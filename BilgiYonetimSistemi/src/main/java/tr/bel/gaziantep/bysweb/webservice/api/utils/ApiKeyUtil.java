package tr.bel.gaziantep.bysweb.webservice.api.utils;

import java.security.SecureRandom;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 11:12
 */
public class ApiKeyUtil {

    private static final String CHARSET =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }

        return sb.toString();
    }

    public static String generateAppKey() {
        return generate(20);
    }

    public static String generateAppSecret() {
        return generate(20);
    }
}
