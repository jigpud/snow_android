package com.jigpud.snow.page.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.bean.StoryResponse;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.search.SearchRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class SearchViewModel extends BaseViewModel {
    private static final String TAG = "SearchViewModel";

    public static final long SEARCH_RESULT_PAGE_SIZE = 10;

    private final SearchRepository searchRepository;
    private long storyCurrentPage = 1;
    private long userCurrentPage = 1;
    private long attractionCurrentPage = 1;

    public SearchViewModel(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public void addSearchHistory(String keyWords) {
        searchRepository.addSearchHistory(keyWords);
    }

    public LiveData<List<SearchHistoryEntity>> getSearchHistory() {
        return searchRepository.getSearchHistory();
    }

    public void deleteSearchHistory(String keyWords) {
        searchRepository.deleteSearchHistory(keyWords);
    }

    public void clearSearchHistory() {
        searchRepository.clearAllSearchHistory();
    }

    public LiveData<List<StoryResponse>> searchStory(String keyWords) {
        Logger.d(TAG, "search story");
        storyCurrentPage = 1;
        return searchStory(keyWords, storyCurrentPage);
    }

    public LiveData<List<StoryResponse>> moreStorySearchResult(String keyWords) {
        long storyCurrentPage = this.storyCurrentPage + 1;
        Logger.d(TAG, "load story search result page %d", storyCurrentPage);
        return Transformations.map(searchStory(keyWords, storyCurrentPage), storySearchResult -> {
            if (storySearchResult != null && !storySearchResult.isEmpty()) {
                this.storyCurrentPage++;
            }
            return storySearchResult;
        });
    }

    public LiveData<List<UserInformationResponse>> searchUser(String keyWords) {
        Logger.d(TAG, "search user");
        userCurrentPage = 1;
        return searchUser(keyWords, userCurrentPage);
    }

    public LiveData<List<UserInformationResponse>> moreUserSearchResult(String keyWords) {
        long userCurrentPage = this.userCurrentPage + 1;
        Logger.d(TAG, "load user search result page %d", userCurrentPage);
        return Transformations.map(searchUser(keyWords, userCurrentPage), userSearchResult -> {
            if (userSearchResult != null && !userSearchResult.isEmpty()) {
                this.userCurrentPage++;
            }
            return userSearchResult;
        });
    }

    private LiveData<List<StoryResponse>> searchStory(String keyWords, long page) {
        MutableLiveData<List<StoryResponse>> storySearchResult = new MutableLiveData<>();
        Disposable disposable = searchRepository.searchStory(keyWords, SEARCH_RESULT_PAGE_SIZE, page)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> storySearchResult.postValue(new ArrayList<>()))
                .subscribe(storySearchResult::postValue);
        lifecycle(disposable);
        return storySearchResult;
    }

    private LiveData<List<UserInformationResponse>> searchUser(String keyWords, long page) {
        MutableLiveData<List<UserInformationResponse>> userSearchResult = new MutableLiveData<>();
        Disposable disposable = searchRepository.searchUser(keyWords, SEARCH_RESULT_PAGE_SIZE, page)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> userSearchResult.postValue(new ArrayList<>()))
                .subscribe(userSearchResult::postValue);
        lifecycle(disposable);
        return userSearchResult;
    }
}
