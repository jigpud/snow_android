package com.jigpud.snow.repository.story;

import android.annotation.SuppressLint;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.*;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.http.StoryService;
import com.jigpud.snow.repository.base.BaseRepository;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class StoryRepositoryImpl extends BaseRepository implements StoryRepository {
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
        return withIO(storyService.getUserStoryList(userid, pageSize, currentPage))
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<List<StoryEntity>> getMyStoryList(long pageSize, long currentPage) {
        return withIO(storyService.getMyStoryList(pageSize, currentPage))
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<List<StoryEntity>> getMomentsStoryList(long pageSize, long currentPage) {
        return withIO(storyService.getMomentsStoryList(pageSize, currentPage))
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<Pair<Boolean, String>> likeStory(String storyId) {
        return withIO(storyService.likeStory(storyId))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> unlikeStory(String storyId) {
        return withIO(storyService.unlikeStory(storyId))
                .map(super::handleResponseStatus);
    }

    @SuppressLint("CheckResult")
    @Override
    public LiveData<StoryEntity> getStory(String storyId) {
        withIO(storyService.getStory(storyId))
                .subscribe(this::handleStoryResponse);
        return storyDao.getStory(storyId);
    }

    @Override
    public Observable<Pair<Boolean, String>> release(String title, String content, List<String> pictureList, String attractionId) {
        PostStoryRequest story = new PostStoryRequest();
        story.setTitle(title);
        story.setContent(content);
        story.setPictures(pictureList);
        story.setAttractionId(attractionId);
        return withIO(storyService.postStory(story))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> postComment(String storyId, String content) {
        return withIO(storyService.postComment(storyId, content))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> likeComment(String commentId) {
        return withIO(storyService.likeComment(commentId))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> unlikeComment(String commentId) {
        return withIO(storyService.unlikeComment(commentId))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<List<CommentResponse>> getCommentList(String storyId, long pageSize, long currentPage) {
        return withIO(storyService.getCommentList(storyId, pageSize, currentPage))
                .map(this::handleCommentListResponse);
    }

    @Override
    public Observable<CommentResponse> getComment(String commentId) {
        return withIO(storyService.getComment(commentId))
                .map(this::handleCommentResponse);
    }

    @Override
    public Observable<Pair<Boolean, String>> favoriteStory(String storyId) {
        return withIO(storyService.favoriteStory(storyId))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<Pair<Boolean, String>> unFavoriteStory(String storyId) {
        return withIO(storyService.unFavoriteStory(storyId))
                .map(super::handleResponseStatus);
    }

    @Override
    public Observable<List<StoryEntity>> getSelfFavoriteStoryList(long pageSize, long currentPage) {
        return storyService.getSelfFavoriteStoryList(pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleStoryListResponse);
    }

    private CommentResponse handleCommentResponse(ApiResponse<CommentResponse> apiResponse) {
        return apiResponse.getData();
    }

    private List<CommentResponse> handleCommentListResponse(ApiResponse<PageData<CommentResponse>> apiResponse) {
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            return apiResponse.getData().getRecords();
        } else {
            return new ArrayList<>();
        }
    }

    private void handleStoryResponse(ApiResponse<StoryResponse> storyResponse) {
        if (storyResponse.isSuccess()) {
            StoryEntity story = StoryEntity.create(storyResponse.getData());
            storyDao.insert(story);
        }
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
