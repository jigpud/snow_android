package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.jigpud.snow.bean.FoodResponse;
import lombok.Data;

import java.util.List;

/**
 * @author : jigpud
 */
@Entity(tableName = "food", indices = {
        @Index(value = "food_id", unique = true)
})
@Data
public class FoodEntity {
    // 主键
    @PrimaryKey(autoGenerate = true)
    private long id;

    // 美食id
    @ColumnInfo(name = "food_id")
    private String foodId;

    // 名称
    private String name;

    // 简介
    private String description;

    // 照片
    private List<String> pictures;

    public static FoodEntity create(FoodResponse foodResponse) {
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setFoodId(foodResponse.getFoodId());
        foodEntity.setName(foodResponse.getName());
        foodEntity.setDescription(foodResponse.getDescription());
        foodEntity.setPictures(foodResponse.getPictures());
        return foodEntity;
    }
}
