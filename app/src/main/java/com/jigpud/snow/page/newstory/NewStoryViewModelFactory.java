package com.jigpud.snow.page.newstory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.http.QiniuService;
import com.jigpud.snow.http.StoryService;
import com.jigpud.snow.repository.img.ImageRepository;
import com.jigpud.snow.repository.img.ImageRepositoryImpl;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.repository.story.StoryRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class NewStoryViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private NewStoryViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        StoryDao storyDao = database.storyDao();

        StoryService storyService = ApiGenerator.create(StoryService.class);
        QiniuService qiniuService = ApiGenerator.create(QiniuService.class);

        StoryRepository storyRepository = StoryRepositoryImpl.getInstance(storyService, storyDao);
        ImageRepository imageRepository = ImageRepositoryImpl.getInstance(qiniuService);
        return (T) new NewStoryViewModel(storyRepository, imageRepository);
    }

    public static NewStoryViewModelFactory create() {
        return new NewStoryViewModelFactory();
    }
}
