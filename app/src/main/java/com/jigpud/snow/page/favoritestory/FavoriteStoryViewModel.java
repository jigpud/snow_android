package com.jigpud.snow.page.favoritestory;

import android.util.Log;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class FavoriteStoryViewModel extends BaseViewModel {
    public static final long FAVORITE_STORY_LIST_SIZE = 10;

    private static final String TAG = "FavoriteStoryViewModel";

    private final StoryRepository storyRepository;

    private long favoriteStoryCurrentPage = 1;

    FavoriteStoryViewModel(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public LiveData<List<StoryEntity>> refreshMyFavoriteStoryList() {
        Logger.d(TAG, "refresh my favorite story list");
        favoriteStoryCurrentPage = 1;
        return getMyFavoriteStoryList(favoriteStoryCurrentPage);
    }

    public LiveData<List<StoryEntity>> loadMoreMyFavoriteStoryList() {
        long favoriteStoryCurrentPage = this.favoriteStoryCurrentPage + 1;
        Logger.d(TAG, "load my favorite story list page %d", favoriteStoryCurrentPage);
        return Transformations.map(getMyFavoriteStoryList(favoriteStoryCurrentPage), favoriteStoryList -> {
            if (favoriteStoryList != null && !favoriteStoryList.isEmpty()) {
                this.favoriteStoryCurrentPage = favoriteStoryCurrentPage;
            }
            return favoriteStoryList;
        });
    }

    public LiveData<Pair<Boolean, String>> likeStory(String storyId) {
        MutableLiveData<Pair<Boolean, String>> likeStoryStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.likeStory(storyId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    likeStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(likeStoryStatusLiveData::postValue);
        lifecycle(disposable);
        return likeStoryStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unlikeStory(String storyId) {
        MutableLiveData<Pair<Boolean, String>> unlikeStoryStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.unlikeStory(storyId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    unlikeStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(unlikeStoryStatusLiveData::postValue);
        lifecycle(disposable);
        return unlikeStoryStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unFavoriteStory(String storyId) {
        MutableLiveData<Pair<Boolean, String>> unFavoriteStoryStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.unFavoriteStory(storyId)
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    unFavoriteStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(unFavoriteStoryStatusLiveData::postValue);
        lifecycle(disposable);
        return unFavoriteStoryStatusLiveData;
    }

    public LiveData<StoryEntity> getStory(String storyId) {
        return storyRepository.getStory(storyId);
    }

    private LiveData<List<StoryEntity>> getMyFavoriteStoryList(long currentPage) {
        MutableLiveData<List<StoryEntity>> favoriteStoryListLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getSelfFavoriteStoryList(FAVORITE_STORY_LIST_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    favoriteStoryListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(favoriteStoryListLiveData::postValue);
        lifecycle(disposable);
        return favoriteStoryListLiveData;
    }
}
