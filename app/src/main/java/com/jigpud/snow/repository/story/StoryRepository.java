package com.jigpud.snow.repository.story;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.database.entity.StoryEntity;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author jigpud
 */
public interface StoryRepository {
    Observable<List<StoryEntity>> getUserStoryList(String userid, long pageSize, long currentPage);

    Observable<List<StoryEntity>> getMyStoryList(long pageSize, long currentPage);

    Observable<List<StoryEntity>> getMyMomentsStoryList(long pageSize, long currentPage);

    Observable<Pair<Boolean, String>> likeStory(String storyId);

    Observable<Pair<Boolean, String>> unlikeStory(String storyId);

    LiveData<StoryEntity> getStory(String storyId);

    Observable<Pair<Boolean, String>> release(String title, String content, List<String> pictureList, String attractionId);
}
