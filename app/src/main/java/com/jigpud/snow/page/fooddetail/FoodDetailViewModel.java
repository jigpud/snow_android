package com.jigpud.snow.page.fooddetail;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.FoodEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.food.FoodRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class FoodDetailViewModel extends BaseViewModel {
    public static final long ATTRACTION_LIST_PAGE_SIZE = 10;

    private static final String TAG = "FoodDetailViewModel";

    private final FoodRepository foodRepository;

    private long attractionCurrentPage = 1;

    FoodDetailViewModel(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public LiveData<FoodEntity> getFood(String foodId) {
        return foodRepository.getFood(foodId);
    }

    public LiveData<List<AttractionEntity>> refreshAttractionList(String foodId) {
        Logger.d(TAG, "refresh attraction list");
        attractionCurrentPage = 1;
        return getAttractionList(foodId, attractionCurrentPage);
    }

    public LiveData<List<AttractionEntity>> loadMoreAttractionList(String foodId) {
        long attractionCurrentPage = this.attractionCurrentPage + 1;
        Logger.d(TAG, "load attraction list page %d", attractionCurrentPage);
        return Transformations.map(getAttractionList(foodId, attractionCurrentPage), attractionList -> {
           if (attractionList != null && !attractionList.isEmpty()) {
               this.attractionCurrentPage = attractionCurrentPage;
           }
            return attractionList;
        });
    }

    private LiveData<List<AttractionEntity>> getAttractionList(String foodId, long currentPage) {
        MutableLiveData<List<AttractionEntity>> attractionListLiveData = new MutableLiveData<>();
        Disposable disposable = foodRepository.getAttractionList(foodId, ATTRACTION_LIST_PAGE_SIZE, attractionCurrentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    attractionListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(attractionListLiveData::postValue);
        lifecycle(disposable);
        return attractionListLiveData;
    }
}
