package com.jigpud.snow.page.editprofile;

import android.util.Log;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.img.ImageRepository;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class EditProfileViewModel extends BaseViewModel {
    private static final String TAG ="EditProfileViewModel";

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private UserEntity self;

    EditProfileViewModel(UserRepository userRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public LiveData<UserEntity> getSelfInfo() {
        return Transformations.map(userRepository.getSelfInfo(), user -> {
            self = user;
            return user;
        });
    }

    public LiveData<Pair<Boolean, String>> updateInfo(String background, String avatar, String nickname, String signature) {
        MutableLiveData<Pair<Boolean, String>> updateInfoStatusLiveData = new MutableLiveData<>();

        // 如果需要上传背景图片
        Observable<String> uploadBackground = Observable.just(background)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(newBackground -> {
                    if (self != null && self.getBackground().equals(newBackground)) {
                        return Observable.just(newBackground);
                    } else {
                        return imageRepository.uploadUserProfileBackgroundImage(newBackground);
                    }
                });

        // 如果需要上传头像
        Observable<String> uploadAvatar = Observable.just(avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(newAvatar -> {
                    if (self != null && self.getAvatar().equals(newAvatar)) {
                        return Observable.just(newAvatar);
                    } else {
                        return imageRepository.uploadAvatarImage(avatar);
                    }
                });

        // 图片上传完成后更新用户信息
        Disposable disposable = Observable.zip(uploadBackground, uploadAvatar, (backgroundUrl, avatarUrl) -> {
            List<String> urlList = new ArrayList<>();
            urlList.add(backgroundUrl);
            urlList.add(avatarUrl);
            return urlList;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(urlList -> {
                    String backgroundUrl = urlList.get(0);
                    String avatarUrl = urlList.get(1);
                    return userRepository.updateInfo(backgroundUrl, avatarUrl, nickname, signature);
                })
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    updateInfoStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(updateInfoStatusLiveData::postValue);
        lifecycle(disposable);
        return updateInfoStatusLiveData;
    }
}
