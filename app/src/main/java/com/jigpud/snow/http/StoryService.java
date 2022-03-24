package com.jigpud.snow.http;

import com.jigpud.snow.bean.ApiResponse;
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
    @POST(PathConstant.GET_STORY_LIST)
    @Multipart
    Observable<ApiResponse<StoryListResponse>> getStoryList(
            @Part(FormDataConstant.PAGE_COUNT) long pageCount,
            @Part(FormDataConstant.PAGE) long page
    );
}
