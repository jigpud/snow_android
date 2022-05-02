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

    @POST(PathConstant.GET_ATTRACTION_PICTURE_LIST)
    @Multipart
    Observable<ApiResponse<PageData<AttractionPictureResponse>>> getAttractionPhotoList(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.UPLOAD_ATTRACTION_PICTURE)
    @Multipart
    Observable<ApiResponseStatus> uploadPicture(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PICTURE) String picture
    );

    @POST(PathConstant.DELETE_ATTRACTION_PICTURE)
    @Multipart
    Observable<ApiResponseStatus> deletePicture(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PICTURE) String picture
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

    @POST(PathConstant.FOLLOWING_ATTRACTION_LIST)
    @Multipart
    Observable<ApiResponse<PageData<AttractionResponse>>> getFollowingAttractionList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_ATTRACTION_FOOD_LIST)
    @Multipart
    Observable<ApiResponse<PageData<FoodResponse>>> getFoodList(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_ATTRACTION_PICTURE_LIST)
    @Multipart
    Observable<ApiResponse<PageData<AttractionPictureResponse>>> getPictureList(
            @Part(FormDataConstant.ATTRACTION_ID) String attractionId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );
}
