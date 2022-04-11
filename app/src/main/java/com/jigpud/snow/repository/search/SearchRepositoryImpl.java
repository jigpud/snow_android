package com.jigpud.snow.repository.search;

import androidx.lifecycle.LiveData;
import bolts.Task;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.StoryResponse;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.database.dao.SearchHistoryDao;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
import com.jigpud.snow.http.SearchService;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class SearchRepositoryImpl implements SearchRepository {
    private static final String TAG = "SearchRepository";
    private static volatile SearchRepositoryImpl instance;

    private final SearchService searchService;
    private final SearchHistoryDao searchHistoryDao;

    private SearchRepositoryImpl(SearchService searchService, SearchHistoryDao searchHistoryDao) {
        this.searchService = searchService;
        this.searchHistoryDao = searchHistoryDao;
    }

    @Override
    public void addSearchHistory(String keyWords) {
        Task.callInBackground(() -> {
            SearchHistoryEntity searchHistoryEntity = new SearchHistoryEntity();
            searchHistoryEntity.setSearchTime(System.currentTimeMillis());
            searchHistoryEntity.setKeyWords(keyWords);
            searchHistoryEntity.setUserid(CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUserid());
            searchHistoryDao.insert(searchHistoryEntity);
            Logger.d(TAG, "addSearchHistory: %s", searchHistoryEntity);
            return null;
        });
    }

    @Override
    public LiveData<List<SearchHistoryEntity>> getSearchHistory() {
        String currentUserid = CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUserid();
        return searchHistoryDao.getSearchHistory(currentUserid);
    }

    @Override
    public void deleteSearchHistory(String keyWords) {
        Task.callInBackground(() -> {
            searchHistoryDao.delete(keyWords);
            return null;
        });
    }

    @Override
    public void clearAllSearchHistory() {
        Task.callInBackground(() -> {
            searchHistoryDao.clearAll(CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUserid());
            return null;
        });
    }

    @Override
    public Observable<List<StoryResponse>> searchStory(String keyWords, long pageCount, long page) {
        return searchService.searchStory(keyWords, pageCount, page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleSearchResult);
    }

    @Override
    public Observable<List<UserInformationResponse>> searchUser(String keyWords, long pageCount, long page) {
        return searchService.searchUser(keyWords, pageCount, page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleSearchResult);
    }

    private <T> List<T> handleSearchResult(ApiResponse<PageData<T>> apiResponse) {
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            return apiResponse.getData().getRecords();
        } else {
            return new ArrayList<>();
        }
    }

    public static SearchRepositoryImpl getInstance(SearchService searchService, SearchHistoryDao searchHistoryDao) {
        if (instance == null) {
            synchronized (SearchRepositoryImpl.class) {
                if (instance == null) {
                    instance = new SearchRepositoryImpl(searchService, searchHistoryDao);
                }
            }
        }
        return instance;
    }
}
