package com.jigpud.snow.repository.story;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.database.entity.StoryEntity;

import java.util.List;

/**
 * @author jigpud
 */
public interface StoryRepository {
    LiveData<Pair<List<StoryEntity>, Boolean>> myStoryList();

    LiveData<Pair<List<StoryEntity>, Boolean>> moreMyStoryList();
}
