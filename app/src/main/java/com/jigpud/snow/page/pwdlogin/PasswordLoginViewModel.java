package com.jigpud.snow.page.pwdlogin;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.user.UserRepository;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jigpud
 */
public class PasswordLoginViewModel extends BaseViewModel {
    private final UserRepository userRepository;

    public PasswordLoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<Pair<Boolean, String>> loginByPassword(String username, String password) {
        MutableLiveData<Pair<Boolean, String>> loginStatusLiveData = new MutableLiveData<>();
        Disposable disposable = userRepository.loginByPassword(username, password)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> loginStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe(loginStatusLiveData::postValue);
        lifecycle(disposable);
        return loginStatusLiveData;
    }
}
