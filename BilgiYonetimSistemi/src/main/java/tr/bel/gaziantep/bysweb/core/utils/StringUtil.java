package tr.bel.gaziantep.bysweb.core.utils;/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 17.06.2025 13:46
 */
public class StringUtil {
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static String removeLastPart(String str) {
        int lastSpaceIndex = str.lastIndexOf(" ");
        if (lastSpaceIndex != -1) {
            return str.substring(0, lastSpaceIndex);
        }
        return str;
    }

    public static String removeBracket(String str){
        return str.replace("[","").replace("]","");
    }

    public static String incrementName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }
        String prefix = name.replaceAll("\\d+$", "");
        String numberPart = name.substring(prefix.length());

        if (numberPart.isEmpty()) {
            return name + "_1";
        }

        int num = Integer.parseInt(numberPart);
        return prefix + (num + 1);
    }

    public static String toCamelCase(String className) {
        if (className == null) return "";

        String[] parts = className.split("\\.");
        String simpleName = parts[parts.length - 1];

        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }
}
