package com.jigpud.snow.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jigpud.snow.database.entity.StoryEntity;

import java.util.List;

/**
 * @author : jigpud
 */
@Dao
public interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoryEntity story);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StoryEntity> stories);

    @Query("DELETE FROM story WHERE story_id=:storyId")
    void delete(String storyId);

    @Query("SELECT * FROM story")
    List<StoryEntity> getStoryList();

    @Query("SELECT * FROM story WHERE story_id=:storyId")
    LiveData<StoryEntity> getStory(String storyId);
}
