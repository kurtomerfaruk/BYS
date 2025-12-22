package tr.bel.gaziantep.bysweb.core.service;

import jakarta.ejb.Stateless;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.12.2025 09:54
 */
@Stateless
public class EnumService {

    public List<Map<String, Object>> getEnumValues(String enumClassName) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            Class<?> enumClass = Class.forName(enumClassName);

            if (enumClass.isEnum()) {
                Object[] enumConstants = enumClass.getEnumConstants();

                for (Object enumConstant : enumConstants) {
                    Map<String, Object> enumMap = new HashMap<>();

                    // Enum'un adını al
                    enumMap.put("name", ((Enum<?>) enumConstant).name());

                    // Getter metodlarını çağır
//                    try {
//                        Method getKodMethod = enumClass.getMethod("getLabel");
//                        enumMap.put("kod", getKodMethod.invoke(enumConstant));
//                    } catch (NoSuchMethodException e) {
//                        // getKod yoksa name'i kullan
//                        enumMap.put("kod", ((Enum<?>) enumConstant).name());
//                    }

                    enumMap.put("kod", ((Enum<?>) enumConstant).name());

                    try {
                        Method getAciklamaMethod = enumClass.getMethod("getLabel");
                        enumMap.put("aciklama", getAciklamaMethod.invoke(enumConstant));
                    } catch (NoSuchMethodException e) {
                        enumMap.put("aciklama", ((Enum<?>) enumConstant).name());
                    }

                    result.add(enumMap);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Enum değerleri getirilirken hata: " + e.getMessage(), e);
        }

        return result;
    }

    public Object convertToEnum(String enumClassName, String value) {
        try {
            Class<?> enumClass = Class.forName(enumClassName);

            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            // Enum.valueOf ile dene
            try {
                return Enum.valueOf((Class<Enum>) enumClass, value);
            } catch (IllegalArgumentException e) {
                // Kod'a göre bulmayı dene
                Object[] enumConstants = enumClass.getEnumConstants();
                for (Object enumConstant : enumConstants) {
                    Method getKodMethod = enumClass.getMethod("getKod");
                    String kod = (String) getKodMethod.invoke(enumConstant);

                    if (value.equals(kod)) {
                        return enumConstant;
                    }
                }

                throw new RuntimeException("Enum değeri bulunamadı: " + value);
            }

        } catch (Exception e) {
            throw new RuntimeException("Enum conversion hatası: " + e.getMessage(), e);
        }
    }
}
