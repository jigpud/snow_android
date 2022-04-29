package com.jigpud.snow.page.attractiondetail;

import android.util.Log;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.attraction.AttractionRepository;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class AttractionDetailViewModel extends BaseViewModel {
    public static final int STORY_LIST_PAGE_SIZE = 10;

    private static final String TAG = "AttractionDetailViewModel";

    private final AttractionRepository attractionRepository;
    private final StoryRepository storyRepository;

    private int currentAttractionStoryListPage = 1;

    AttractionDetailViewModel(AttractionRepository attractionRepository, StoryRepository storyRepository) {
        this.attractionRepository = attractionRepository;
        this.storyRepository = storyRepository;
    }

    public LiveData<AttractionEntity> getAttraction(String attractionId) {
        return attractionRepository.getAttraction(attractionId);
    }

    public LiveData<Pair<Boolean, String>> followAttraction(String attractionId) {
        MutableLiveData<Pair<Boolean, String>> followAttractionStatusLiveData = new MutableLiveData<>();
        Disposable disposable = attractionRepository.followAttraction(attractionId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.d(TAG, Log.getStackTraceString(throwable));
                    followAttractionStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(followAttractionStatusLiveData::postValue);
        lifecycle(disposable);
        return followAttractionStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unfollowAttraction(String attractionId) {
        MutableLiveData<Pair<Boolean, String>> unfollowAttractionStatusLiveData = new MutableLiveData<>();
        Disposable disposable = attractionRepository.unfollowAttraction(attractionId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.d(TAG, Log.getStackTraceString(throwable));
                    unfollowAttractionStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(unfollowAttractionStatusLiveData::postValue);
        lifecycle(disposable);
        return unfollowAttractionStatusLiveData;
    }

    public LiveData<List<StoryEntity>> refreshAttractionStoryList(String attractionId) {
        Logger.d(TAG, "refresh attraction story list");
        currentAttractionStoryListPage = 1;
        return getAttractionStoryList(attractionId, currentAttractionStoryListPage);
    }

    public LiveData<List<StoryEntity>> moreAttractionStoryList(String attractionId) {
        int currentAttractionStoryListPage = this.currentAttractionStoryListPage + 1;
        Logger.d(TAG, "load attraction story page %d", currentAttractionStoryListPage);
        return Transformations.map(getAttractionStoryList(attractionId, currentAttractionStoryListPage), attractionStoryList -> {
            if (attractionStoryList != null && !attractionStoryList.isEmpty()) {
                this.currentAttractionStoryListPage = currentAttractionStoryListPage;
            }
            return attractionStoryList;
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

    public LiveData<StoryEntity> getStory(String storyId) {
        return storyRepository.getStory(storyId);
    }

    public LiveData<Pair<Boolean, String>> scoreAttraction(String attractionId, int score) {
        MutableLiveData<Pair<Boolean, String>> scoreAttractionStatusLiveData = new MutableLiveData<>();
        Disposable disposable = attractionRepository.scoreAttraction(attractionId, score)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    scoreAttractionStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(scoreAttractionStatusLiveData::postValue);
        lifecycle(disposable);
        return scoreAttractionStatusLiveData;
    }

    private LiveData<List<StoryEntity>> getAttractionStoryList(String attractionId, long currentPage) {
        MutableLiveData<List<StoryEntity>> attractionStoryListLiveData = new MutableLiveData<>();
        Disposable disposable = attractionRepository.getAttractionStoryList(attractionId, STORY_LIST_PAGE_SIZE, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    attractionStoryListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(attractionStoryListLiveData::postValue);
        lifecycle(disposable);
        return attractionStoryListLiveData;
    }
}
