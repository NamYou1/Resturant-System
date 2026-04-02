package com.saranaresturantsystem.Common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    // ── Formatters ────────────────────────────────────────────
    public static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ── Format: LocalDateTime → String ────────────────────────
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_TIME_FORMAT);
    }

    // ── Parse: String → LocalDateTime ────────────────────────
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) return null;
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMAT);
    }

    // ── Now ──────────
    public static String nowFormatted() {
        return LocalDateTime.now().format(DATE_TIME_FORMAT);
    }
}