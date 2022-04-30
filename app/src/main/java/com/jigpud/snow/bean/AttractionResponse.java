package com.jigpud.snow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : jigpud
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttractionResponse {
    private String attractionId;
    private String name;
    private String description;
    private List<String> photos;
    private List<String> tags;
    private String location;
    private float score;
    private long scoreCount;
    private boolean followed;
    private long storyCount;
    private int myScore;
    private long followers;
}
