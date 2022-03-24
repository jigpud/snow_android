package com.jigpud.snow.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jigpud.snow.database.entity.TokenEntity;

/**
 * @author jigpud
 */
@Dao
public interface TokenDao {
    @Query("SELECT * FROM token WHERE username=:username")
    TokenEntity getUserToken(String username);

    @Query("SELECT token FROM token WHERE username=:username")
    String getToken(String username);

    @Query("SELECT refresh_token FROM token WHERE username=:username")
    String getRefreshToken(String username);

    @Query("UPDATE token SET token=:token WHERE username=:username")
    void updateToken(String username, String token);

    @Query("UPDATE token SET refresh_token=:refreshToken WHERE username=:username")
    void updateRefreshToken(String username, String refreshToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TokenEntity tokenEntity);
}
