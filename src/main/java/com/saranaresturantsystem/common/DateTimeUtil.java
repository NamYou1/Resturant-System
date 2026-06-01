package com.saranaresturantsystem.common;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final String DEFAULT_PATTERN =
            "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }
}