package com.jigpud.snow.util.toast;

import android.widget.Toast;
import com.jigpud.snow.SnowApplication;

/**
 * @author jigpud
 */
public class ToastMaker {
    public static void makeToast(String message) {
        Toast.makeText(SnowApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
    }
}
