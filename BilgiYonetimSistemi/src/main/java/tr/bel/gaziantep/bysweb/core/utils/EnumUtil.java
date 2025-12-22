package tr.bel.gaziantep.bysweb.core.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
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

    public static List<Class<?>> findEnumsInPackage(String packageName) throws Exception {
        List<Class<?>> enumClasses = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');

        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());

            if (directory.exists()) {
                findEnumsInDirectory(packageName, directory, enumClasses);
            }
        }

        return enumClasses;
    }

    public static void findEnumsInDirectory(String packageName, File directory, List<Class<?>> enumClasses) throws Exception {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findEnumsInDirectory(packageName + "." + file.getName(),
                            file, enumClasses);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' +
                            file.getName().substring(0, file.getName().length() - 6);

                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isEnum()) {
                            enumClasses.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        // Sınıf bulunamazsa geç
                    }
                }
            }
        }
    }
}
