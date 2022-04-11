package com.jigpud.snow.database;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.security.crypto.MasterKey;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.jigpud.snow.database.converter.ListTypeConverter;
import com.jigpud.snow.database.dao.SearchHistoryDao;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.database.entity.TokenEntity;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.util.logger.Logger;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory;

/**
 * @author jigpud
 */
@Database(entities = {
        UserEntity.class,
        TokenEntity.class,
        StoryEntity.class,
        SearchHistoryEntity.class
}, version = 1, exportSchema = false)
@TypeConverters({
        ListTypeConverter.class
})
public abstract class SnowDatabase extends RoomDatabase {
    private static final String TAG = "SnowDatabase";
    private static volatile SnowDatabase instance;

    public abstract UserDao userDao();
    public abstract TokenDao tokenDao();
    public abstract StoryDao storyDao();
    public abstract SearchHistoryDao searchHistoryDao();

    public static SnowDatabase getSnowDatabase(Context context) {
        if (instance == null) {
            synchronized (SnowDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), SnowDatabase.class, "snow")
                            .openHelperFactory(getSQLiteOpenHelperFactory(context.getApplicationContext()))
                            .build();
                }
            }
        }
        return instance;
    }

    /**
     * 使用WCDB
     * @return factory
     */
    private static SupportSQLiteOpenHelper.Factory getSQLiteOpenHelperFactory(Context context) {
        return new WCDBOpenHelperFactory()
                .passphrase(getSecret(context.getApplicationContext()))
                .cipherSpec(getCipherSpec())
                .writeAheadLoggingEnabled(true)
                .asyncCheckpointEnabled(true);
    }

    /**
     * 自定义数据库的加密算法
     * @return cipherSpec
     */
    private static SQLiteCipherSpec getCipherSpec() {
        return new SQLiteCipherSpec()
                .setPageSize(4096)
                .setKDFIteration(64000);
    }

    /**
     * 获取加密数据库的密钥
     * @return secret
     */
    private static byte[] getSecret(Context context) {
        byte[] secret = null;
        try {
            secret = new MasterKey.Builder(context.getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
                    .toString()
                    .getBytes();
        } catch (Exception e) {
            Logger.e(TAG, Log.getStackTraceString(e));
        }
        if (secret != null) {
            return secret;
        }
        return "snow".getBytes();
    }
}
