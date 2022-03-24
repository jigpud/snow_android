package com.jigpud.snow.util.json;

import com.google.gson.Gson;

/**
 * @author jigpud
 */
public class JsonUtil {
    private static final Gson GSON = new Gson();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
