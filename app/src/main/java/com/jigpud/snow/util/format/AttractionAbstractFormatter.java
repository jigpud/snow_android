package com.jigpud.snow.util.format;

import java.util.Locale;

/**
 * @author : jigpud
 */
public class AttractionAbstractFormatter {
    public static String getRecommendAttractionAbstract(float score, long storyCount) {
        return String.format(Locale.CHINA, "%.1f", score) +
                "（" +
                IntegerFormatter.formatWithUnit(storyCount) +
                "游记）";
    }
}
