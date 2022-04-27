package com.jigpud.snow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : jigpud
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentResponse {
    private String commentId;
    private String storyId;
    private String authorId;
    private String authorNickname;
    private String authorAvatar;
    private String content;
    private long likes;
    private boolean liked;
    private String replyTo;
    private long commentTime;
}
