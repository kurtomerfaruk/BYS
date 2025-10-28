package tr.bel.gaziantep.bysweb.core.utils;

import java.io.Serial;
import java.util.Random;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 11:10
 */
public class CaptchaUtil implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -2362937583674526895L;

    public CaptchaUtil() {
        // TODO document why this constructor is empty
    }

    public static String generateCaptchaText(int length) {
        String chars = "ABCDEFGHJKMNOPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }
}
