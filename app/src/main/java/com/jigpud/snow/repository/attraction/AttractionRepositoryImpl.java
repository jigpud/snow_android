package com.jigpud.snow.repository.attraction;

import android.annotation.SuppressLint;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.*;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.FoodDao;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.FoodEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.http.AttractionService;
import com.jigpud.snow.repository.base.BaseRepository;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class AttractionRepositoryImpl extends BaseRepository implements AttractionRepository {
    private static volatile AttractionRepositoryImpl instance;

    private final AttractionService attractionService;
    private final StoryDao storyDao;
    private final AttractionDao attractionDao;
    private final FoodDao foodDao;

    private AttractionRepositoryImpl(
            AttractionService attractionService,
            StoryDao storyDao,
            AttractionDao attractionDao,
            FoodDao foodDao
    ) {
        this.attractionService = attractionService;
        this.attractionDao = attractionDao;
        this.storyDao = storyDao;
        this.foodDao = foodDao;
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<AttractionEntity> getAttraction(String attractionId) {
        withIO(attractionService.getAttraction(attractionId))
                .subscribe(this::handleAttractionResponse);
        return attractionDao.getAttractionLiveData(attractionId);
    }

    @Override
    public Observable<List<StoryEntity>> getStoryList(String attractionId, long pageSize, long currentPage) {
        return withIO(attractionService.getAttractionStoryList(attractionId, pageSize, currentPage))
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<Pair<Boolean, String>> follow(String attractionId) {
        return withIO(attractionService.followAttraction(attractionId))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> unfollow(String attractionId) {
        return withIO(attractionService.unfollowAttraction(attractionId))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> score(String attractionId, int score) {
        return withIO(attractionService.scoreAttraction(attractionId, score))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<List<AttractionEntity>> getFollowingList(long pageSize, long currentPage) {
        return withIO(attractionService.getFollowingAttractionList(pageSize, currentPage))
                .map(this::handleAttractionListResponse);
    }

    @Override
    public Observable<List<FoodEntity>> getFoodList(String attractionId, long pageSize, long currentPage) {
        return withIO(attractionService.getFoodList(attractionId, pageSize, currentPage))
                .map(this::handleFoodListResponse);
    }

    @Override
    public Observable<List<AttractionPictureResponse>> getPictureList(String attractionId, long pageSize, long currentPage) {
        return withIO(attractionService.getPictureList(attractionId, pageSize, currentPage))
                .map(super::handleListResponse);
    }

    @Override
    public Observable<Pair<Boolean, String>> uploadPicture(String attractionId, String picture) {
        return withIO(attractionService.uploadPicture(attractionId, picture))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> deletePicture(String attractionId, String picture) {
        return withIO(attractionService.deletePicture(attractionId, picture))
                .map(super::handleResponseStatus);
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

    private void handleAttractionResponse(ApiResponse<AttractionResponse> apiResponse) {
        if (apiResponse.isSuccess()) {
            AttractionEntity attractionEntity = AttractionEntity.create(apiResponse.getData());
            attractionDao.insert(attractionEntity);
        }
    }

    private List<StoryEntity> handleStoryListResponse(ApiResponse<PageData<StoryResponse>> apiResponse) {
        List<StoryEntity> storyEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (StoryResponse storyResponse : apiResponse.getData().getRecords()) {
                StoryEntity storyEntity = StoryEntity.create(storyResponse);
                storyEntityList.add(storyEntity);
            }
            storyDao.insertAll(storyEntityList);
        }
        return storyEntityList;
    }

    public static AttractionRepositoryImpl getInstance(
            AttractionService attractionService,
            AttractionDao attractionDao,
            StoryDao storyDao,
            FoodDao foodDao
    ) {
        if (instance == null) {
            synchronized (AttractionRepositoryImpl.class) {
                if (instance == null) {
                    instance = new AttractionRepositoryImpl(attractionService, storyDao, attractionDao, foodDao);
                }
            }
        }
        return instance;
    }
}
