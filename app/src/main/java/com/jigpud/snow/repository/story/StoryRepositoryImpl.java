package com.jigpud.snow.repository.story;

import android.content.Context;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.http.StoryService;
import com.jigpud.snow.util.network.ApiGenerator;

import java.util.List;

/**
 * @author jigpud
 */
public class StoryRepositoryImpl implements StoryRepository {
    private static volatile StoryRepositoryImpl instance;

    private final StoryDao storyDao;
    private final StoryService storyService;

    private StoryRepositoryImpl(StoryDao storyDao) {
        this.storyService = ApiGenerator.create(StoryService.class);
        this.storyDao = storyDao;
    }

    @Override
    public LiveData<Pair<List<StoryEntity>, Boolean>> myStoryList() {
        return null;
    }

    @Override
    public LiveData<Pair<List<StoryEntity>, Boolean>> moreMyStoryList() {
        return null;
    }

    public StoryRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (StoryRepositoryImpl.class) {
                if (instance == null) {
                    Context applicationContext = context.getApplicationContext();
                    SnowDatabase database = SnowDatabase.getSnowDatabase(applicationContext);
                    StoryDao storyDao = database.storyDao();
                    instance = new StoryRepositoryImpl(storyDao);
                }
            }
        }
        return instance;
    }
}
