package com.jigpud.snow.http;

import androidx.annotation.NonNull;
import com.jigpud.snow.bean.ApiResponseStatus;
import com.jigpud.snow.util.constant.FormDataConstant;
import com.jigpud.snow.util.constant.PathConstant;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author : jigpud
 */
public interface VerificationCodeService {
    @POST(PathConstant.GET_VERIFICATION_CODE)
    @Multipart
    Observable<ApiResponseStatus> sendVerificationCode(
            @Part(FormDataConstant.USERNAME) String username,
            @Part(FormDataConstant.VERIFICATION_CODE_TARGET) String verificationCodeTarget
    );

    @POST(PathConstant.GET_VERIFICATION_CODE)
    @Multipart
    Observable<ApiResponseStatus> sendVerificationCode(
            @Part(FormDataConstant.USERNAME) String username
    );

    enum VerificationCodeTarget {
        LOGIN("login"),
        REGISTER("register"),
        RETRIEVE_PASSWORD("retrieve_password");

        private final String target;

        VerificationCodeTarget(String target) {
            this.target = target;
        }


        @NonNull
        @NotNull
        @Override
        public String toString() {
            return target;
        }
    }
}
