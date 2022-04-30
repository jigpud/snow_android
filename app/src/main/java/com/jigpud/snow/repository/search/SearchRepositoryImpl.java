package com.jigpud.snow.repository.search;

import androidx.lifecycle.LiveData;
import bolts.Task;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.bean.*;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.SearchHistoryDao;
import com.jigpud.snow.database.dao.StoryDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.database.entity.UserEntity;
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
    private final UserDao userDao;
    private final StoryDao storyDao;
    private final AttractionDao attractionDao;

    private SearchRepositoryImpl(
            SearchService searchService,
            SearchHistoryDao searchHistoryDao,
            UserDao userDao,
            StoryDao storyDao,
            AttractionDao attractionDao
    ) {
        this.searchService = searchService;
        this.searchHistoryDao = searchHistoryDao;
        this.userDao = userDao;
        this.storyDao = storyDao;
        this.attractionDao = attractionDao;
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
    public Observable<List<StoryEntity>> searchStory(String keyWords, long pageSize, long currentPage) {
        return searchService.searchStory(keyWords, pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleStorySearchResult);
    }

    @Override
    public Observable<List<UserEntity>> searchUser(String keyWords, long pageSize, long currentPage) {
        return searchService.searchUser(keyWords, pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleUserSearchResult);
    }

    @Override
    public Observable<List<AttractionEntity>> searchAttraction(String keyWords, long pageSize, long currentPage) {
        return searchService.searchAttraction(keyWords, pageSize, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(this::handleAttractionSearchResult);
    }

    private List<AttractionEntity> handleAttractionSearchResult(ApiResponse<PageData<AttractionResponse>> apiResponse) {
        List<AttractionEntity> attractionEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (AttractionResponse attractionResponse : apiResponse.getData().getRecords()) {
                AttractionEntity attractionEntity = AttractionEntity.create(attractionResponse);
                attractionEntityList.add(attractionEntity);
            }
            attractionDao.insertAll(attractionEntityList);
        }
        return attractionEntityList;
    }

    private List<UserEntity> handleUserSearchResult(ApiResponse<PageData<UserInformationResponse>> apiResponse) {
        List<UserEntity> userEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (UserInformationResponse userInformationResponse : apiResponse.getData().getRecords()) {
                UserEntity userEntity = UserEntity.create(userInformationResponse);
                userEntityList.add(userEntity);
            }
            userDao.insertAll(userEntityList);
        }
        return userEntityList;
    }

    private List<StoryEntity> handleStorySearchResult(ApiResponse<PageData<StoryResponse>> apiResponse) {
        List<StoryEntity> storyEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (StoryResponse storyResponse : apiResponse.getData().getRecords()) {
                StoryEntity storyEntity = StoryEntity.create(storyResponse);
                storyEntityList.add(storyEntity);
            }
            storyDao.insertAll(storyEntityList);
        }
        return storyEntityList;
    }

    public static SearchRepositoryImpl getInstance(
            SearchService searchService,
            SearchHistoryDao searchHistoryDao,
            StoryDao storyDao,
            UserDao userDao,
            AttractionDao attractionDao
    ) {
        if (instance == null) {
            synchronized (SearchRepositoryImpl.class) {
                if (instance == null) {
                    instance = new SearchRepositoryImpl(searchService, searchHistoryDao, userDao, storyDao, attractionDao);
                }
            }
        }
        return instance;
    }
}
