package com.jigpud.snow.util.pixel;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.jigpud.snow.SnowApplication;

public class PixelUtil {
    public static float dpToPixel(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics());
    }

    public static float spToPixel(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getDisplayMetrics());
    }

    private static DisplayMetrics getDisplayMetrics() {
        return SnowApplication.getAppContext().getResources().getDisplayMetrics();
    }
}
