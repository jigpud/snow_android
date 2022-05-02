package com.jigpud.snow.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jigpud.snow.database.entity.FoodEntity;

import java.util.List;

/**
 * @author : jigpud
 */
@Dao
public interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FoodEntity foodEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FoodEntity> foodEntityList);

    @Query("SELECT * FROM food WHERE food_id=:foodId")
    LiveData<FoodEntity> getFoodLiveData(String foodId);

    @Query("SELECT * FROM food")
    LiveData<List<FoodEntity>> getFoodListLiveData();
}
