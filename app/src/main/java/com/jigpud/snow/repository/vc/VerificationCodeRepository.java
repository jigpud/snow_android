package com.jigpud.snow.repository.vc;

import androidx.core.util.Pair;
import io.reactivex.Observable;

/**
 * @author : jigpud
 */
public interface VerificationCodeRepository {
    Observable<Pair<Boolean, String>> sendVerificationCode(String username);

    Observable<Pair<Boolean, String>> sendVerificationCode(String username, String target);
}
