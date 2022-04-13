package com.jigpud.snow.page.search;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.bean.StoryResponse;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
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

    public LiveData<StoryResponse> getStory(String storyId) {
        MutableLiveData<StoryResponse> storyResponseLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getStory(storyId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> storyResponseLiveData.postValue(null))
                .subscribe(storyResponseLiveData::postValue);
        lifecycle(disposable);
        return storyResponseLiveData;
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

    public LiveData<UserInformationResponse> getUserInfo(String userid) {
        return Transformations.map(userRepository.getUserInfo(userid), userEntity -> {
            if (userEntity != null) {
                UserInformationResponse userInformationResponse = new UserInformationResponse();
                userInformationResponse.setUserid(userEntity.getUserid());
                userInformationResponse.setAge(userEntity.getAge());
                userInformationResponse.setAvatar(userEntity.getAvatar());
                userInformationResponse.setBackground(userEntity.getBackground());
                userInformationResponse.setFollowed(userEntity.getFollowed());
                userInformationResponse.setFollowers(userEntity.getFollowers());
                userInformationResponse.setGender(userEntity.getGender());
                userInformationResponse.setHaveFollowed(userEntity.getHaveFollowed());
                userInformationResponse.setLikes(userEntity.getLikes());
                userInformationResponse.setNickname(userEntity.getNickname());
                userInformationResponse.setSignature(userEntity.getSignature());
                return userInformationResponse;
            } else {
                return null;
            }
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
