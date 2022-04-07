package com.jigpud.snow.repository.story;

import com.jigpud.snow.bean.StoryListResponse;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author jigpud
 */
public interface StoryRepository {
    Observable<List<StoryListResponse>> getUserStoryList(String userid, long pageCount, long page);

    Observable<List<StoryListResponse>> getMyStoryList(long pageCount, long page);

    Observable<List<StoryListResponse>> getSelfMomentsStoryList(long pageCount, long page);
}
