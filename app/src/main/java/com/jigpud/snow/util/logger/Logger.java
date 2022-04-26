package com.jigpud.snow.util.logger;

import android.util.Log;
import com.jigpud.snow.BuildConfig;

/**
 * @author : jigpud
 */
public class Logger {
    private static final String TAG = "Snow";

    public static void d(String tag, String message, Object... args) {
        if (message != null && BuildConfig.DEBUG) {
            Log.d(tag, format(message, args));
        }
    }

    public static void e(String tag, String message, Object... args) {
        if (message != null && BuildConfig.DEBUG) {
            Log.e(tag, format(message, args));
        }
    }

    private static String format(String format, Object... args) {
        return String.format(format, args);
    }
}
