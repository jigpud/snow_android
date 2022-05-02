package com.jigpud.snow.repository.attraction;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.AttractionPictureResponse;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.FoodEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author : jigpud
 */
public interface AttractionRepository {
    LiveData<AttractionEntity> getAttraction(String attractionId);

    Observable<List<StoryEntity>> getStoryList(String attractionId, long pageSize, long currentPage);

    Observable<Pair<Boolean, String>> follow(String attractionId);

    Observable<Pair<Boolean, String>> unfollow(String attractionId);

    Observable<Pair<Boolean, String>> score(String attractionId, int score);

    Observable<List<AttractionEntity>> getFollowingList(long pageSize, long currentPage);

    Observable<List<FoodEntity>> getFoodList(String attractionId, long pageSize, long currentPage);

    Observable<List<AttractionPictureResponse>> getPictureList(String attractionId, long pageSize, long currentPage);

    Observable<Pair<Boolean, String>> uploadPicture(String attractionId, String picture);

    Observable<Pair<Boolean, String>> deletePicture(String attractionId, String picture);
}
