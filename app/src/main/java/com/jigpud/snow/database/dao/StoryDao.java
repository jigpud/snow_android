package com.jigpud.snow.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jigpud.snow.database.entity.StoryEntity;

/**
 * @author jigpud
 */
@Dao
public interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoryEntity story);

    @Query("DELETE FROM story WHERE story_id=:storyId")
    void delete(String storyId);
}
