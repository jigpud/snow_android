package com.jigpud.snow.util.format;

import android.annotation.SuppressLint;

/**
 * @author jigpud
 */
@SuppressLint("DefaultLocale")
public class IntegerFormatter {
    public static String formatWithUnit(int i) {
        if (i >= 10000000) {
            float kw = i / 10000000f;
            return String.format("%.2fkw", kw);
        } else if (i >= 10000) {
            float w = i / 10000f;
            return String.format("%.2fw", w);
        } else {
            return String.format("%d", i);
        }
    }

    public static String formatWithUnit(long l) {
        if (l >= 10000000) {
            float kw = l / 10000000f;
            return String.format("%.2fkw", kw);
        } else if (l >= 10000) {
            float w = l / 10000f;
            return String.format("%.2fw", w);
        } else {
            return String.format("%d", l);
        }
    }

    public static String toString(Integer i) {
        return i.toString();
    }

    public static String toString(Long l) {
        return l.toString();
    }
}
