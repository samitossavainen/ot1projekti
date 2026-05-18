package com.mokkikodit.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    // SQLite DATE-muoto: YYYY-MM-DD
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    // SQLite DATETIME-muoto: YYYY-MM-DD HH:MM:SS
    private static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    // Muuntaa tietokannasta tulevan arvon LocalDate-muotoon
    public static LocalDate parseDate(Object dbValue) {
        if (dbValue == null) return null;

        String value = dbValue.toString();

        try {
            // Jos arvo sisältää myös ajan, otetaan vain päivämääräosa
            if (value.length() > 10) {
                return LocalDate.parse(value.substring(0, 10), DATE_FORMAT);
            }

            // Pelkkä päivämäärä voidaan parsia suoraan
            return LocalDate.parse(value, DATE_FORMAT);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Virheellinen päivämääräformaatti: " + value, e
            );
        }
    }


    // Muuntaa tietokannasta tulevan arvon LocalDateTime-muotoon
    public static LocalDateTime parseDateTime(Object dbValue) {
        if (dbValue == null) return null;

        String value = dbValue.toString();

        try {
            // Täysi datetime-muoto (yyyy-MM-dd HH:mm:ss)
            if (value.length() > 10) {
                return LocalDateTime.parse(
                        value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                );
            }

            // Jos vain päivämäärä, asetetaan aika 00:00
            return LocalDate.parse(value).atStartOfDay();

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Virheellinen datetime-formaatti: " + value, e
            );
        }
    }

    // Utility-luokka: estetään olion luonti
    private DateUtil() {
    }
}