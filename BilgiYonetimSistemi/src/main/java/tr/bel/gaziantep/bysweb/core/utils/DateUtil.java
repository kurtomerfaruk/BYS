package tr.bel.gaziantep.bysweb.core.utils;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:41
 */
public class DateUtil implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -505574464128480476L;

    public static LocalDate stringToLocalDate(String value, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(value, formatter);
    }

    public static LocalDateTime atStartOfDay(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    public static LocalDateTime atEndOfDay(LocalDate localDate) {
        return localDate.atTime(LocalTime.MAX);
    }

    public static String localdateToString(LocalDate localDate, String format) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(format);
        return localDate.format(formatters);
    }

    public static String localdateTimeToString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatters);
    }

    public static LocalDateTime addDay(int value) {
        LocalDateTime date = LocalDateTime.now();
        date = date.plusDays(value);
        return date;
    }

    public static long dateDifferenceDays(LocalDate dateTime) {
        LocalDate today = LocalDate.now();
        if (dateTime != null) {
            return Duration.between(dateTime.atStartOfDay(), today.atStartOfDay()).toDays();
        } else {
            return 0;
        }
    }
}
