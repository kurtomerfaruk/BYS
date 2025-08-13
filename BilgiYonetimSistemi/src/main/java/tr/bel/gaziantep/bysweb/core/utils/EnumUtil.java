package tr.bel.gaziantep.bysweb.core.utils;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 14:01
 */
public class EnumUtil {

    public static <T extends Enum<T>> Enum<T> enumValues(Class<T> enumType, String value) {
        for (T c : enumType.getEnumConstants()) {
            if (c.name().equals(value)) {
                return c;
            }
        }
        return null;
    }

    public static <T extends Enum<T>> String enumListToString(List<T> list) {
        String result="";
        if(!list.isEmpty()){
            result = list
                    .stream()
                    .map(Enum::name)
                    .toList()
                    .toString();
        }
        return result;
    }
}
