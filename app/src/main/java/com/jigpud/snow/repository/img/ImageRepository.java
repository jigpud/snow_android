package com.jigpud.snow.repository.img;

import io.reactivex.Observable;

/**
 * @author : jigpud
 */
public interface ImageRepository {
    Observable<String> uploadAvatarImage(String path);

    Observable<String> uploadStoryImage(String path);

    Observable<String> uploadAttractionImage(String path);

    Observable<String> uploadUserProfileBackgroundImage(String path);
}
