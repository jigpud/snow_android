package com.jigpud.snow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jigpud
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoryListResponse {
    private String storyId;
    private String authorId;
    private String title;
    private String content;
    private Long likes;
    private List<String> pictures;
    private Long releaseTime;
    private String releaseLocation;
    private Boolean liked;
    private String attractionId;
}
