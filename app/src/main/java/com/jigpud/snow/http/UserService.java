package com.jigpud.snow.http;

import com.jigpud.snow.bean.*;
import com.jigpud.snow.util.constant.FormDataConstant;
import com.jigpud.snow.util.constant.PathConstant;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author jigpud
 */
public interface UserService {
    @POST(PathConstant.LOGIN_WITH_PASSWORD)
    @Multipart
    Observable<ApiResponse<LoginResponse>> loginByPassword(
            @Part(FormDataConstant.USERNAME) String username,
            @Part(FormDataConstant.PASSWORD) String password
    );

    @POST(PathConstant.LOGIN_WITH_VERIFICATION_CODE)
    @Multipart
    Observable<ApiResponse<LoginResponse>> loginByVerificationCode(
            @Part(FormDataConstant.USERNAME) String username,
            @Part(FormDataConstant.VERIFICATION_CODE) String verificationCode
    );

    @POST(PathConstant.USER_INFO)
    @Multipart
    Observable<ApiResponse<UserInformationResponse>> getUserInfo(
            @Part(FormDataConstant.USERNAME) String userid
    );

    @GET(PathConstant.USER_INFO)
    Observable<ApiResponse<SelfInformationResponse>> getSelfInfo();

    @POST(PathConstant.REGISTER)
    @Multipart
    Observable<ApiResponseStatus> register(
            @Part(FormDataConstant.USERNAME) String username,
            @Part(FormDataConstant.PASSWORD) String password,
            @Part(FormDataConstant.VERIFICATION_CODE) String verificationCode
    );

    @POST(PathConstant.RETRIEVE_PASSWORD)
    @Multipart
    Observable<ApiResponseStatus> retrievePassword(
            @Part(FormDataConstant.USERNAME) String username,
            @Part(FormDataConstant.PASSWORD) String password,
            @Part(FormDataConstant.VERIFICATION_CODE) String verificationCode
    );
}
