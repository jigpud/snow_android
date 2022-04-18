package com.jigpud.snow.page.userprofile;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.page.base.BaseViewModel;
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
public class UserProfileViewModel extends BaseViewModel {
    private static final String TAG = "UserProfileViewModel";

    public static final long USER_STORY_LIST_PAGE_SIZE = 10;

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;

    private long currentUserStoryListPage = 1;

    public UserProfileViewModel(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }

    public LiveData<UserEntity> getUserInfo(String userid) {
        return userRepository.getUserInfo(userid);
    }

    public LiveData<List<StoryEntity>> userStoryList(String userid) {
        Logger.d(TAG, "refresh user story list");
        currentUserStoryListPage = 1;
        return getUserStoryList(userid, currentUserStoryListPage);
    }

    public LiveData<List<StoryEntity>> moreUserStoryList(String userid) {
        long currentUserStoryListPage = this.currentUserStoryListPage + 1;
        Logger.d(TAG, "load user story list page %d", currentUserStoryListPage);
        return getUserStoryList(userid, currentUserStoryListPage);
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
                .subscribe();
        lifecycle(disposable);
        return unlikeStoryStatusLiveData;
    }

    public LiveData<StoryEntity> getStory(String storyId) {
        return storyRepository.getStory(storyId);
    }

    public LiveData<Pair<Boolean, String>> follow(String userid) {
        MutableLiveData<Pair<Boolean, String>> followStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.follow(userid)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> followStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(followStatusLiveData::postValue);
        lifecycle(disposable);
        return followStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unfollow(String userid) {
        MutableLiveData<Pair<Boolean, String>> unfollowStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.unfollow(userid)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> unfollowStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(unfollowStatusLiveData::postValue);
        lifecycle(disposable);
        return unfollowStatusLiveData;
    }

    private LiveData<List<StoryEntity>> getUserStoryList(String userid, long currentPage) {
        MutableLiveData<List<StoryEntity>> userStoryListLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getUserStoryList(userid, USER_STORY_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> userStoryListLiveData.postValue(new ArrayList<>()))
                .subscribe(userStoryListLiveData::postValue);
        lifecycle(disposable);
        return userStoryListLiveData;
    }
}
