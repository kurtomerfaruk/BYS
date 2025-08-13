package tr.bel.gaziantep.bysweb.core.utils;



/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:59
 */
public class NumberUtil {
    public static boolean isParsable(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        } else if (str.charAt(str.length() - 1) == '.') {
            return false;
        } else if (str.charAt(0) == '-') {
            return str.length() == 1 ? false : withDecimalsParsing(str, 1);
        } else {
            return withDecimalsParsing(str, 0);
        }
    }

    private static boolean withDecimalsParsing(String str, int beginIdx) {
        int decimalPoints = 0;

        for(int i = beginIdx; i < str.length(); ++i) {
            char ch = str.charAt(i);
            boolean isDecimalPoint = ch == '.';
            if (isDecimalPoint) {
                ++decimalPoints;
            }

            if (decimalPoints > 1) {
                return false;
            }

            if (!isDecimalPoint && !Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }
}
