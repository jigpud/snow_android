package com.jigpud.snow.http;

import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.UploadTokenResponse;
import com.jigpud.snow.util.constant.PathConstant;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author : jigpud
 */
public interface QiniuService {
    @GET(PathConstant.GET_AVATAR_UPLOAD_TOKEN)
    Observable<ApiResponse<UploadTokenResponse>> getAvatarUploadToken();

    @GET(PathConstant.GET_ATTRACTION_IMG_UPLOAD_TOKEN)
    Observable<ApiResponse<UploadTokenResponse>> getAttractionImgUploadToken();

    @GET(PathConstant.GET_STORY_IMG_UPLOAD_TOKEN)
    Observable<ApiResponse<UploadTokenResponse>> getStoryImgUploadToken();
}
