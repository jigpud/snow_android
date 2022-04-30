package com.jigpud.snow.page.moments;

import android.util.Log;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.attraction.AttractionRepository;
import com.jigpud.snow.repository.recommend.RecommendRepository;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class MomentsViewModel extends BaseViewModel {
    public static final long RECOMMEND_USER_LIST_PAGE_SIZE = 10;
    public static final long RECOMMEND_USER_LIST_CURRENT_PAGE = 1;
    public static final long FOLLOWING_ATTRACTION_LIST_PAGES_SIZE = 10;
    public static final long FOLLOWING_ATTRACTION_LIST_CURRENT_PAGE = 1;
    public static final long MOMENTS_STORY_LIST_PAGE_SIZE = 10;

    private static final String TAG = "MomentsViewModel";

    private final RecommendRepository recommendRepository;
    private final AttractionRepository attractionRepository;
    private final StoryRepository storyRepository;

    private long momentsStoryListCurrentPage = 1;

    MomentsViewModel(
            RecommendRepository recommendRepository,
            AttractionRepository attractionRepository,
            StoryRepository storyRepository
    ) {
        this.recommendRepository = recommendRepository;
        this.attractionRepository = attractionRepository;
        this.storyRepository = storyRepository;
    }

    public LiveData<List<UserEntity>> getRecommendUserList() {
        MutableLiveData<List<UserEntity>> recommendUserListLiveData = new MutableLiveData<>();
        Disposable disposable = recommendRepository.getRecommendUserList(
                RECOMMEND_USER_LIST_PAGE_SIZE, RECOMMEND_USER_LIST_CURRENT_PAGE)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    recommendUserListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(recommendUserListLiveData::postValue);
        lifecycle(disposable);
        return recommendUserListLiveData;
    }

    public LiveData<List<AttractionEntity>> getFollowingAttractionList() {
        MutableLiveData<List<AttractionEntity>> followingAttractionListLiveData = new MutableLiveData<>();
        Disposable disposable = attractionRepository.getFollowingAttractionList(
                FOLLOWING_ATTRACTION_LIST_PAGES_SIZE, FOLLOWING_ATTRACTION_LIST_CURRENT_PAGE)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    followingAttractionListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(followingAttractionListLiveData::postValue);
        lifecycle(disposable);
        return followingAttractionListLiveData;
    }

    public LiveData<List<StoryEntity>> refreshMomentsStoryList() {
        Logger.d(TAG, "refresh moments story list");
        momentsStoryListCurrentPage = 1;
        return getMomentsStoryList(momentsStoryListCurrentPage);
    }

    public LiveData<List<StoryEntity>> loadMoreMomentsStoryList() {
        long momentsStoryListCurrentPage = this.momentsStoryListCurrentPage + 1;
        Logger.d(TAG, "load moments story list page %d", momentsStoryListCurrentPage);
        return Transformations.map(getMomentsStoryList(momentsStoryListCurrentPage), momentsStoryList -> {
            if (momentsStoryList != null && !momentsStoryList.isEmpty()) {
                this.momentsStoryListCurrentPage = momentsStoryListCurrentPage;
            }
            return momentsStoryList;
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

    private LiveData<List<StoryEntity>> getMomentsStoryList(long currentPage) {
        MutableLiveData<List<StoryEntity>> commentsStoryListLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getMomentsStoryList(MOMENTS_STORY_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    commentsStoryListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(commentsStoryListLiveData::postValue);
        lifecycle(disposable);
        return commentsStoryListLiveData;
    }
}
