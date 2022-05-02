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
public interface FoodService {
    @POST(PathConstant.GET_FOOD)
    @Multipart
    Observable<ApiResponse<FoodResponse>> getFood(
            @Part(FormDataConstant.FOOD_ID) String foodId
    );

    @POST(PathConstant.GET_FOOD_LIST)
    @Multipart
    Observable<ApiResponse<PageData<FoodResponse>>> getFoodList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_FOOD_PICTURE_LIST)
    @Multipart
    Observable<ApiResponse<PageData<FoodPictureResponse>>> getPictureList(
            @Part(FormDataConstant.FOOD_ID) String foodId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_FOOD_ATTRACTION_LIST)
    @Multipart
    Observable<ApiResponse<PageData<AttractionResponse>>> getAttractionList(
            @Part(FormDataConstant.FOOD_ID) String foodId,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.UPLOAD_FOOD_PICTURE)
    @Multipart
    Observable<ApiResponseStatus> uploadPicture(
            @Part(FormDataConstant.FOOD_ID) String foodId,
            @Part(FormDataConstant.PICTURE) String picture
    );

    @POST(PathConstant.DELETE_FOOD_PICTURE)
    @Multipart
    Observable<ApiResponseStatus> deletePicture(
            @Part(FormDataConstant.FOOD_ID) String foodId,
            @Part(FormDataConstant.PICTURE) String picture
    );
}
