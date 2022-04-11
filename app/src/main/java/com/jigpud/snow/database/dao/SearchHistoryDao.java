package com.jigpud.snow.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jigpud.snow.database.entity.SearchHistoryEntity;

import java.util.List;

/**
 * @author : jigpud
 */
@Dao
public interface SearchHistoryDao {
    @Query("SELECT * FROM search_history WHERE userid=:userid ORDER BY search_time DESC")
    LiveData<List<SearchHistoryEntity>> getSearchHistory(String userid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistoryEntity searchHistoryEntity);

    @Query("DELETE FROM search_history WHERE key_words=:keyWords")
    void delete(String keyWords);

    @Query("DELETE FROM search_history WHERE userid=:userid")
    void clearAll(String userid);
}
