package com.jigpud.snow.util.format;

import android.annotation.SuppressLint;

/**
 * @author : jigpud
 */
@SuppressLint("DefaultLocale")
public class IntegerFormatter {
    public static String formatWithUnit(Integer i) {
        if (i == null) {
            return "0";
        }
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

    public static String formatWithUnit(Long l) {
        if (l == null) {
            return "0";
        }
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
        if (i == null) {
            return "0";
        }
        return i.toString();
    }

    public static String toString(Long l) {
        if (l == null) {
            return "0";
        }
        return l.toString();
    }

    public static String formatFraction(Integer numerator, Integer denominator) {
        return toString(numerator) + "/" + toString(denominator);
    }

    public static String formatFraction(Long numerator, Long denominator) {
        return toString(numerator) + "/" + toString(denominator);
    }
}
