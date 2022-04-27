package com.jigpud.snow.util.format;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author : jigpud
 */
public class DateFormatter {
    private static final SimpleDateFormat YEAR_MONTH_DAY_HOUR_MINUTE = new SimpleDateFormat("yyyy.M.d HH:mm", Locale.SIMPLIFIED_CHINESE);
    private static final SimpleDateFormat MONTH_DAY_HOUR_MINUTE = new SimpleDateFormat("M.d HH:mm", Locale.SIMPLIFIED_CHINESE);

    public static String yearMonthDayHourMinute(long timestamp) {
        return YEAR_MONTH_DAY_HOUR_MINUTE.format(new Date(timestamp));
    }

    public static String monthDayHourMinute(long timestamp) {
        return MONTH_DAY_HOUR_MINUTE.format(new Date(timestamp));
    }

    public static int getYear(long timestamp) {
        Calendar time = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
        return time.get(Calendar.YEAR);
    }
}
