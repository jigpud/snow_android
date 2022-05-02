package com.jigpud.snow.repository.base;

import androidx.core.util.Pair;
import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.ApiResponseStatus;
import com.jigpud.snow.bean.PageData;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public abstract class BaseRepository {
    protected <T> Observable<T> withIO(Observable<T> source) {
        return source
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    protected <T> List<T> handleListResponse(ApiResponse<PageData<T>> apiResponse) {
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            return apiResponse.getData().getRecords();
        }
        return new ArrayList<>();
    }

    protected Pair<Boolean, String> handleResponseStatus(ApiResponseStatus apiResponseStatus) {
        return new Pair<>(apiResponseStatus.isSuccess(), apiResponseStatus.getMessage());
    }
}
