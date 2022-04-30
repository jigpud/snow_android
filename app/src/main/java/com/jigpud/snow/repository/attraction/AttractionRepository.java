package com.jigpud.snow.repository.attraction;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author : jigpud
 */
public interface AttractionRepository {
    LiveData<AttractionEntity> getAttraction(String attractionId);

    Observable<List<StoryEntity>> getAttractionStoryList(String attractionId, long pageSize, long currentPage);

    Observable<Pair<Boolean, String>> followAttraction(String attractionId);

    Observable<Pair<Boolean, String>> unfollowAttraction(String attractionId);

    Observable<Pair<Boolean, String>> scoreAttraction(String attractionId, int score);

    Observable<Pair<Boolean, String>> uploadPhoto(String attractionId, String photo);

    Observable<Pair<Boolean, String>> deletePhoto(String attractionId, String photo);

    Observable<List<AttractionEntity>> getFollowingAttractionList(long pageSize, long currentPage);
}
