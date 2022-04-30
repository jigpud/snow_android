package com.jigpud.snow.http;

import com.jigpud.snow.bean.*;
import com.jigpud.snow.util.constant.FormDataConstant;
import com.jigpud.snow.util.constant.PathConstant;
import io.reactivex.Observable;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author : jigpud
 */
public interface AttractionService {
    @POST(PathConstant.GET_ATTRACTION)
    @Multipart
    Observable<ApiResponse<AttractionResponse>> getAttraction(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId
    );

    @POST(PathConstant.SCORE_ATTRACTION)
    @Multipart
    Observable<ApiResponseStatus> scoreAttraction(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.SCORE) int score
    );

    @POST(PathConstant.GET_ATTRACTION_STORY_LIST)
    @Multipart
    Observable<ApiResponse<PageData<StoryResponse>>> getAttractionStoryList(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_ATTRACTION_PHOTO_LIST)
    @Multipart
    Observable<ApiResponse<PageData<AttractionPhotoResponse>>> getAttractionPhotoList(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.UPLOAD_ATTRACTION_PHOTO)
    @Multipart
    Observable<ApiResponseStatus> uploadAttractionPhoto(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PHOTO) String photo
    );

    @POST(PathConstant.DELETE_ATTRACTION_PHOTO)
    @Multipart
    Observable<ApiResponseStatus> deleteAttractionPhoto(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PHOTO) String photo
    );

    @POST(PathConstant.FOLLOW_ATTRACTION)
    @Multipart
    Observable<ApiResponseStatus> followAttraction(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId
    );

    @POST(PathConstant.UNFOLLOW_ATTRACTION)
    @Multipart
    Observable<ApiResponseStatus> unfollowAttraction(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId
    );

    @POST(PathConstant.FOLLOWED_ATTRACTION_LIST)
    @Multipart
    Observable<ApiResponse<PageData<AttractionResponse>>> getFollowedAttractionList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );
}
