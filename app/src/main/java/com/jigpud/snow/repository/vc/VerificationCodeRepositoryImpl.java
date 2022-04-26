package com.jigpud.snow.repository.vc;

import androidx.core.util.Pair;
import com.jigpud.snow.http.VerificationCodeService;
import com.jigpud.snow.util.network.ApiGenerator;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : jigpud
 */
public class VerificationCodeRepositoryImpl implements VerificationCodeRepository {
    private static volatile VerificationCodeRepositoryImpl instance;

    private final VerificationCodeService verificationCodeService;

    private VerificationCodeRepositoryImpl() {
        verificationCodeService = ApiGenerator.create(VerificationCodeService.class);
    }

    @Override
    public Observable<Pair<Boolean, String>> sendVerificationCode(String username) {
        return verificationCodeService.sendVerificationCode(username)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(response -> {
                    if (response.isSuccess()) {
                        return new Pair<>(true, response.getMessage());
                    } else {
                        return new Pair<>(false, response.getMessage());
                    }
                });
    }

    @Override
    public Observable<Pair<Boolean, String>> sendVerificationCode(String username, String target) {
        return verificationCodeService.sendVerificationCode(username, target)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(response -> {
                    if (response.isSuccess()) {
                        return new Pair<>(true, response.getMessage());
                    } else {
                        return new Pair<>(false, response.getMessage());
                    }
                });
    }

    public static VerificationCodeRepositoryImpl getInstance() {
        if (instance == null) {
            synchronized (VerificationCodeRepositoryImpl.class) {
                if (instance == null) {
                    instance = new VerificationCodeRepositoryImpl();
                }
            }
        }
        return instance;
    }
}
