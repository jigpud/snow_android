package com.jigpud.snow.page.morefood;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.FoodDao;
import com.jigpud.snow.http.FoodService;
import com.jigpud.snow.repository.food.FoodRepository;
import com.jigpud.snow.repository.food.FoodRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class MoreFoodViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MoreFoodViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        AttractionDao attractionDao = database.attractionDao();
        FoodDao foodDao = database.foodDao();

        FoodService foodService = ApiGenerator.create(FoodService.class);

        FoodRepository foodRepository = FoodRepositoryImpl.getInstance(foodService, attractionDao, foodDao);
        return (T) new MoreFoodViewModel(foodRepository);
    }

    public static MoreFoodViewModelFactory create() {
        return new MoreFoodViewModelFactory();
    }
}
