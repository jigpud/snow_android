package com.jigpud.snow.page.morefollowingattraction;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.FoodDao;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.http.AttractionService;
import com.jigpud.snow.repository.attraction.AttractionRepository;
import com.jigpud.snow.repository.attraction.AttractionRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class MoreFollowingAttractionViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MoreFollowingAttractionViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        AttractionDao attractionDao = database.attractionDao();
        StoryDao storyDao = database.storyDao();
        FoodDao foodDao = database.foodDao();

        AttractionService attractionService = ApiGenerator.create(AttractionService.class);

        AttractionRepository attractionRepository = AttractionRepositoryImpl.getInstance(attractionService, attractionDao, storyDao, foodDao);
        return (T) new MoreFollowingAttractionViewModel(attractionRepository);
    }

    public static MoreFollowingAttractionViewModelFactory create() {
        return new MoreFollowingAttractionViewModelFactory();
    }
}
