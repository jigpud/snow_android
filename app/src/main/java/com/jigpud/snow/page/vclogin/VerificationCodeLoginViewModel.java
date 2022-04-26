package com.jigpud.snow.page.vclogin;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.page.base.BaseVerificationCodeViewModel;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.vc.VerificationCodeRepository;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : jigpud
 */
public class VerificationCodeLoginViewModel extends BaseVerificationCodeViewModel {
    private final UserRepository userRepository;

    public VerificationCodeLoginViewModel(
            UserRepository userRepository,
            VerificationCodeRepository verificationCodeRepository
    ) {
        super(verificationCodeRepository);
        this.userRepository = userRepository;
    }

    public LiveData<Pair<Boolean, String>> loginByVerificationCode(String username, String verificationCode) {
        MutableLiveData<Pair<Boolean, String>> loginStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.loginByVerificationCode(username, verificationCode)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> loginStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(loginStatusLiveData::postValue);
        lifecycle(disposable);
        return loginStatusLiveData;
    }
}
