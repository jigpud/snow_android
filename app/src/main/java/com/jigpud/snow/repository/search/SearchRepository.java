package com.jigpud.snow.repository.search;

import androidx.lifecycle.LiveData;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.database.entity.UserEntity;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author : jigpud
 */
public interface SearchRepository {
    void addSearchHistory(String keyWords);

    LiveData<List<SearchHistoryEntity>> getSearchHistory();

    void deleteSearchHistory(String keyWords);

    void clearAllSearchHistory();

    Observable<List<StoryEntity>> searchStory(String keyWords, long pageSize, long currentPage);

    Observable<List<UserEntity>> searchUser(String keyWords, long pageSize, long currentPage);
}
