package com.jigpud.snow.page.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.SearchHistoryDao;
import com.jigpud.snow.http.SearchService;
import com.jigpud.snow.repository.search.SearchRepository;
import com.jigpud.snow.repository.search.SearchRepositoryImpl;
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
        SnowDatabase snowDatabase = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        SearchHistoryDao searchHistoryDao = snowDatabase.searchHistoryDao();

        SearchService searchService = ApiGenerator.create(SearchService.class);
        SearchRepository searchRepository = SearchRepositoryImpl.getInstance(searchService, searchHistoryDao);
        return (T) new SearchViewModel(searchRepository);
    }

    public static SearchViewModelFactory create() {
        return new SearchViewModelFactory();
    }
}
