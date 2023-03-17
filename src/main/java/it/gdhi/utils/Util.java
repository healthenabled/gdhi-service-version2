package it.gdhi.utils;

import java.time.Year;

public class Util {
    public static String getCurrentYear() {
        int currentYear = Year.now().getValue();
        String year = new String(String.valueOf(currentYear));
        return year;
    }
}
