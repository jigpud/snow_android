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
public class ReleaseStoryRequest {
    private String title;
    private String content;
    private List<String> pictures;
    private long releaseTime;
    private String attractionId;
}
