package com.jigpud.snow.page.mine;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
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
 * @author jigpud
 */
public class MineViewModel extends BaseViewModel {
    private static final String TAG = "MineViewModel";

    public static final long MY_STORY_LIST_PAGE_SIZE = 10;

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;

    private long currentPage = 1;

    public MineViewModel(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }

    public LiveData<UserEntity> getMyProfile() {
        return userRepository.getSelfInfo();
    }

    public LiveData<List<StoryEntity>> refreshMyStoryList() {
        Logger.d(TAG, "refresh my story list");
        currentPage = 1;
        return getMyStoryList(currentPage);
    }

    public LiveData<List<StoryEntity>> moreStoryList() {
        long currentPage = this.currentPage + 1;
        Logger.d(TAG, "load page %d", currentPage);
        return Transformations.map(getMyStoryList(currentPage), storyList -> {
           if (storyList != null && !storyList.isEmpty()) {
               this.currentPage++;
           }
           return storyList;
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

    private LiveData<List<StoryEntity>> getMyStoryList(long currentPage) {
        MutableLiveData<List<StoryEntity>> myStoryListLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getMyStoryList(MY_STORY_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> myStoryListLiveData.postValue(new ArrayList<>()))
                .subscribe(myStoryListLiveData::postValue);
        lifecycle(disposable);
        return myStoryListLiveData;
    }
}
