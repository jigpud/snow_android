package com.jigpud.snow.page.morefood;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
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
public class MoreFoodViewModel extends BaseViewModel {
    public static final long FOOD_LIST_PAGE_SIZE = 10;

    private static final String TAG = "MoreFoodViewModel";

    private final FoodRepository foodRepository;

    private long foodCurrentPage = 1;

    MoreFoodViewModel(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public LiveData<List<FoodEntity>> refreshFoodList() {
        Logger.d(TAG, "refresh food list");
        foodCurrentPage = 1;
        return getFoodList(foodCurrentPage);
    }

    public LiveData<List<FoodEntity>> loadMoreFoodList() {
        long foodCurrentPage = this.foodCurrentPage + 1;
        Logger.d(TAG, "load food list page %d", foodCurrentPage);
        return Transformations.map(getFoodList(foodCurrentPage), foodList -> {
           if (foodList != null && !foodList.isEmpty()) {
               this.foodCurrentPage = foodCurrentPage;
           }
            return foodList;
        });
    }

    private LiveData<List<FoodEntity>> getFoodList(long currentPage) {
        MutableLiveData<List<FoodEntity>> foodListLiveData = new MutableLiveData<>();
        Disposable disposable = foodRepository.getFoodList(FOOD_LIST_PAGE_SIZE, currentPage)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> {
                    Logger.e(TAG, Log.getStackTraceString(throwable));
                    foodListLiveData.postValue(new ArrayList<>());
                })
                .subscribe(foodListLiveData::postValue);
        lifecycle(disposable);
        return foodListLiveData;
    }
}
