package com.jigpud.snow.util.user;

import android.content.Context;
import android.content.SharedPreferences;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.util.logger.Logger;

/**
 * @author : jigpud
 */
public class CurrentUser {
    private static final String TAG = "CurrentUser";
    private static final String KEY_CURRENT_USER = "currentUser";
    private static volatile CurrentUser instance;

    private volatile String token;
    private volatile String refreshToken;
    private volatile String currentUsername;
    private volatile String currentUserid;
    private final SharedPreferences currentUserSP;
    private final TokenDao tokenDao;
    private final UserDao userDao;

    private CurrentUser(Context context, TokenDao tokenDao, UserDao userDao) {
        this.currentUserSP = context.getSharedPreferences(KEY_CURRENT_USER, Context.MODE_PRIVATE);
        this.tokenDao = tokenDao;
        this.userDao = userDao;
    }

    public void login(String username) {
        Logger.d(TAG, "login %s", username);
        // logout
        logout();

        // set current username
        setCurrentUsername(username);

        // set current userid
        UserEntity currentUser = userDao.getUserByUsername(username);
        Logger.d(TAG, "currentUser: %s", currentUser);
        if (currentUser != null) {
            currentUserid = currentUser.getUserid();
        }

        // set token
        if (token == null || token.isEmpty()) {
            token = tokenDao.getToken(username);
        }

        // set refresh token
        if (refreshToken == null || refreshToken.isEmpty()) {
            refreshToken = tokenDao.getRefreshToken(username);
        }

        Logger.d(TAG, "login status:\n" +
                "currentUsername: %s\n" +
                "currentUserid: %s\n" +
                "token: %s\n" +
                "refreshToken: %s", currentUsername, currentUserid, token, refreshToken);
    }

    public boolean isLogin() {
        return currentUsername != null && !currentUsername.isEmpty() &&
                currentUserid != null && !currentUserid.isEmpty() &&
                refreshToken != null && !refreshToken.isEmpty();
    }

    public void tryAutoLogin() {
        if (!isLogin()) {
            Logger.d(TAG, "tryAutoLogin: not login!");
            login(currentUserSP.getString(KEY_CURRENT_USER, ""));
        } else {
            Logger.d(TAG, "tryAutoLogin: already login, current user is %s!", getCurrentUsername());
        }
    }

    public String getCurrentUserid() {
        return currentUserid;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
        currentUserSP.edit()
                .putString(KEY_CURRENT_USER, currentUsername)
                .apply();
    }

    public void logout() {
        setCurrentUsername("");
        currentUserid = "";
        token = "";
        refreshToken = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void updateToken(String newToken) {
        token = newToken;
        tokenDao.updateToken(getCurrentUsername(), token);
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static CurrentUser getInstance(Context context) {
        if (instance == null) {
            synchronized (CurrentUser.class) {
                if (instance == null) {
                    Context applicationContext = context.getApplicationContext();
                    SnowDatabase database = SnowDatabase.getSnowDatabase(applicationContext);
                    TokenDao tokenDao = database.tokenDao();
                    UserDao userDao = database.userDao();
                    instance = new CurrentUser(applicationContext, tokenDao, userDao);
                }
            }
        }
        return instance;
    }
}
