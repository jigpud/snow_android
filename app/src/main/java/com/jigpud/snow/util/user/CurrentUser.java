package com.jigpud.snow.util.user;

import android.content.Context;
import android.content.SharedPreferences;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.util.logger.Logger;

/**
 * @author jigpud
 */
public class CurrentUser {
    private static final String TAG = "CurrentUser";
    private static final String KEY_CURRENT_USER = "currentUser";
    private static volatile CurrentUser instance;

    private volatile String token;
    private volatile String refreshToken;
    private final SharedPreferences currentUserSP;
    private final TokenDao tokenDao;

    private CurrentUser(Context context, TokenDao tokenDao) {
        this.currentUserSP = context.getSharedPreferences(KEY_CURRENT_USER, Context.MODE_PRIVATE);
        this.tokenDao = tokenDao;
    }

    public void login(String username) {
        if (!getCurrentUsername().equals(username)) {
            logout();
            setCurrentUsername(username);
        }
    }

    public boolean isLogin() {
        return !getCurrentUsername().isEmpty();
    }

    public void tryAutoLogin() {
        if (!isLogin()) {
            Logger.d(TAG, "tryAutoLogin: not login!");
        } else {
            Logger.d(TAG, "tryAutoLogin: already login, current user is %s!", getCurrentUsername());
        }
    }

    public String getCurrentUsername() {
        return currentUserSP.getString(KEY_CURRENT_USER, "");
    }

    public void setCurrentUsername(String currentUsername) {
        currentUserSP.edit()
                .putString(KEY_CURRENT_USER, currentUsername)
                .apply();
    }

    public void logout() {
        setCurrentUsername("");
        token = "";
        refreshToken = "";
    }

    public String getToken() {
        if (token == null || token.isEmpty()) {
            token = tokenDao.getToken(getCurrentUsername());
        }
        return token;
    }

    public void updateToken(String newToken) {
        token = newToken;
        tokenDao.updateToken(getCurrentUsername(), token);
    }

    public String getRefreshToken() {
        if (refreshToken == null || refreshToken.isEmpty()) {
            refreshToken = tokenDao.getRefreshToken(getCurrentUsername());
        }
        return refreshToken;
    }

    public static CurrentUser getInstance(Context context) {
        if (instance == null) {
            synchronized (CurrentUser.class) {
                if (instance == null) {
                    Context applicationContext = context.getApplicationContext();
                    TokenDao tokenDao = SnowDatabase.getSnowDatabase(applicationContext).tokenDao();
                    instance = new CurrentUser(applicationContext, tokenDao);
                }
            }
        }
        return instance;
    }
}
