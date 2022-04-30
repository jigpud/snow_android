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
public interface SearchService {
    @POST(PathConstant.SEARCH_STORY)
    @Multipart
    Observable<ApiResponse<PageData<StoryResponse>>> searchStory(
            @Part(FormDataConstant.KEY_WORDS) String keyWords,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.SEARCH_USER)
    @Multipart
    Observable<ApiResponse<PageData<UserInformationResponse>>> searchUser(
            @Part(FormDataConstant.KEY_WORDS) String keyWords,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );

    @POST(PathConstant.SEARCH_ATTRACTION)
    @Multipart
    Observable<ApiResponse<PageData<AttractionResponse>>> searchAttraction(
            @Part(FormDataConstant.KEY_WORDS) String keyWords,
            @Part(FormDataConstant.PAGE_SIZE) long pageSize,
            @Part(FormDataConstant.CURRENT_PAGE) long currentPage
    );
}
