package com.jigpud.snow.repository.story;

import android.annotation.SuppressLint;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.ApiResponseStatus;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.StoryResponse;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.http.StoryService;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jigpud
 */
public class StoryRepositoryImpl implements StoryRepository {
    private static volatile StoryRepositoryImpl instance;

    private final StoryService storyService;
    private final StoryDao storyDao;

    private StoryRepositoryImpl(StoryService storyService, StoryDao storyDao) {
        this.storyService = storyService;
        this.storyDao = storyDao;
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<List<StoryEntity>> getUserStoryList(String userid, long pageSize, long currentPage) {
        return storyService.getUserStoryList(userid, pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<List<StoryEntity>> getMyStoryList(long pageSize, long currentPage) {
        return storyService.getMyStoryList(pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<List<StoryEntity>> getMyMomentsStoryList(long pageSize, long currentPage) {
        return null;
    }

    @Override
    public Observable<Pair<Boolean, String>> likeStory(String storyId) {
        return storyService.likeStory(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleLikedResponse);
    }

    @Override
    public Observable<Pair<Boolean, String>> unlikeStory(String storyId) {
        return storyService.unlikeStory(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleLikedResponse);
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<StoryEntity> getStory(String storyId) {
        storyService.getStory(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::handleStoryResponse);
        return storyDao.getStory(storyId);
    }

    private long calculateOffset(long pageSize, long currentPage) {
        return (currentPage - 1) * pageSize;
    }

    private void handleStoryResponse(ApiResponse<StoryResponse> storyResponse) {
        if (storyResponse.isSuccess()) {
            StoryEntity story = StoryEntity.create(storyResponse.getData());
            storyDao.insert(story);
        }
    }

    private Pair<Boolean, String> handleLikedResponse(ApiResponseStatus likedResponse) {
        return new Pair<>(likedResponse.isSuccess(), likedResponse.getMessage());
    }

    private List<StoryEntity> handleStoryListResponse(ApiResponse<PageData<StoryResponse>> storyListResponse) {
        List<StoryEntity> storyEntityList = new ArrayList<>();
        if (storyListResponse.isSuccess()) {
            for (StoryResponse storyResponse : storyListResponse.getData().getRecords()) {
                storyEntityList.add(StoryEntity.create(storyResponse));
            }
            storyDao.insertAll(storyEntityList);
        }
        return storyEntityList;
    }

    public static StoryRepositoryImpl getInstance(StoryService storyService, StoryDao storyDao) {
        if (instance == null) {
            synchronized (StoryRepositoryImpl.class) {
                if (instance == null) {
                    instance = new StoryRepositoryImpl(storyService, storyDao);
                }
            }
        }
        return instance;
    }
}
