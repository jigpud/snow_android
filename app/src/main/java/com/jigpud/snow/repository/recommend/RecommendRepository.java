package com.jigpud.snow.repository.recommend;

import androidx.lifecycle.LiveData;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.UserEntity;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author : jigpud
 */
public interface RecommendRepository {
    LiveData<List<AttractionEntity>> getHotAttractionListFromLocal();

    Observable<List<AttractionEntity>> getHotAttractionList();

    LiveData<List<AttractionEntity>> getRecommendAttractionListFromLocal(long pageSize, long currentPage);

    Observable<List<AttractionEntity>> getRecommendAttractionList(long pageSize, long currentPage);

    LiveData<List<UserEntity>> getRecommendUserListFromLocal(long pageSize, long currentPage);

    Observable<List<UserEntity>> getRecommendUserList(long pageSize, long currentPage);
}
