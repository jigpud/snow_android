package com.jigpud.snow.page.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.FoodDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.http.FoodService;
import com.jigpud.snow.http.RecommendService;
import com.jigpud.snow.repository.food.FoodRepository;
import com.jigpud.snow.repository.food.FoodRepositoryImpl;
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
        FoodDao foodDao = database.foodDao();

        RecommendService recommendService = ApiGenerator.create(RecommendService.class);
        FoodService foodService = ApiGenerator.create(FoodService.class);

        RecommendRepository recommendRepository = RecommendRepositoryImpl.getInstance(recommendService, attractionDao, userDao);
        FoodRepository foodRepository = FoodRepositoryImpl.getInstance(foodService, attractionDao, foodDao);
        return (T) new MainViewModel(recommendRepository, foodRepository);
    }

    public static MainViewModelFactory create() {
        return new MainViewModelFactory();
    }
}
