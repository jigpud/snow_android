package com.jigpud.snow.repository.img;

import com.jigpud.snow.bean.UploadTokenResponse;
import com.jigpud.snow.http.QiniuService;
import com.jigpud.snow.util.img.ImageUploader;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : jigpud
 */
public class ImageRepositoryImpl implements ImageRepository {
    private static volatile ImageRepositoryImpl instance;

    private final QiniuService qiniuService;

    private ImageRepositoryImpl(QiniuService qiniuService) {
        this.qiniuService = qiniuService;
    }

    @Override
    public Observable<String> uploadStoryImage(String path) {
        return qiniuService.getStoryImgUploadToken()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(apiResponse -> {
                    if (apiResponse.isSuccess()) {
                        UploadTokenResponse uploadTokenResponse = apiResponse.getData();
                        return uploadImage(path, uploadTokenResponse.getUploadToken(), uploadTokenResponse.getKey());
                    } else {
                        return Observable.just("");
                    }
                });
    }

    @Override
    public Observable<String> uploadAvatarImage(String path) {
        return qiniuService.getAvatarUploadToken()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(apiResponse -> {
                    if (apiResponse.isSuccess()) {
                        UploadTokenResponse uploadTokenResponse = apiResponse.getData();
                        return uploadImage(path, uploadTokenResponse.getUploadToken(), uploadTokenResponse.getKey());
                    } else {
                        return Observable.just("");
                    }
                });
    }

    @Override
    public Observable<String> uploadAttractionImage(String path) {
        return qiniuService.getAttractionImgUploadToken()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(apiResponse -> {
                    if (apiResponse.isSuccess()) {
                        UploadTokenResponse uploadTokenResponse = apiResponse.getData();
                        return uploadImage(path, uploadTokenResponse.getUploadToken(), uploadTokenResponse.getKey());
                    } else {
                        return Observable.just("");
                    }
                });
    }

    @Override
    public Observable<String> uploadUserProfileBackgroundImage(String path) {
        return qiniuService.getUserProfileBackgroundUploadToken()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(apiResponse -> {
                    if (apiResponse.isSuccess()) {
                        UploadTokenResponse uploadTokenResponse = apiResponse.getData();
                        return uploadImage(path, uploadTokenResponse.getUploadToken(), uploadTokenResponse.getKey());
                    } else {
                        return Observable.just("");
                    }
                });
    }

    private Observable<String> uploadImage(String path, String uploadToken, String key) {
        return ImageUploader.getInstance().upload(path, uploadToken, key);
    }

    public static ImageRepositoryImpl getInstance(QiniuService qiniuService) {
        if (instance == null) {
            synchronized (ImageRepositoryImpl.class) {
                if (instance == null) {
                    instance = new ImageRepositoryImpl(qiniuService);
                }
            }
        }
        return instance;
    }
}
