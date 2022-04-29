package com.jigpud.snow.util.format;

import java.util.Locale;

/**
 * @author : jigpud
 */
public class FloatFormatter {
    public static String formatWithDotOne(Float f) {
        if (f == null) {
            return "0.0";
        } else {
            return String.format(Locale.SIMPLIFIED_CHINESE, "%.1f", f);
        }
    }

    public static String formatWithDotOne(Double d) {
        if (d == null) {
            return "0.0";
        } else {
            return String.format(Locale.SIMPLIFIED_CHINESE, "%.1f", d);
        }
    }
}
