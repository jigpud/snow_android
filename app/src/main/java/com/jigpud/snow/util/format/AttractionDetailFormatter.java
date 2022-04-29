package com.jigpud.snow.util.format;

/**
 * @author : jigpud
 */
public class AttractionDetailFormatter {
    public static String formatScoreCount(Long scoreCount) {
        return "已有" +
                IntegerFormatter.formatWithUnit(scoreCount) +
                "人参与评分";
    }

    public static String formatPhotoCount(Integer photoCount) {
        return "有" +
                IntegerFormatter.formatWithUnit(photoCount) +
                "张照片";
    }
}
