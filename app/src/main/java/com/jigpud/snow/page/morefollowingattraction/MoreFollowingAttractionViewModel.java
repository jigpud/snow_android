package com.jigpud.snow.page.morefollowingattraction;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.attraction.AttractionRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class MoreFollowingAttractionViewModel extends BaseViewModel {
    public static final long FOLLOWING_ATTRACTION_LIST_PAGE_SIZE = 10;

    private static final String TAG = "FollowingAttractionViewModel";

    private final AttractionRepository attractionRepository;

    private long followingAttractionListCurrentPage = 1;

    MoreFollowingAttractionViewModel(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    public LiveData<List<AttractionEntity>> refreshFollowingAttractionList() {
        Logger.d(TAG, "refresh following attraction list");
        followingAttractionListCurrentPage = 1;
        return getFollowingAttractionList(followingAttractionListCurrentPage);
    }

    public LiveData<List<AttractionEntity>> loadMoreFollowingAttractionList() {
        long followingAttractionListCurrentPage = this.followingAttractionListCurrentPage + 1;
        Logger.d(TAG, "load following attraction ist page %d", followingAttractionListCurrentPage);
        return Transformations.map(getFollowingAttractionList(followingAttractionListCurrentPage), followingAttractionList -> {
            if (followingAttractionList != null && !followingAttractionList.isEmpty()) {
                this.followingAttractionListCurrentPage =  followingAttractionListCurrentPage;
            }
            return followingAttractionList;
        });
    }

    private LiveData<List<AttractionEntity>> getFollowingAttractionList(long currentPage) {
        MutableLiveData<List<AttractionEntity>> followingAttractionListLiveData = new MutableLiveData<>();
        Disposable disposable = attractionRepository.getFollowingList(FOLLOWING_ATTRACTION_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    followingAttractionListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(followingAttractionListLiveData::postValue);
        lifecycle(disposable);
        return followingAttractionListLiveData;
    }
}
