package com.jigpud.snow.repository.search;

import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.StoryResponse;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
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

    Observable<List<StoryResponse>> searchStory(String keyWords, long pageCount, long page);

    Observable<List<UserInformationResponse>> searchUser(String keyWords, long pageCount, long page);
}
