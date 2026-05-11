package com.mokkikodit.util;

import java.time.LocalDate;

public class DateUtil {

    public static LocalDate parseDate(Object dbValue) {
        if (dbValue == null) return null;

        return LocalDate.parse(dbValue.toString());
    }
}