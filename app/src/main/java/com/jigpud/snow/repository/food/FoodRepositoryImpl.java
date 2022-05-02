package com.jigpud.snow.repository.food;

import android.annotation.SuppressLint;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.*;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.FoodDao;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.FoodEntity;
import com.jigpud.snow.http.FoodService;
import com.jigpud.snow.repository.base.BaseRepository;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class FoodRepositoryImpl extends BaseRepository implements FoodRepository {
    private static volatile FoodRepositoryImpl instance;

    private final FoodService foodService;
    private final AttractionDao attractionDao;
    private final FoodDao foodDao;

    private FoodRepositoryImpl(
            FoodService foodService,
            AttractionDao attractionDao,
            FoodDao foodDao
    ) {
        this.foodService = foodService;
        this.attractionDao = attractionDao;
        this.foodDao = foodDao;
    }

    @Override
    public Observable<List<FoodEntity>> getFoodList(long pageSize, long currentPage) {
        return withIO(foodService.getFoodList(pageSize, currentPage))
                .map(this::handleFoodListResponse);
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<FoodEntity> getFood(String foodId) {
        withIO(foodService.getFood(foodId))
                .subscribe(this::handleFoodResponse);
        return foodDao.getFoodLiveData(foodId);
    }

    @Override
    public Observable<List<AttractionEntity>> getAttractionList(String foodId, long pageSize, long currentPage) {
        return withIO(foodService.getAttractionList(foodId, pageSize, currentPage))
                .map(this::handleAttractionListResponse);
    }

    @Override
    public Observable<List<FoodPictureResponse>> getPictureList(String fooId, long pageSize, long currentPage) {
        return withIO(foodService.getPictureList(fooId, pageSize, currentPage))
                .map(super::handleListResponse);
    }

    @Override
    public Observable<Pair<Boolean, String>> uploadPicture(String foodId, String picture) {
        return withIO(foodService.uploadPicture(foodId, picture))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> deletePicture(String foodId, String picture) {
        return withIO(foodService.deletePicture(foodId, picture))
                .map(this::handleResponseStatus);
    }

    private List<FoodEntity> handleFoodListResponse(ApiResponse<PageData<FoodResponse>> apiResponse) {
        List<FoodEntity> foodEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (FoodResponse foodResponse : apiResponse.getData().getRecords()) {
                FoodEntity foodEntity = FoodEntity.create(foodResponse);
                foodEntityList.add(foodEntity);
            }
            foodDao.insertAll(foodEntityList);
        }
        return foodEntityList;
    }

    private void handleFoodResponse(ApiResponse<FoodResponse> apiResponse) {
        if (apiResponse.isSuccess()) {
            FoodEntity foodEntity = FoodEntity.create(apiResponse.getData());
            foodDao.insert(foodEntity);
        }
    }
    private List<AttractionEntity> handleAttractionListResponse(ApiResponse<PageData<AttractionResponse>> apiResponse) {
        List<AttractionEntity> attractionEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (AttractionResponse attractionResponse : apiResponse.getData().getRecords()) {
                AttractionEntity attractionEntity = AttractionEntity.create(attractionResponse);
                attractionEntityList.add(attractionEntity);
            }
            attractionDao.insertAll(attractionEntityList);
        }
        return attractionEntityList;
    }

    public static FoodRepositoryImpl getInstance(FoodService foodService, AttractionDao attractionDao, FoodDao foodDao) {
        if (instance == null) {
            synchronized (FoodRepositoryImpl.class) {
                if (instance == null) {
                    instance = new FoodRepositoryImpl(foodService, attractionDao, foodDao);
                }
            }
        }
        return instance;
    }
}
