package com.jigpud.snow.page.register;

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
public class RegisterViewModel extends BaseVerificationCodeViewModel {
    private final UserRepository userRepository;

    public RegisterViewModel(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository) {
        super(verificationCodeRepository);
        this.userRepository = userRepository;
    }

    public LiveData<Pair<Boolean, String>> register(String username, String password, String verificationCode) {
        MutableLiveData<Pair<Boolean, String>> registerStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.register(username, password, verificationCode)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> registerStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(registerStatusLiveData::postValue);
        lifecycle(disposable);
        return registerStatusLiveData;
    }
}
