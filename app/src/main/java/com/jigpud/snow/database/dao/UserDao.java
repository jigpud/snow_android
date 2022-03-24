package com.jigpud.snow.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jigpud.snow.database.entity.UserEntity;

/**
 * @author jigpud
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE username=:username")
    UserEntity getUserByUsername(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity userEntity);

    @Query("SELECT * FROM user WHERE username=:username")
    LiveData<UserEntity> getUserLiveDataByUsername(String username);
}
