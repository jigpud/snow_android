package com.jigpud.snow.util.format;

/**
 * @author : jigpud
 */
public class AttractionFormatter {
    public static String getAbstract(float score, long storyCount) {
        return formatScore(score) + formatStoryCount(storyCount);
    }

    public static String formatScore(float score) {
        return FloatFormatter.formatWithDotOne(score);
    }

    public static String formatStoryCount(long storyCount) {
        return "（" +
                IntegerFormatter.formatWithUnit(storyCount) +
                "篇游记）";
    }

    public static String formatScoreCount(long scoreCount) {
        return "已有" +
                IntegerFormatter.formatWithUnit(scoreCount) +
                "人参与评分";
    }

    public static String formatPhotoCount(int photoCount) {
        return "有" +
                IntegerFormatter.formatWithUnit(photoCount) +
                "张照片";
    }

    public static String formatFollowerCount(long followers) {
        return "关注：" +
                IntegerFormatter.formatWithUnit(followers);
    }

    public static String formatStoryCountBefore(long storyCount) {
        return "游记：" +
                IntegerFormatter.formatWithUnit(storyCount);
    }
}
