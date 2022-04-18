package com.jigpud.snow.http;

import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.ApiResponseStatus;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.StoryResponse;
import com.jigpud.snow.util.constant.FormDataConstant;
import com.jigpud.snow.util.constant.PathConstant;
import io.reactivex.Observable;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author jigpud
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
}
