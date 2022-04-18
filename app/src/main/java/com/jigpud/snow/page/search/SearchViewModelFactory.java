package com.jigpud.snow.page.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.SearchHistoryDao;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.http.SearchService;
import com.jigpud.snow.http.StoryService;
import com.jigpud.snow.http.UserService;
import com.jigpud.snow.repository.search.SearchRepository;
import com.jigpud.snow.repository.search.SearchRepositoryImpl;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.repository.story.StoryRepositoryImpl;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.user.UserRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private SearchViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        UserDao userDao = database.userDao();
        TokenDao tokenDao = database.tokenDao();
        SearchHistoryDao searchHistoryDao = database.searchHistoryDao();
        StoryDao storyDao = database.storyDao();

        SearchService searchService = ApiGenerator.create(SearchService.class);
        StoryService storyService = ApiGenerator.create(StoryService.class);
        UserService userService = ApiGenerator.create(UserService.class);

        SearchRepository searchRepository = SearchRepositoryImpl.getInstance(searchService, searchHistoryDao);
        StoryRepository storyRepository = StoryRepositoryImpl.getInstance(storyService, storyDao);
        UserRepository userRepository = UserRepositoryImpl.getInstance(userService, tokenDao, userDao);

        return (T) new SearchViewModel(searchRepository, storyRepository, userRepository);
    }

    public static SearchViewModelFactory create() {
        return new SearchViewModelFactory();
    }
}
