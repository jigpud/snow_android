package com.jigpud.snow.util.img;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import io.reactivex.Observable;

import java.io.File;

/**
 * @author : jigpud
 */
public class ImageUploader {
    private static volatile ImageUploader instance;

    private final UploadManager uploadManager;

    private ImageUploader() {
        Configuration config = new Configuration.Builder()
                .connectTimeout(30)
                .useHttps(true)
                .useConcurrentResumeUpload(true)
                .concurrentTaskCount(3)
                .responseTimeout(30)
                .build();
        uploadManager = new UploadManager(config);
    }

    public Observable<String> upload(String path, String uploadToken, String key) {
        return Observable.create(emitter ->
                uploadManager.put(path, key, uploadToken, (uploadKey, info, response) -> {
                    if (info.isOK()) {
                        String url = response.optString("url", "");
                        emitter.onNext(url);
                    } else {
                        emitter.onError(new RuntimeException(info.error));
                    }
                    emitter.onComplete();
                }, null)
        );
    }

    public Observable<String> upload(File file, String uploadToken, String key) {
        return Observable.create(emitter ->
                uploadManager.put(file, key, uploadToken, (key1, info, response) -> {
                    if (info.isOK()) {
                        String url = response.optString("url", "");
                        emitter.onNext(url);
                    } else {
                        emitter.onError(new RuntimeException(info.error));
                    }
                    emitter.onComplete();
                }, null));
    }

    public static ImageUploader getInstance() {
        if (instance == null) {
            synchronized (ImageUploader.class) {
                if (instance == null) {
                    instance = new ImageUploader();
                }
            }
        }
        return instance;
    }
}
