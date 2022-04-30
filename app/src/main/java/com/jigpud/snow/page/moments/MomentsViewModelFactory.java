package com.jigpud.snow.page.moments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.http.AttractionService;
import com.jigpud.snow.http.RecommendService;
import com.jigpud.snow.http.StoryService;
import com.jigpud.snow.repository.attraction.AttractionRepository;
import com.jigpud.snow.repository.attraction.AttractionRepositoryImpl;
import com.jigpud.snow.repository.recommend.RecommendRepository;
import com.jigpud.snow.repository.recommend.RecommendRepositoryImpl;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.repository.story.StoryRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class MomentsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MomentsViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        StoryDao storyDao = database.storyDao();
        UserDao userDao = database.userDao();
        AttractionDao attractionDao = database.attractionDao();

        RecommendService recommendService = ApiGenerator.create(RecommendService.class);
        AttractionService attractionService = ApiGenerator.create(AttractionService.class);
        StoryService storyService = ApiGenerator.create(StoryService.class);

        AttractionRepository attractionRepository = AttractionRepositoryImpl.getInstance(attractionService, attractionDao, storyDao);
        StoryRepository storyRepository = StoryRepositoryImpl.getInstance(storyService, storyDao);
        RecommendRepository recommendRepository = RecommendRepositoryImpl.getInstance(recommendService, attractionDao, userDao);
        return (T) new MomentsViewModel(recommendRepository, attractionRepository, storyRepository);
    }

    public static MomentsViewModelFactory create() {
        return new MomentsViewModelFactory();
    }
}
