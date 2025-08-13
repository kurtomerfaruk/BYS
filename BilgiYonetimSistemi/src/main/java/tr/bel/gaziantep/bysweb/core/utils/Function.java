package tr.bel.gaziantep.bysweb.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.file.UploadedFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:50
 */
public class Function {

    public static String pkFieldType(Class clazz) {

        for (Field field : clazz.getDeclaredFields()) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.toString().contains("jakarta.persistence.Id")) {
                    return field.getType().toString();
                }
            }
        }
        return null;
    }

    public static <T> Object pkFieldValue(T selected) {
        Class clazz = selected.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.toString().contains("jakarta.persistence.Id")) {
                    try {
                        Object result = field.get(selected);
                        field.setAccessible(false);
                        return result;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            field.setAccessible(false);
        }
        return null;
    }

    public static String encrypt(String metin) {
        try {
            MessageDigest messageDigestNesnesi = MessageDigest.getInstance("MD5");
            messageDigestNesnesi.update(metin.getBytes());
            byte[] messageDigestDizisi = messageDigestNesnesi.digest();
            StringBuilder sb32 = new StringBuilder();
            for (int i = 0; i < messageDigestDizisi.length; i++) {
                sb32.append(Integer.toString((messageDigestDizisi[i] & 0xff) + 0x100, 32));
            }
            return sb32.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public static String phoneValidate(String phone) {
        if (!StringUtil.isBlank(phone)) {
            phone = phone.replace("(", "").replace(")", "");
            if (phone.startsWith("0")) {
                phone = phone.substring(1);
            }

            if (phone.length() == 10) {
                phone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "($1) $2 $3 $4");

            }
        }
        return phone;
    }

    public static String getExtension(UploadedFile file){
        String result = "";

        String fileName = file.getFileName();
        result = fileName.substring(fileName.lastIndexOf("."));
        return result;
    }

    public static boolean isImage(String fileName) {
        if (StringUtil.isNotBlank(fileName)) {
            String extension = FilenameUtils.getExtension(fileName);
            List<String> lists = Arrays.asList("png", "jpg", "jpeg", "bmp");
            return lists.contains(extension);
        }
        return false;
    }

    public static boolean isWindows() {
        return !System.getProperty("os.name").equals("Mac OS X");
    }

    public static <T, R> List<R> filterAndCollect(List<T> sourceList, java.util.function.Predicate<T> filter, java.util.function.Function<T, R> mapper) {
        return sourceList.stream()
                .filter(filter)
                .map(mapper)
                .collect(Collectors.toList());
    }

    public static boolean isPasswordStrong(String password) {
        if (password == null) return false;
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$";
        return Pattern.matches(pattern, password);
    }
}
