package com.jigpud.snow.page.retrievepwd;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import com.jigpud.snow.base.BaseActivity;
import com.jigpud.snow.databinding.RetrievePasswordBinding;
import com.jigpud.snow.util.logger.Logger;

import java.util.Objects;

/**
 * @author jigpud
 */
public class RetrievePasswordActivity extends BaseActivity<RetrievePasswordBinding> {
    private static final String TAG = "RetrievePasswordActivity";
    private static final int USERNAME_LENGTH = 11;
    private static final int VERIFICATION_CODE_LENGTH = 6;

    private RetrievePasswordViewModel retrievePasswordViewModel;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        retrievePasswordViewModel = getViewModel(RetrievePasswordViewModel.class, RetrievePasswordViewModelFactory.create());
    }

    @Override
    protected void initView() {
        super.initView();

        binding.close.setOnClickListener(target -> finish());

        binding.username.addTextChangedListener(onUsernameChanged());

        binding.password.addTextChangedListener(onPasswordChanged());

        binding.passwordConfirm.addTextChangedListener(onPasswordChanged());

        binding.verificationCode.addTextChangedListener(onVerificationCodeChanged());

        binding.sendVerificationCode.setOnClickListener(this::onSendVerificationCodeClick);
        observeNotNull(retrievePasswordViewModel.getSendVerificationTimer(), binding.sendVerificationCode::setText);
        observeNotNull(retrievePasswordViewModel.getSendVerificationCodeEnabled(), binding.sendVerificationCode::setEnabled);

        binding.retrievePassword.setOnClickListener(this::onRetrievePasswordClick);
    }

    private void onRetrievePasswordClick(View target) {
        String username = Objects.requireNonNull(binding.username.getText()).toString();
        String password = Objects.requireNonNull(binding.password.getText()).toString();
        String passwordConfirm = Objects.requireNonNull(binding.passwordConfirm.getText()).toString();
        String verificationCode = Objects.requireNonNull(binding.verificationCode.getText()).toString();
        if (retrievePasswordViewModel.verifyPassword(password, passwordConfirm)) {
            loading();
            observeNotNull(retrievePasswordViewModel.retrievePassword(username, password, verificationCode),
                    retrievePasswordStatus -> {
                        if (retrievePasswordStatus.first) {
                            Logger.d(TAG, "retrieve password success!");
                            // TODO toast
                        } else {
                            Logger.d(TAG, "retrieve password failed!");
                            // TODO toast
                        }
                        unLoading();
                    });
        } else {
            Logger.d(TAG, "password verification not pass!");
            // TODO toast
        }
    }

    private void onSendVerificationCodeClick(View target) {
        loading();
        String username = Objects.requireNonNull(binding.username.getText()).toString();
        observeNotNull(retrievePasswordViewModel.sendRetrievePasswordVerificationCode(username),
                sendVerificationCodeStatus -> {
                    if (sendVerificationCodeStatus.first) {
                        retrievePasswordViewModel.startTimer();
                        Logger.d(TAG, "send verification code success!");
                        // TODO toast
                    } else {
                        Logger.d(TAG, "send verification code failed!");
                        // TODO toast
                    }
                    unLoading();
                });
    }

    private TextWatcher onUsernameChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateSendVerificationCodeEnabled();
                updateRetrievePasswordEnabled();
            }
        };
    }

    private TextWatcher onPasswordChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateRetrievePasswordEnabled();
            }
        };
    }

    private TextWatcher onVerificationCodeChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateRetrievePasswordEnabled();
            }
        };
    }

    private void updateRetrievePasswordEnabled() {
        boolean enabled = Objects.requireNonNull(binding.username.getText()).length() == USERNAME_LENGTH &&
                Objects.requireNonNull(binding.password.getText()).length() > 0
                && Objects.requireNonNull(binding.passwordConfirm.getText()).length() > 0
                && Objects.requireNonNull(binding.verificationCode.getText()).length() == VERIFICATION_CODE_LENGTH;
        binding.retrievePassword.setEnabled(enabled);
    }

    private void updateSendVerificationCodeEnabled() {
        boolean enabled = Objects.requireNonNull(binding.username.getText()).length() == USERNAME_LENGTH;
        binding.sendVerificationCode.setEnabled(enabled);
    }

    @Override
    protected void onDestroy() {
        retrievePasswordViewModel.stopTimer();
        super.onDestroy();
    }

    @Override
    protected RetrievePasswordBinding createViewBinding() {
        return RetrievePasswordBinding.inflate(getLayoutInflater());
    }
}
