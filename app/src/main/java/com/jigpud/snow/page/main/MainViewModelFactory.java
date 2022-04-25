package com.jigpud.snow.page.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.http.RecommendService;
import com.jigpud.snow.repository.recommend.RecommendRepository;
import com.jigpud.snow.repository.recommend.RecommendRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MainViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        AttractionDao attractionDao = database.attractionDao();
        UserDao userDao = database.userDao();

        RecommendService recommendService = ApiGenerator.create(RecommendService.class);

        RecommendRepository recommendRepository = RecommendRepositoryImpl.getInstance(recommendService, attractionDao, userDao);
        return (T) new MainViewModel(recommendRepository);
    }

    public static MainViewModelFactory create() {
        return new MainViewModelFactory();
    }
}
