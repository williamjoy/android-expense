package org.williamjoy.gexpense.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateHelper {
    public static String getDisplayDate() {
        Calendar calendar = Calendar.getInstance();
        String date = formatter.format(calendar.getTime());
        return date;
    }

    public static String getDisplayDate(Calendar calendar) {
        String date = formatter.format(calendar.getTime());
        return date;
    }

    public static SimpleDateFormat getDateFormat() {
        return formatter;
    }

    public static Calendar setDate(Calendar c, int year, int monthOfYear,
            int dayOfMonth) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return c;
    }

    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "MM/dd/yyyy", Locale.getDefault());

}
