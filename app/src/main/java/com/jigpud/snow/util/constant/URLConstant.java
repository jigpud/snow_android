package com.jigpud.snow.util.constant;

import com.jigpud.snow.BuildConfig;

/**
 * @author : jigpud
 */
public class URLConstant {
    public static final String ONLINE_BASE_URL = "https://snow.jigpud.com:8443";
    public static final String DEBUG_BASE_URL = "http://10.17.237.51:8080";

    public static String getURL() {
        if (BuildConfig.DEBUG) {
            return DEBUG_BASE_URL;
        } else {
            return ONLINE_BASE_URL;
        }
    }
}
