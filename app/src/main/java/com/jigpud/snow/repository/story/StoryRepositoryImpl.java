package com.jigpud.snow.repository.story;

import androidx.core.util.Pair;
import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.ApiResponseStatus;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.StoryResponse;
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

    private StoryRepositoryImpl(StoryService storyService) {
        this.storyService = storyService;
    }

    @Override
    public Observable<List<StoryResponse>> getUserStoryList(String userid, long pageCount, long page) {
        return null;
    }

    @Override
    public Observable<List<StoryResponse>> getMyStoryList(long pageCount, long page) {
        return storyService.getSelfStoryList(pageCount, page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<List<StoryResponse>> getSelfMomentsStoryList(long pageCount, long page) {
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

    @Override
    public Observable<StoryResponse> getStory(String storyId) {
        return storyService.getStory(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(ApiResponse::getData);
    }

    private Pair<Boolean, String> handleLikedResponse(ApiResponseStatus likedResponse) {
        return new Pair<>(likedResponse.isSuccess(), likedResponse.getMessage());
    }

    private List<StoryResponse> handleStoryListResponse(ApiResponse<PageData<StoryResponse>> storyListResponse) {
        if (storyListResponse.isSuccess() && storyListResponse.getData().getRecords() != null) {
            return storyListResponse.getData().getRecords();
        } else {
            return new ArrayList<>();
        }
    }

    public static StoryRepositoryImpl getInstance(StoryService storyService) {
        if (instance == null) {
            synchronized (StoryRepositoryImpl.class) {
                if (instance == null) {
                    instance = new StoryRepositoryImpl(storyService);
                }
            }
        }
        return instance;
    }
}
