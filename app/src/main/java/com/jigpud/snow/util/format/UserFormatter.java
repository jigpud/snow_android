package com.jigpud.snow.util.format;

/**
 * @author : jigpud
 */
public class UserFormatter {
    public static String getAbstract(long storyCount, long likes) {
        return "已发布" +
                IntegerFormatter.formatWithUnit(storyCount) +
                "篇游记，获得" +
                IntegerFormatter.formatWithUnit(likes) +
                "个点赞";
    }
}
