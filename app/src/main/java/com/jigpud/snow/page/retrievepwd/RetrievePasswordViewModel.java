package com.jigpud.snow.page.retrievepwd;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.page.base.BaseVerificationCodeViewModel;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.vc.VerificationCodeRepository;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jigpud
 */
public class RetrievePasswordViewModel extends BaseVerificationCodeViewModel {
    private final UserRepository userRepository;

    public RetrievePasswordViewModel(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository) {
        super(verificationCodeRepository);
        this.userRepository = userRepository;
    }

    public LiveData<Pair<Boolean, String>> retrievePassword(String username, String password, String verificationCode) {
        MutableLiveData<Pair<Boolean, String>> retrievePasswordStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.retrievePassword(username, password, verificationCode)
                .observeOn(Schedulers.io())
                .subscribe(retrievePasswordStatusLiveData::postValue);
        lifecycle(disposable);
        return retrievePasswordStatusLiveData;
    }

    public boolean verifyPassword(String password, String passwordConfirm) {
        return true;
    }
}
