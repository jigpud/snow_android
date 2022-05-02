package com.jigpud.snow.page.main;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.FoodEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.food.FoodRepository;
import com.jigpud.snow.repository.recommend.RecommendRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class MainViewModel extends BaseViewModel {
    public static final long RECOMMEND_ATTRACTION_LIST_PAGE_SIZE = 10;
    public static final long RECOMMEND_ATTRACTION_LIST_CURRENT_PAGE = 1;
    public static final long FOOD_LIST_PAGE_SIZE = 10;
    public static final long FOOD_LIST_CURRENT_PAGE = 1;

    private static final String TAG = "MineViewModel";

    private final RecommendRepository recommendRepository;
    private final FoodRepository foodRepository;

    MainViewModel(RecommendRepository recommendRepository, FoodRepository foodRepository) {
        this.recommendRepository = recommendRepository;
        this.foodRepository = foodRepository;
    }

    public LiveData<List<AttractionEntity>> getHotAttractionList() {
        MutableLiveData<List<AttractionEntity>> hotAttractionListLiveData = new MutableLiveData<>();
        Disposable disposable = recommendRepository.getHotAttractionList()
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.d(TAG, Log.getStackTraceString(throwable));
                    hotAttractionListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(hotAttractionListLiveData::postValue);
        lifecycle(disposable);
        return hotAttractionListLiveData;
    }

    public LiveData<List<FoodEntity>> getFoodList() {
        MutableLiveData<List<FoodEntity>> foodListLiveData = new MutableLiveData<>();
        Disposable disposable = foodRepository.getFoodList(FOOD_LIST_PAGE_SIZE, FOOD_LIST_CURRENT_PAGE)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    foodListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(foodListLiveData::postValue);
        lifecycle(disposable);
        return foodListLiveData;
    }

    public LiveData<List<AttractionEntity>> getRecommendAttractionList() {
        MutableLiveData<List<AttractionEntity>> recommendAttractionListLiveData = new MutableLiveData<>();
        Disposable disposable = recommendRepository.getRecommendAttractionList(
                RECOMMEND_ATTRACTION_LIST_PAGE_SIZE, RECOMMEND_ATTRACTION_LIST_CURRENT_PAGE)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.d(TAG, Log.getStackTraceString(throwable));
                    recommendAttractionListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(recommendAttractionListLiveData::postValue);
        lifecycle(disposable);
        return recommendAttractionListLiveData;
    }
}
