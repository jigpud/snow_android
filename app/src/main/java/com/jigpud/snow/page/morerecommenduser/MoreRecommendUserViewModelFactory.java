package com.jigpud.snow.page.morerecommenduser;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.http.RecommendService;
import com.jigpud.snow.http.UserService;
import com.jigpud.snow.repository.recommend.RecommendRepository;
import com.jigpud.snow.repository.recommend.RecommendRepositoryImpl;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.user.UserRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class MoreRecommendUserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MoreRecommendUserViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        AttractionDao attractionDao = database.attractionDao();
        UserDao userDao = database.userDao();
        TokenDao tokenDao = database.tokenDao();

        RecommendService recommendService = ApiGenerator.create(RecommendService.class);
        UserService userService=  ApiGenerator.create(UserService.class);

        RecommendRepository recommendRepository = RecommendRepositoryImpl.getInstance(recommendService, attractionDao, userDao);
        UserRepository userRepository = UserRepositoryImpl.getInstance(userService, tokenDao, userDao);
        return (T) new MoreRecommendUserViewModel(recommendRepository, userRepository);
    }

    public static MoreRecommendUserViewModelFactory create() {
        return new MoreRecommendUserViewModelFactory();
    }
}
