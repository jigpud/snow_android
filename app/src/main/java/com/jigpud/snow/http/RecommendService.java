package com.jigpud.snow.http;

import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.AttractionResponse;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.util.constant.FormDataConstant;
import com.jigpud.snow.util.constant.PathConstant;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import java.util.List;

/**
 * @author : jigpud
 */
public interface RecommendService {
    @GET(PathConstant.GET_HOT_ATTRACTION_LIST)
    Observable<ApiResponse<List<AttractionResponse>>> getHotAttractionList();

    @POST(PathConstant.GET_RECOMMEND_ATTRACTION_LIST)
    @Multipart
    Observable<ApiResponse<PageData<AttractionResponse>>> getRecommendAttractionList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.GET_RECOMMEND_USER_LIST)
    @Multipart
    Observable<ApiResponse<PageData<UserInformationResponse>>> getRecommendUserList(
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );
}
