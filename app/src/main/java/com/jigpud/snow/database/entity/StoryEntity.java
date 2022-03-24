package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

import java.util.List;

/**
 * @author jigpud
 */
@Entity(tableName = "story", indices = {
        @Index(value = "story_id", unique = true)
})
@Data
public class StoryEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "story_id")
    private String storyId;
    @ColumnInfo(name = "author_id")
    private String authorId;
    private String title;
    private String content;
    private Long likes;
    private List<String> pictures;
    @ColumnInfo(name = "release_time")
    private Long releaseTime;
    @ColumnInfo(name = "release_location")
    private String releaseLocation;
    private Boolean liked;
    @ColumnInfo(name = "attraction_id")
    private String attractionId;
}
