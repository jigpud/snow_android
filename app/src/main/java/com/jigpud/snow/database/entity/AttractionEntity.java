package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.jigpud.snow.bean.AttractionResponse;
import lombok.Data;

import java.util.List;

/**
 * @author : jigpud
 */
@Entity(tableName = "attraction", indices = {
        @Index(value = "attraction_id", unique = true)
})
@Data
public class AttractionEntity {
    // 主键
    @PrimaryKey(autoGenerate = true)
    private long id;

    // 景点id
    @ColumnInfo(name = "attraction_id")
    private String attractionId;

    // 名称
    private String name;

    // 描述
    private String description;

    // 照片
    private List<String> pictures;

    // 标签
    private List<String> tags;

    // 地理位置
    private String location;

    // 评分
    private float score;

    // 参与评分的人数
    @ColumnInfo(name = "score_count")
    private long scoreCount;

    // 是否关注
    private boolean followed;

    // 游记数量
    @ColumnInfo(name = "story_count")
    private long storyCount;

    // 我的评分
    @ColumnInfo(name = "my_score")
    private int myScore;

    // 粉丝数量
    private long followers;

    public static AttractionEntity create(AttractionResponse attractionResponse) {
        AttractionEntity attractionEntity = new AttractionEntity();
        attractionEntity.setAttractionId(attractionResponse.getAttractionId());
        attractionEntity.setName(attractionResponse.getName());
        attractionEntity.setDescription(attractionResponse.getDescription());
        attractionEntity.setPictures(attractionResponse.getPictures());
        attractionEntity.setTags(attractionResponse.getTags());
        attractionEntity.setLocation(attractionResponse.getLocation());
        attractionEntity.setScore(attractionResponse.getScore());
        attractionEntity.setScoreCount(attractionResponse.getScoreCount());
        attractionEntity.setFollowed(attractionResponse.isFollowed());
        attractionEntity.setStoryCount(attractionResponse.getStoryCount());
        attractionEntity.setMyScore(attractionResponse.getMyScore());
        attractionEntity.setFollowers(attractionResponse.getFollowers());
        return attractionEntity;
    }
}
