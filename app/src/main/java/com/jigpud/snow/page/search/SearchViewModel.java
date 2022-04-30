package com.jigpud.snow.page.search;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.search.SearchRepository;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.repository.user.UserRepository;
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
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    private long storyCurrentPage = 1;
    private long userCurrentPage = 1;
    private long attractionCurrentPage = 1;

    public SearchViewModel(
            SearchRepository searchRepository,
            StoryRepository storyRepository,
            UserRepository userRepository
    ) {
        this.searchRepository = searchRepository;
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
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

    public LiveData<List<StoryEntity>> searchStory(String keyWords) {
        Logger.d(TAG, "search story");
        storyCurrentPage = 1;
        return searchStory(keyWords, storyCurrentPage);
    }

    public LiveData<List<StoryEntity>> loadMoreStorySearchResult(String keyWords) {
        long storyCurrentPage = this.storyCurrentPage + 1;
        Logger.d(TAG, "load story search result page %d", storyCurrentPage);
        return Transformations.map(searchStory(keyWords, storyCurrentPage), storySearchResult -> {
            if (storySearchResult != null && !storySearchResult.isEmpty()) {
                this.storyCurrentPage = storyCurrentPage;
            }
            return storySearchResult;
        });
    }

    public LiveData<List<UserEntity>> searchUser(String keyWords) {
        Logger.d(TAG, "search user");
        userCurrentPage = 1;
        return searchUser(keyWords, userCurrentPage);
    }

    public LiveData<List<UserEntity>> loadMoreUserSearchResult(String keyWords) {
        long userCurrentPage = this.userCurrentPage + 1;
        Logger.d(TAG, "load user search result page %d", userCurrentPage);
        return Transformations.map(searchUser(keyWords, userCurrentPage), userSearchResult -> {
            if (userSearchResult != null && !userSearchResult.isEmpty()) {
                this.userCurrentPage = userCurrentPage;
            }
            return userSearchResult;
        });
    }

    public LiveData<List<AttractionEntity>> searchAttraction(String keyWords) {
        Logger.d(TAG, "search attraction");
        attractionCurrentPage = 1;
        return searchAttraction(keyWords, attractionCurrentPage);
    }

    public LiveData<List<AttractionEntity>> loadMoreAttractionSearchResult(String keyWords) {
        long attractionCurrentPage = this.attractionCurrentPage + 1;
        Logger.d(TAG, "load attraction search result page %d", attractionCurrentPage);
        return Transformations.map(searchAttraction(keyWords, attractionCurrentPage), attractionSearchResult -> {
            if (attractionSearchResult != null && !attractionSearchResult.isEmpty()) {
                this.attractionCurrentPage = attractionCurrentPage;
            }
            return attractionSearchResult;
        });
    }

    public LiveData<Pair<Boolean, String>> likeStory(String storyId) {
        MutableLiveData<Pair<Boolean, String>> likeStoryStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.likeStory(storyId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> likeStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(likeStoryStatusLiveData::postValue);
        lifecycle(disposable);
        return likeStoryStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unlikeStory(String storyId) {
        MutableLiveData<Pair<Boolean, String>> unlikeStoryStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.unlikeStory(storyId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> unlikeStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(unlikeStoryStatusLiveData::postValue);
        lifecycle(disposable);
        return unlikeStoryStatusLiveData;
    }

    public LiveData<StoryEntity> getStory(String storyId) {
        return storyRepository.getStory(storyId);
    }

    public LiveData<Pair<Boolean, String>> follow(String userid) {
        MutableLiveData<Pair<Boolean, String>> followStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.follow(userid)
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> followStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(followStatusLiveData::postValue);
        lifecycle(disposable);
        return followStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unfollow(String userid) {
        MutableLiveData<Pair<Boolean, String>> unfollowStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.unfollow(userid)
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> unfollowStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(unfollowStatusLiveData::postValue);
        lifecycle(disposable);
        return unfollowStatusLiveData;
    }

    public LiveData<UserEntity> getUserInfo(String userid) {
        return userRepository.getUserInfo(userid);
    }

    private LiveData<List<StoryEntity>> searchStory(String keyWords, long currentPage) {
        MutableLiveData<List<StoryEntity>> storySearchResult = new MutableLiveData<>();
        Disposable disposable = searchRepository.searchStory(keyWords, SEARCH_RESULT_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> storySearchResult.postValue(new ArrayList<>()))
                .subscribe(storySearchResult::postValue);
        lifecycle(disposable);
        return storySearchResult;
    }

    private LiveData<List<UserEntity>> searchUser(String keyWords, long currentPage) {
        MutableLiveData<List<UserEntity>> userSearchResult = new MutableLiveData<>();
        Disposable disposable = searchRepository.searchUser(keyWords, SEARCH_RESULT_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> userSearchResult.postValue(new ArrayList<>()))
                .subscribe(userSearchResult::postValue);
        lifecycle(disposable);
        return userSearchResult;
    }

    private LiveData<List<AttractionEntity>> searchAttraction(String keyWords, long currentPage) {
        MutableLiveData<List<AttractionEntity>> attractionSearchResult = new MutableLiveData<>();
        Disposable disposable = searchRepository.searchAttraction(keyWords, SEARCH_RESULT_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> attractionSearchResult.postValue(new ArrayList<>()))
                .subscribe(attractionSearchResult::postValue);
        lifecycle(disposable);
        return attractionSearchResult;
    }
}
