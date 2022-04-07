package com.jigpud.snow.repository.story;

import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.StoryListResponse;
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
    public Observable<List<StoryListResponse>> getUserStoryList(String userid, long pageCount, long page) {
        return null;
    }

    @Override
    public Observable<List<StoryListResponse>> getMyStoryList(long pageCount, long page) {
        return storyService.getSelfStoryList(pageCount, page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleStoryListResponse);
    }

    @Override
    public Observable<List<StoryListResponse>> getSelfMomentsStoryList(long pageCount, long page) {
        return null;
    }

    private List<StoryListResponse> handleStoryListResponse(ApiResponse<PageData<StoryListResponse>> storyListResponse) {
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
