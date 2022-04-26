package com.jigpud.snow.util.network;

import com.jigpud.snow.BuildConfig;
import com.jigpud.snow.util.constant.URLConstant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author : jigpud
 */
public class ApiGenerator {
    private static final Retrofit RETROFIT;
    private static final long DEFAULT_TIMEOUT = 10;

    static {
        String baseURL = URLConstant.getURL();
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder()
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new TokenInterceptor());
        if (BuildConfig.DEBUG) {
            okhttpClientBuilder.addInterceptor(new HttpLoggingInterceptor());
        }
        RETROFIT = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okhttpClientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static <T> T create(Class<T> service) {
        return RETROFIT.create(service);
    }
}
