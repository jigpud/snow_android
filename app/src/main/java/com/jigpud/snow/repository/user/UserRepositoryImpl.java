package com.jigpud.snow.repository.user;

import android.annotation.SuppressLint;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.bean.*;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.database.entity.TokenEntity;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.http.UserService;
import com.jigpud.snow.util.user.CurrentUser;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jigpud
 */
public class UserRepositoryImpl implements UserRepository {
    private static final String TAG = "UserRepository";
    private static volatile UserRepository instance;

    private final UserService userService;
    private final TokenDao tokenDao;
    private final UserDao userDao;

    private UserRepositoryImpl(UserService userService, TokenDao tokenDao, UserDao userDao) {
        this.userService = userService;
        this.tokenDao = tokenDao;
        this.userDao = userDao;
    }

    @Override
    public Observable<Pair<Boolean, String>> loginByPassword(String username, String password) {
        return userService.loginByPassword(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(response -> handleLoginResponse(username, response))
                .concatMap(this::handleLoginStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> loginByVerificationCode(String username, String verificationCode) {
        return userService.loginByVerificationCode(username, verificationCode)
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map(response -> handleLoginResponse(username, response))
                .concatMap(this::handleLoginStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> register(String username, String password, String verificationCode) {
        return userService.register(username, password, verificationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> retrievePassword(String username, String password, String verificationCode) {
        return userService.retrievePassword(username, password, verificationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleResponseStatus);
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<UserEntity> getUserInfo(String userid) {
        userService.getUserInfo(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response.isSuccess()) {
                        UserInformationResponse info = response.getData();
                        UserEntity user = mapUserInfoToUserEntity(info);
                        userDao.insert(user);
                    }
                });
        return userDao.getUserLiveDataByUserid(userid);
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<UserEntity> getSelfInfo() {
        String currentUsername = CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUsername();
        userService.getSelfInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                   if (response.isSuccess()) {
                       SelfInformationResponse info = response.getData();
                       UserEntity user = mapSelfInfoToUserEntity(info);
                       userDao.insert(user);
                   }
                });
        return userDao.getUserLiveDataByUsername(currentUsername);
    }

    private Pair<Boolean, String> handleResponseStatus(ApiResponseStatus responseStatus) {
        if (responseStatus.isSuccess()) {
            return new Pair<>(true, responseStatus.getMessage());
        } else {
            return new Pair<>(false, responseStatus.getMessage());
        }
    }

    private Observable<Pair<Boolean, String>> handleLoginStatus(Pair<Boolean, String> loginStatus) {
        if (loginStatus.first) {
            return userService.getSelfInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(response -> {
                        if (response.isSuccess() && response.getData() != null) {
                            SelfInformationResponse info = response.getData();
                            UserEntity user = mapSelfInfoToUserEntity(info);
                            userDao.insert(user);
                            CurrentUser.getInstance(SnowApplication.getAppContext()).login(user.getUsername());
                        }
                        return loginStatus;
                    });
        } else {
            return Observable.just(loginStatus);
        }
    }

    private Pair<Boolean, String> handleLoginResponse(String username, ApiResponse<LoginResponse> response) {
        Pair<Boolean, String> loginStatus;
        if (response.isSuccess() && response.getData() != null) {
            LoginResponse loginResponse = response.getData();
            String token = loginResponse.getToken();
            String refreshToken = loginResponse.getRefreshToken();
            saveTokenAndRefreshToken(username, token, refreshToken);

            CurrentUser currentUser = CurrentUser.getInstance(SnowApplication.getAppContext());
            currentUser.setToken(token);
            currentUser.setRefreshToken(refreshToken);

            loginStatus = new Pair<>(true, response.getMessage());
        } else {
            loginStatus = new Pair<>(false, response.getMessage());
        }
        return loginStatus;
    }

    private void saveTokenAndRefreshToken(String username, String token, String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(username);
        tokenEntity.setToken(token);
        tokenEntity.setRefreshToken(refreshToken);
        tokenDao.insert(tokenEntity);
    }

    private UserEntity mapSelfInfoToUserEntity(SelfInformationResponse selfInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(selfInfo.getUsername());
        userEntity.setUserid(selfInfo.getUserid());
        userEntity.setGender(selfInfo.getGender());
        userEntity.setAge(selfInfo.getAge());
        userEntity.setNickname(selfInfo.getNickname());
        userEntity.setSignature(selfInfo.getSignature());
        userEntity.setLikes(selfInfo.getLikes());
        userEntity.setFollowers(selfInfo.getFollowers());
        userEntity.setFollowed(selfInfo.getFollowed());
        return userEntity;
    }

    private UserEntity mapUserInfoToUserEntity(UserInformationResponse userInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserid(userInfo.getUserid());
        userEntity.setGender(userInfo.getGender());
        userEntity.setAge(userInfo.getAge());
        userEntity.setNickname(userInfo.getNickname());
        userEntity.setSignature(userInfo.getSignature());
        userEntity.setLikes(userInfo.getLikes());
        userEntity.setFollowers(userInfo.getFollowers());
        userEntity.setFollowed(userInfo.getFollowed());
        return userEntity;
    }

    public static UserRepository getInstance(UserService userService, TokenDao tokenDao, UserDao userDao) {
        if (instance == null) {
            synchronized (UserRepositoryImpl.class) {
                if (instance == null) {
                    instance = new UserRepositoryImpl(userService, tokenDao, userDao);
                }
            }
        }
        return instance;
    }
}
