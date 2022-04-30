package com.jigpud.snow.repository.attraction;

import android.annotation.SuppressLint;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.*;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.http.AttractionService;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class AttractionRepositoryImpl implements AttractionRepository {
    private static volatile AttractionRepositoryImpl instance;

    private final AttractionService attractionService;
    private final StoryDao storyDao;
    private final AttractionDao attractionDao;

    private AttractionRepositoryImpl(
            AttractionService attractionService,
            StoryDao storyDao,
            AttractionDao attractionDao
    ) {
        this.attractionService = attractionService;
        this.attractionDao = attractionDao;
        this.storyDao = storyDao;
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<AttractionEntity> getAttraction(String attractionId) {
        attractionService.getAttraction(attractionId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::handleAttractionResponse);
        return attractionDao.getAttractionLiveData(attractionId);
    }

    @Override
    public Observable<List<StoryEntity>> getAttractionStoryList(String attractionId, long pageSize, long currentPage) {
        return attractionService.getAttractionStoryList(attractionId, pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<Pair<Boolean, String>> followAttraction(String attractionId) {
        return attractionService.followAttraction(attractionId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> unfollowAttraction(String attractionId) {
        return attractionService.unfollowAttraction(attractionId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> scoreAttraction(String attractionId, int score) {
        return attractionService.scoreAttraction(attractionId, score)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> uploadPhoto(String attractionId, String photo) {
        return attractionService.uploadAttractionPhoto(attractionId, photo)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> deletePhoto(String attractionId, String photo) {
        return attractionService.deleteAttractionPhoto(attractionId, photo)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleResponseStatus);
    }

    @Override
    public Observable<List<AttractionEntity>> getFollowingAttractionList(long pageSize, long currentPage) {
        return attractionService.getFollowingAttractionList(pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleAttractionListResponse);
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

    private Pair<Boolean, String> handleResponseStatus(ApiResponseStatus apiResponseStatus) {
        return new Pair<>(apiResponseStatus.isSuccess(), apiResponseStatus.getMessage());
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
            StoryDao storyDao
    ) {
        if (instance == null) {
            synchronized (AttractionRepositoryImpl.class) {
                if (instance == null) {
                    instance = new AttractionRepositoryImpl(attractionService, storyDao, attractionDao);
                }
            }
        }
        return instance;
    }
}
