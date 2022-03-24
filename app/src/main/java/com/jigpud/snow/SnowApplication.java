package com.jigpud.snow;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author jigpud
 */
public class SnowApplication extends Application {
    private static final String TAG = "SnowApplication";
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize global context
        if (context == null) {
            context = this.getApplicationContext();
        }

        // preload database
        SnowDatabase.getSnowDatabase(getAppContext());

        RxJavaPlugins.setErrorHandler(throwable -> Logger.e(TAG, Log.getStackTraceString(throwable)));

        // auto login
        CurrentUser.getInstance(getAppContext()).tryAutoLogin();
    }
}
