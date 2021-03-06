package com.jigpud.snow.http;

import com.jigpud.snow.bean.*;
import com.jigpud.snow.util.constant.FormDataConstant;
import com.jigpud.snow.util.constant.PathConstant;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author : jigpud
 */
public interface StoryService {
    @POST(PathConstant.GET_SELF_STORY_LIST)
    @Multipart
    Observable<ApiResponse<PageData<StoryResponse>>> getMyStoryList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_USER_STORY_LIST)
    @Multipart
    Observable<ApiResponse<PageData<StoryResponse>>> getUserStoryList(
            @Part(FormDataConstant.USERID) String userid,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.LIKE_STORY)
    @Multipart
    Observable<ApiResponseStatus> likeStory(
            @Part(FormDataConstant.STORY_ID) String storyId
    );

    @POST(PathConstant.UNLIKE_STORY)
    @Multipart
    Observable<ApiResponseStatus> unlikeStory(
            @Part(FormDataConstant.STORY_ID) String storyId
    );

    @POST(PathConstant.GET_STORY)
    @Multipart
    Observable<ApiResponse<StoryResponse>> getStory(
            @Part(FormDataConstant.STORY_ID) String storyId
    );

    @POST(PathConstant.POST_STORY)
    Observable<ApiResponseStatus> postStory(
            @Body PostStoryRequest story
    );

    @POST(PathConstant.COMMENT_STORY)
    @Multipart
    Observable<ApiResponseStatus> postComment(
            @Part(FormDataConstant.STORY_ID) String storyId,
            @Part(FormDataConstant.CONTENT) String content
    );

    @POST(PathConstant.LIKE_COMMENT)
    @Multipart
    Observable<ApiResponseStatus> likeComment(
            @Part(FormDataConstant.COMMENT_ID) String commentId
    );

    @POST(PathConstant.UNLIKE_COMMENT)
    @Multipart
    Observable<ApiResponseStatus> unlikeComment(
            @Part(FormDataConstant.COMMENT_ID) String commentId
    );

    @POST(PathConstant.STORY_COMMENT_LIST)
    @Multipart
    Observable<ApiResponse<PageData<CommentResponse>>> getCommentList(
            @Part(FormDataConstant.STORY_ID) String storyId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_COMMENT)
    @Multipart
    Observable<ApiResponse<CommentResponse>> getComment(
            @Part(FormDataConstant.COMMENT_ID) String commentId
    );

    @POST(PathConstant.FAVORITE_STORY)
    @Multipart
    Observable<ApiResponseStatus> favoriteStory(
            @Part(FormDataConstant.STORY_ID) String storyId
    );

    @POST(PathConstant.UN_FAVORITE_STORY)
    @Multipart
    Observable<ApiResponseStatus> unFavoriteStory(
            @Part(FormDataConstant.STORY_ID) String storyId
    );

    @POST(PathConstant.GET_MOMENTS_STORY_LIST)
    @Multipart
    Observable<ApiResponse<PageData<StoryResponse>>> getMomentsStoryList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_SELF_FAVORITE_STORY_LIST)
    @Multipart
    Observable<ApiResponse<PageData<StoryResponse>>> getSelfFavoriteStoryList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );
}
