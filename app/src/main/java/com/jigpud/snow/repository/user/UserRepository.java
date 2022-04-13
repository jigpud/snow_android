package com.jigpud.snow.repository.user;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.database.entity.UserEntity;
import io.reactivex.Observable;

/**
 * @author jigpud
 */
public interface UserRepository {
    Observable<Pair<Boolean, String>> loginByPassword(String username, String password);

    Observable<Pair<Boolean, String>> loginByVerificationCode(String username, String verificationCode);

    Observable<Pair<Boolean, String>> register(String username, String password, String verificationCode);

    Observable<Pair<Boolean, String>> retrievePassword(String username, String password, String verificationCode);

    LiveData<UserEntity> getUserInfo(String userid);

    LiveData<UserEntity> getSelfInfo();

    Observable<Pair<Boolean, String>> follow(String userid);

    Observable<Pair<Boolean, String>> unfollow(String userid);
}
