package com.jigpud.snow.repository.story;

import androidx.core.util.Pair;
import com.jigpud.snow.bean.StoryResponse;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author jigpud
 */
public interface StoryRepository {
    Observable<List<StoryResponse>> getUserStoryList(String userid, long pageCount, long page);

    Observable<List<StoryResponse>> getMyStoryList(long pageCount, long page);

    Observable<List<StoryResponse>> getSelfMomentsStoryList(long pageCount, long page);

    Observable<Pair<Boolean, String>> likeStory(String storyId);

    Observable<Pair<Boolean, String>> unlikeStory(String storyId);

    Observable<StoryResponse> getStory(String storyId);
}
