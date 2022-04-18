package com.jigpud.snow.page.userprofile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.http.StoryService;
import com.jigpud.snow.http.UserService;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.repository.story.StoryRepositoryImpl;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.user.UserRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class UserProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private UserProfileViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        UserDao userDao = database.userDao();
        TokenDao tokenDao = database.tokenDao();
        StoryDao storyDao = database.storyDao();

        UserService userService = ApiGenerator.create(UserService.class);
        StoryService storyService = ApiGenerator.create(StoryService.class);

        UserRepository userRepository = UserRepositoryImpl.getInstance(userService, tokenDao, userDao);
        StoryRepository storyRepository = StoryRepositoryImpl.getInstance(storyService, storyDao);

        return (T) new UserProfileViewModel(userRepository, storyRepository);
    }

    public static UserProfileViewModelFactory create() {
        return new UserProfileViewModelFactory();
    }
}
