package com.jigpud.snow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : jigpud
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodPictureResponse {
    private String foodId;
    private String uploader;
    private String uploaderAvatar;
    private String uploaderNickname;
    private String url;
}
