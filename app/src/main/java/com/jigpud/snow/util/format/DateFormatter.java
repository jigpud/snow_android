package com.jigpud.snow.util.format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author : jigpud
 */
public class DateFormatter {
    private static final SimpleDateFormat yyyy_M_d_hh_mm = new SimpleDateFormat("yyyy.M.d HH:mm", Locale.SIMPLIFIED_CHINESE);

    public static String yearMonthDayHourMinute(long timeStamp) {
        return yyyy_M_d_hh_mm.format(new Date(timeStamp));
    }
}
