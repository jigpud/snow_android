package com.jigpud.snow.page.morerecommenduser;

import android.util.Log;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.recommend.RecommendRepository;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * @author : jigpud
 */
public class MoreRecommendUserViewModel extends BaseViewModel {
    public static final long RECOMMEND_USER_LIST_PAGE_SIZE = 10;

    private final RecommendRepository recommendRepository;
    private final UserRepository userRepository;

    private long recommendUserCurrentPage;

    MoreRecommendUserViewModel(RecommendRepository recommendRepository, UserRepository userRepository) {
        this.recommendRepository = recommendRepository;
        this.userRepository = userRepository;
    }

    public LiveData<List<UserEntity>> refreshRecommendUserList() {
        Logger.d(TAG, "refresh recommend user list");
        recommendUserCurrentPage = 1;
        return getRecommendUserList(recommendUserCurrentPage);
    }

    public LiveData<List<UserEntity>> loadMoreRecommendUserList() {
        long recommendUserCurrentPage = this.recommendUserCurrentPage + 1;
        Logger.d(TAG, "load recommend user list page %d", recommendUserCurrentPage);
        return Transformations.map(getRecommendUserList(recommendUserCurrentPage), recommendUserList -> {
            if (recommendUserList != null && !recommendUserList.isEmpty()) {
                this.recommendUserCurrentPage = recommendUserCurrentPage;
            }
            return recommendUserList;
        });
    }

    public LiveData<Pair<Boolean, String>> follow(String userid) {
        MutableLiveData<Pair<Boolean, String>> followStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.follow(userid)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    followStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(followStatusLiveData::postValue);
        lifecycle(disposable);
        return followStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> unfollow(String userid) {
        MutableLiveData<Pair<Boolean, String>> unfollowStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.unfollow(userid)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    unfollowStatusLiveData.postValue(new Pair<>(false, "出错啦！"));
                })
                .subscribe(unfollowStatusLiveData::postValue);
        lifecycle(disposable);
        return unfollowStatusLiveData;
    }

    public LiveData<UserEntity> getUser(String userid) {
        return userRepository.getUserInfo(userid);
    }

    private LiveData<List<UserEntity>> getRecommendUserList(long currentPage) {
        MutableLiveData<List<UserEntity>> recommendUserListLiveData = new MutableLiveData<>();
        Disposable disposable = recommendRepository.getRecommendUserList(RECOMMEND_USER_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    recommendUserListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(recommendUserListLiveData::postValue);
        lifecycle(disposable);
        return recommendUserListLiveData;
    }
}
