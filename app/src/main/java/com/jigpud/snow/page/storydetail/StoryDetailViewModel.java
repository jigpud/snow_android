package com.jigpud.snow.page.storydetail;

import android.util.Log;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.bean.CommentResponse;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * @author : jigpud
 */
public class StoryDetailViewModel extends BaseViewModel {
    public static final long COMMENT_LIST_PAGE_SIZE = 10;

    private static final String TAG = "StoryDetailViewModel";

    private final StoryRepository storyRepository;

    private long currentPage = 1;

    StoryDetailViewModel(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public LiveData<StoryEntity> getStory(String storyId) {
        return storyRepository.getStory(storyId);
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

    public LiveData<Pair<Boolean, String>> postComment(String storyId, String content) {
        MutableLiveData<Pair<Boolean, String>> postCommentStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.postComment(storyId, content)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    postCommentStatusLiveData.postValue(new Pair<>(false, "出錯啦！"));
                })
                .subscribe(postCommentStatusLiveData::postValue);
        lifecycle(disposable);
        return postCommentStatusLiveData;
    }

    public LiveData<CommentResponse> getComment(String commentId) {
        MutableLiveData<CommentResponse> commentLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getComment(commentId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> Logger.e(TAG, Log.getStackTraceString(throwable)))
                .subscribe(commentLiveData::postValue);
        lifecycle(disposable);
        return commentLiveData;
    }

    public LiveData<Pair<Boolean, String>> likeComment(String commentId) {
        MutableLiveData<Pair<Boolean, String>> likeCommentStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.likeComment(commentId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    likeCommentStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(likeCommentStatusLiveData::postValue);
        lifecycle(disposable);
        return likeCommentStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unlikeComment(String commentId) {
        MutableLiveData<Pair<Boolean, String>> unlikeCommentStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.unlikeComment(commentId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    unlikeCommentStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(unlikeCommentStatusLiveData::postValue);
        lifecycle(disposable);
        return unlikeCommentStatusLiveData;
    }

    public LiveData<List<CommentResponse>> refreshCommentList(String storyId) {
        Logger.d(TAG, "refresh comment list");
        currentPage = 1;
        return getCommentList(storyId, currentPage);
    }

    public LiveData<List<CommentResponse>> moreCommentList(String storyId) {
        long currentPage = this.currentPage + 1;
        Logger.d(TAG, "load page %d", currentPage);
        return getCommentList(storyId, currentPage);
    }

    public LiveData<Pair<Boolean, String>> favoriteStory(String storyId) {
        MutableLiveData<Pair<Boolean, String>> favoriteStoryStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.favoriteStory(storyId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    favoriteStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(favoriteStoryStatusLiveData::postValue);
        lifecycle(disposable);
        return favoriteStoryStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unFavoriteStory(String storyId) {
        MutableLiveData<Pair<Boolean, String>> unFavoriteStoryStatusLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.unFavoriteStory(storyId)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    unFavoriteStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(unFavoriteStoryStatusLiveData::postValue);
        lifecycle(disposable);
        return unFavoriteStoryStatusLiveData;
    }

    private LiveData<List<CommentResponse>> getCommentList(String storyId, long currentPage) {
        MutableLiveData<List<CommentResponse>> commentListLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getCommentList(storyId, COMMENT_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> Logger.e(TAG, Log.getStackTraceString(throwable)))
                .subscribe(commentListLiveData::postValue);
        lifecycle(disposable);
        return commentListLiveData;
    }
}
