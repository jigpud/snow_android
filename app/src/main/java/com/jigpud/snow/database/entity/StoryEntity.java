package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.jigpud.snow.bean.StoryResponse;
import lombok.Data;

import java.util.List;

/**
 * @author : jigpud
 */
@Entity(tableName = "story", indices = {
        @Index(value = "story_id", unique = true)
})
@Data
public class StoryEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "story_id")
    private String storyId;

    @ColumnInfo(name = "author_id")
    private String authorId;

    @ColumnInfo(name = "author_nickname")
    private String authorNickname;

    @ColumnInfo(name = "author_avatar")
    private String authorAvatar;

    private String title;

    private String content;

    private long likes;

    private List<String> pictures;

    @ColumnInfo(name = "release_time")
    private long releaseTime;

    @ColumnInfo(name = "release_location")
    private String releaseLocation;

    private boolean liked;

    @ColumnInfo(name = "attraction_id")
    private String attractionId;

    @ColumnInfo(name = "have_favorite")
    private boolean haveFavorite;

    private long favorites;

    public static StoryEntity create(StoryResponse storyResponse) {
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setStoryId(storyResponse.getStoryId());
        storyEntity.setAuthorId(storyResponse.getAuthorId());
        storyEntity.setAuthorNickname(storyResponse.getAuthorNickname());
        storyEntity.setAuthorAvatar(storyResponse.getAuthorAvatar());
        storyEntity.setPictures(storyResponse.getPictures());
        storyEntity.setTitle(storyResponse.getTitle());
        storyEntity.setContent(storyResponse.getContent());
        storyEntity.setReleaseTime(storyResponse.getReleaseTime());
        storyEntity.setReleaseLocation(storyResponse.getReleaseLocation());
        storyEntity.setLikes(storyResponse.getLikes());
        storyEntity.setLiked(storyResponse.isLiked());
        storyEntity.setAttractionId(storyResponse.getAttractionId());
        storyEntity.setHaveFavorite(storyResponse.isHaveFavorite());
        storyEntity.setFavorites(storyResponse.getFavorites());
        return storyEntity;
    }
}
