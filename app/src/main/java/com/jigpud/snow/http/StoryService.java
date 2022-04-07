package com.jigpud.snow.http;

import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.StoryListResponse;
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
    Observable<ApiResponse<PageData<StoryListResponse>>> getSelfStoryList(
            @Part(FormDataConstant.PAGE_COUNT) long pageCount,
            @Part(FormDataConstant.PAGE) long page
    );

    @POST(PathConstant.GET_USER_STORY_LIST)
    @Multipart
    Observable<ApiResponse<PageData<StoryListResponse>>> getUserStoryList(
            @Part(FormDataConstant.USERID) String userid,
            @Part(FormDataConstant.PAGE_COUNT) long pageCount,
            @Part(FormDataConstant.PAGE) long page
    );
}
