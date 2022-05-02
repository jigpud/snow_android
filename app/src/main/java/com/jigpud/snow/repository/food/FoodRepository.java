package com.jigpud.snow.repository.food;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.FoodPictureResponse;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.FoodEntity;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author : jigpud
 */
public interface FoodRepository {
    Observable<List<FoodEntity>> getFoodList(long pageSize, long currentPage);

    LiveData<FoodEntity> getFood(String foodId);

    Observable<List<AttractionEntity>> getAttractionList(String foodId, long pageSize, long currentPage);

    Observable<List<FoodPictureResponse>> getPictureList(String fooId, long pageSize, long currentPage);

    Observable<Pair<Boolean, String>> uploadPicture(String foodId, String picture);

    Observable<Pair<Boolean, String>> deletePicture(String foodId, String picture);
}
