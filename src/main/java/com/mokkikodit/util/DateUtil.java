package com.mokkikodit.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    // SQLite DATE: YYYY-MM-DD
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    // SQLite DATETIME: YYYY-MM-DD HH:MM:SS
    private static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static LocalDate parseDate(Object dbValue) {
        if (dbValue == null) return null;

        String value = dbValue.toString();

        try {
            // Jos sisältää ajan → otetaan vain päiväosa
            if (value.length() > 10) {
                return LocalDate.parse(value.substring(0, 10), DATE_FORMAT);
            }

            return LocalDate.parse(value, DATE_FORMAT);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Virheellinen päivämääräformaatti: " + value, e
            );
        }
    }


    public static LocalDateTime parseDateTime(Object dbValue) {
        if (dbValue == null) return null;

        String value = dbValue.toString();

        try {
            // Täysi datetime: yyyy-MM-dd HH:mm:ss
            if (value.length() > 10) {
                return LocalDateTime.parse(
                        value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                );
            }

            // Pelkkä päivämäärä: yyyy-MM-dd → kello 00:00
            return LocalDate.parse(value).atStartOfDay();

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Virheellinen datetime-formaatti: " + value, e
            );
        }
    }


    private DateUtil() {
    }
}