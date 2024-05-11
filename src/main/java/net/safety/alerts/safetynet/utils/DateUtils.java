package net.safety.alerts.safetynet.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
    public static LocalDate convertStrDateToDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        return LocalDate.parse(strDate, formatter);
    }

    public static int getDatesYearsDiff(LocalDate a, LocalDate b) {
        Period diff = Period.between(a, b);
        return diff.getYears();
    }

    public static int getDateYearsDiff(LocalDate a) {
        return getDatesYearsDiff(a, LocalDate.now());
    }
}
