package com.jigpud.snow.page.morerecommendattraction;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.recommend.RecommendRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class MoreRecommendAttractionViewModel extends BaseViewModel {
    public static final long RECOMMEND_ATTRACTION_LIST_PAGE_SIZE = 10;

    private static final String TAG = "RecommendAttractionViewModel";

    private final RecommendRepository recommendRepository;

    private long recommendAttractionCurrentPage = 1;

    MoreRecommendAttractionViewModel(RecommendRepository recommendRepository) {
        this.recommendRepository = recommendRepository;
    }

    public LiveData<List<AttractionEntity>> refreshRecommendAttractionList() {
        Logger.d(TAG, "refresh recommend attraction list");
        recommendAttractionCurrentPage = 1;
        return getRecommendAttractionList(recommendAttractionCurrentPage);
    }

    public LiveData<List<AttractionEntity>> loadMoreRecommendAttractionList() {
        long recommendAttractionCurrentPage = this.recommendAttractionCurrentPage + 1;
        Logger.d(TAG, "load recommend attraction list page %d", recommendAttractionCurrentPage);
        return Transformations.map(getRecommendAttractionList(recommendAttractionCurrentPage), recommendAttractionList -> {
            if (recommendAttractionList != null && !recommendAttractionList.isEmpty()) {
                this.recommendAttractionCurrentPage = recommendAttractionCurrentPage;
            }
            return recommendAttractionList;
        });
    }

    private LiveData<List<AttractionEntity>> getRecommendAttractionList(long currentPage) {
        MutableLiveData<List<AttractionEntity>> recommendAttractionListLiveData = new MutableLiveData<>();
        Disposable disposable = recommendRepository.getRecommendAttractionList(RECOMMEND_ATTRACTION_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    recommendAttractionListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(recommendAttractionListLiveData::postValue);
        lifecycle(disposable);
        return recommendAttractionListLiveData;
    }
}
