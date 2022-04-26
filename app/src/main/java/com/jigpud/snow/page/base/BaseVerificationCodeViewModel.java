package com.jigpud.snow.page.base;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.R;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.http.VerificationCodeService;
import com.jigpud.snow.repository.vc.VerificationCodeRepository;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author : jigpud
 */
public abstract class BaseVerificationCodeViewModel  extends BaseViewModel {
    @SuppressLint("StaticFieldLeak")
    protected final Context context;
    protected final VerificationCodeRepository verificationCodeRepository;
    private final MutableLiveData<String> sendVerificationCodeTimer = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendVerificationCodeEnabled = new MutableLiveData<>();

    protected BaseVerificationCodeViewModel(VerificationCodeRepository userRepository) {
        context = SnowApplication.getAppContext();
        this.verificationCodeRepository = userRepository;
    }

    private Disposable sendVerificationCodeDisposable;

    public LiveData<Pair<Boolean, String>> sendLoginVerificationCode(String username) {
        MutableLiveData<Pair<Boolean, String>> sendVerificationCodeStatusLiveData = new MutableLiveData<>();
        String target = VerificationCodeService.VerificationCodeTarget.LOGIN.toString();
        Disposable disposable = verificationCodeRepository.sendVerificationCode(username, target)
                .observeOn(Schedulers.io())
                .subscribe(sendVerificationCodeStatusLiveData::postValue);
        lifecycle(disposable);
        return sendVerificationCodeStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> sendRegisterVerificationCode(String username) {
        MutableLiveData<Pair<Boolean, String>> sendVerificationCodeStatusLiveData = new MutableLiveData<>();
        String target = VerificationCodeService.VerificationCodeTarget.REGISTER.toString();
        Disposable disposable = verificationCodeRepository.sendVerificationCode(username, target)
                .observeOn(Schedulers.io())
                .subscribe(sendVerificationCodeStatusLiveData::postValue);
        lifecycle(disposable);
        return sendVerificationCodeStatusLiveData;
    }

    public LiveData<Pair<Boolean, String>> sendRetrievePasswordVerificationCode(String username) {
        MutableLiveData<Pair<Boolean, String>> sendVerificationCodeStatusLiveData = new MutableLiveData<>();
        String target = VerificationCodeService.VerificationCodeTarget.RETRIEVE_PASSWORD.toString();
        Disposable disposable = verificationCodeRepository.sendVerificationCode(username, target)
                .observeOn(Schedulers.io())
                .subscribe(sendVerificationCodeStatusLiveData::postValue);
        lifecycle(disposable);
        return sendVerificationCodeStatusLiveData;
    }

    public void startTimer() {
        stopTimer();
        sendVerificationCodeDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .take(60)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(t -> {
                    long countDown = 59 - t;
                    String sendVerificationCodeCountdown = context.getString(R.string.btn_send_verification_code_countdown);
                    @SuppressLint("DefaultLocale")
                    String display = String.format("%d%s", countDown, sendVerificationCodeCountdown);
                    return display;
                })
                .observeOn(Schedulers.io())
                .subscribe(countDown -> {
                    sendVerificationCodeTimer.postValue(countDown);
                    sendVerificationCodeEnabled.postValue(false);
                }, throwable -> stopTimer(), this::stopTimer);
    }

    public void stopTimer() {
        if (sendVerificationCodeDisposable != null) {
            sendVerificationCodeDisposable.dispose();
        }
        sendVerificationCodeTimer.postValue(context.getString(R.string.btn_send_verification_code));
        sendVerificationCodeEnabled.postValue(true);
    }

    public LiveData<String> getSendVerificationTimer() {
        return sendVerificationCodeTimer;
    }

    public LiveData<Boolean> getSendVerificationCodeEnabled() {
        return sendVerificationCodeEnabled;
    }
}
