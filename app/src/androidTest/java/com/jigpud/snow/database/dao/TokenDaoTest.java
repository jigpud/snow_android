package com.jigpud.snow.database.dao;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.entity.TokenEntity;
import com.jigpud.snow.util.logger.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author : jigpud
 */
@RunWith(AndroidJUnit4.class)
public class TokenDaoTest {
    private static final String TAG = "TokenDaoTest";

    @Test
    public void test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        TokenDao tokenDao = SnowDatabase.getSnowDatabase(appContext).tokenDao();
        String username = "12345678901";
        String token = "token_12345678901";
        String refreshToken = "refreshToken_12345678901";
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(username);
        tokenEntity.setToken(token);
        tokenEntity.setRefreshToken(refreshToken);
        tokenDao.insert(tokenEntity);
        Logger.d(TAG, tokenDao.getToken(username));
        Logger.d(TAG, tokenDao.getRefreshToken(username));
    }
}
