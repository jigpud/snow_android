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
import com.jigpud.snow.repository.base.BaseRepository;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : jigpud
 */
public class UserRepositoryImpl extends BaseRepository implements UserRepository {
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
        return withIO(userService.loginByPassword(username, password))
                .map(response -> handleLoginResponse(username, response))
                .concatMap(this::handleLoginStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> loginByVerificationCode(String username, String verificationCode) {
        return withIO(userService.loginByVerificationCode(username, verificationCode))
                .map(response -> handleLoginResponse(username, response))
                .concatMap(this::handleLoginStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> register(String username, String password, String verificationCode) {
        return withIO(userService.register(username, password, verificationCode))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> retrievePassword(String username, String password, String verificationCode) {
        return withIO(userService.retrievePassword(username, password, verificationCode))
                .map(super::handleResponseStatus);
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<UserEntity> getUserInfo(String userid) {
        withIO(userService.getUserInfo(userid))
                .subscribe(this::handleUserInformationResponse);
        return userDao.getUserLiveDataByUserid(userid);
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<UserEntity> getSelfInfo() {
        String currentUserid = CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUserid();
        withIO(userService.getSelfInfo())
                .subscribe(this::handleSelfInformationResponse);
        return userDao.getUserLiveDataByUserid(currentUserid);
    }

    @Override
    public Observable<Pair<Boolean, String>> updateInfo(String background, String avatar, String nickname, String signature) {
        String currentUserid = CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUserid();
        UserEntity self = userDao.getUserByUserid(currentUserid);
        UpdateUserInformationRequest info = new UpdateUserInformationRequest();
        info.setAge(self.getAge());
        info.setGender(self.getGender());
        info.setBackground(background);
        info.setAvatar(avatar);
        info.setNickname(nickname);
        info.setSignature(signature);
        return withIO(userService.updateInfo(info))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> follow(String userid) {
        return withIO(userService.follow(userid))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> unfollow(String userid) {
        return withIO(userService.unfollow(userid))
                .map(super::handleResponseStatus);
    }

    private void handleUserInformationResponse(ApiResponse<UserInformationResponse> userInformationResponse) {
        if (userInformationResponse.isSuccess()) {
            UserEntity user = UserEntity.create(userInformationResponse.getData());
            CurrentUser currentUser = CurrentUser.getInstance(SnowApplication.getAppContext());
            if (currentUser.getCurrentUserid().equals(user.getUserid())) {
                // 对于当前登录的用户需要增加用户名
                user.setUsername(currentUser.getCurrentUsername());
            }
            Logger.d(TAG, "insert user info into db: %s", user);
            userDao.insert(user);
        }
    }

    private void handleSelfInformationResponse(ApiResponse<SelfInformationResponse> selfInformationResponse) {
        if (selfInformationResponse.isSuccess()) {
            UserEntity user = UserEntity.create(selfInformationResponse.getData());
            userDao.insert(user);
        }
    }

    private Observable<Pair<Boolean, String>> handleLoginStatus(Pair<Boolean, String> loginStatus) {
        if (loginStatus.first) {
            return userService.getSelfInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(response -> {
                        if (response.isSuccess()) {
                            SelfInformationResponse info = response.getData();
                            UserEntity user = UserEntity.create(info);
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
