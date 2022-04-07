package com.jigpud.snow.util.network;

import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.ApiResponseStatus;
import com.jigpud.snow.bean.RefreshTokenResponse;
import com.jigpud.snow.event.OnLoginExpiredEvent;
import com.jigpud.snow.util.constant.*;
import com.jigpud.snow.util.json.JsonUtil;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;
import okhttp3.*;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author jigpud
 * 自动添加token与token刷新
 */
public class TokenInterceptor implements Interceptor {
    private static final String TAG = "TokenInterceptor";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request generalRequest = chain.request();
        Request newRequest = addTokenIfNeed(generalRequest);
        Response generalResponse = chain.proceed(newRequest);
        return refreshTokenIfNeed(chain, generalRequest, generalResponse);
    }

    private Request addTokenIfNeed(Request generalRequest) {
        if (isNeedInterceptToken(generalRequest)) {
            return generalRequest.newBuilder()
                    .addHeader(HeaderConstant.AUTHORIZATION, getToken())
                    .build();
        }
        return generalRequest;
    }

    private Response refreshTokenIfNeed(Chain chain, Request generalRequest, Response generalResponse) {
        try {
            if (isNeedInterceptToken(generalRequest)) {
                ResponseBody generalResponseBody = generalResponse.body();
                if (generalResponseBody != null) {
                    generalResponseBody.source().request(Long.MAX_VALUE);
                    String generalResponseString = generalResponseBody.source()
                            .getBuffer()
                            .clone()
                            .readString(StandardCharsets.UTF_8);
                    ApiResponseStatus generalStatus = JsonUtil.fromJson(generalResponseString, ApiResponseStatus.class);
                    if (generalStatus.getCode() == ResponseCodeConstant.UNAUTHORIZED) {
                        // token过期
                        Logger.d(TAG, "token expired!");
                        String newToken = refreshToken();
                        if (newToken.isEmpty()) {
                            // token刷新失败
                            Logger.d(TAG, "token refresh failed!");
                            postLoginExpired();
                        }
                        Request newRequest = generalRequest.newBuilder()
                                .addHeader(HeaderConstant.AUTHORIZATION, newToken)
                                .build();
                        return chain.proceed(newRequest);
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, Log.getStackTraceString(e));
        }
        return generalResponse;
    }

    private boolean isNeedInterceptToken(Request request) {
        return true;
    }

    private String getToken() {
        CurrentUser currentUser = CurrentUser.getInstance(SnowApplication.getAppContext());
        String token = currentUser.getToken();
        if (token == null) {
            return "";
        } else {
            return token;
        }
    }

    @SuppressWarnings("unchecked")
    private String refreshToken() {
        Logger.d(TAG, "refresh token!");
        CurrentUser currentUser = CurrentUser.getInstance(SnowApplication.getAppContext());
        String refreshToken = currentUser.getRefreshToken();
        String url = URLConstant.getURL() + PathConstant.REFRESH_TOKEN;
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart(FormDataConstant.REFRESH_TOKEN, refreshToken)
                .build();
        Request refreshTokenRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response refreshTokenResponse = new OkHttpClient().newCall(refreshTokenRequest).execute();
            ResponseBody refreshTokenResponseBody = refreshTokenResponse.body();
            if (refreshTokenResponseBody != null) {
                Type type = new TypeToken<ApiResponse<RefreshTokenResponse>>() {}.getType();
                ApiResponse<RefreshTokenResponse> refreshResponse =
                        JsonUtil.fromJson(refreshTokenResponseBody.string(), type);
                if (refreshResponse.isSuccess()) {
                    String newToken = refreshResponse.getData().getToken();
                    if (newToken != null) {
                        // token刷新成功
                        Logger.d(TAG, "token refresh success!");
                        currentUser.updateToken(newToken);
                        return newToken;
                    }
                }
            }
        } catch (IOException e) {
            Logger.e(TAG, "refresh token failed!\n%s", Log.getStackTraceString(e));
        }
        return "";
    }

    private void postLoginExpired() {
        EventBus.getDefault().postSticky(new OnLoginExpiredEvent());
    }
}
