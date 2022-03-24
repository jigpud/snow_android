package com.jigpud.snow.page.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import com.jigpud.snow.base.BaseActivity;
import com.jigpud.snow.databinding.RegisterBinding;
import com.jigpud.snow.util.logger.Logger;

import java.util.Objects;

/**
 * @author jigpud
 */
public class RegisterActivity extends BaseActivity<RegisterBinding> {
    private static final String TAG = "RegisterActivity";
    private static final int USERNAME_LENGTH = 11;
    private static final int VERIFICATION_CODE_LENGTH = 6;

    private RegisterViewModel registerViewModel;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        registerViewModel = getViewModel(RegisterViewModel.class, RegisterViewModelFactory.create());
    }

    @Override
    protected void initView() {
        super.initView();

        binding.close.setOnClickListener(target -> finish());

        binding.close.setOnClickListener(target -> finish());

        binding.username.addTextChangedListener(onUsernameChanged());

        binding.password.addTextChangedListener(onPasswordChanged());

        binding.passwordConfirm.addTextChangedListener(onPasswordChanged());

        binding.verificationCode.addTextChangedListener(onVerificationCodeChanged());

        binding.sendVerificationCode.setOnClickListener(this::onSendVerificationCodeClick);
        observeNotNull(registerViewModel.getSendVerificationTimer(), binding.sendVerificationCode::setText);
        observeNotNull(registerViewModel.getSendVerificationCodeEnabled(), binding.sendVerificationCode::setEnabled);

        binding.register.setOnClickListener(this::onRegisterClick);
    }

    private void onRegisterClick(View target) {
        String username = Objects.requireNonNull(binding.username.getText()).toString();
        String password = Objects.requireNonNull(binding.password.getText()).toString();
        String passwordConfirm = Objects.requireNonNull(binding.passwordConfirm.getText()).toString();
        String verificationCode = Objects.requireNonNull(binding.verificationCode.getText()).toString();
        if (verifyPassword(password, passwordConfirm)) {
            observeNotNull(registerViewModel.register(username, password, verificationCode), registerStatus -> {
                if (registerStatus.first) {
                    Logger.d(TAG, "register success!");
                    // TODO toast, auto login or not
                } else {
                    Logger.d(TAG, "register failed!");
                    // TODO toast
                }
            });
        } else {
            Logger.d(TAG, "password verification not pass!");
            // TODO toast
        }
    }

    private void onSendVerificationCodeClick(View target) {
        loading();
        String username = Objects.requireNonNull(binding.username.getText()).toString();
        observeNotNull(registerViewModel.sendRegisterVerificationCode(username), sendVerificationCodeStatus -> {
            if (sendVerificationCodeStatus.first) {
                registerViewModel.startTimer();
                Logger.d(TAG, "send verification code success!");
                // TODO toast
            } else {
                Logger.d(TAG, "send verification code failed!");
                // TODO toast
            }
            unLoading();
        });
    }

    private TextWatcher onVerificationCodeChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateRegisterEnabled();
            }
        };
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
                updateRegisterEnabled();
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
                updateRegisterEnabled();
            }
        };
    }

    private boolean verifyPassword(String password, String passwordConfirm) {
        return true;
    }

    private void updateSendVerificationCodeEnabled() {
        binding.sendVerificationCode.setEnabled(Objects.requireNonNull(binding.username.getText()).length() == USERNAME_LENGTH);
    }

    private void updateRegisterEnabled() {
        boolean enabled = Objects.requireNonNull(binding.username.getText()).length() == USERNAME_LENGTH &&
                Objects.requireNonNull(binding.verificationCode.getText()).length() == VERIFICATION_CODE_LENGTH &&
                Objects.requireNonNull(binding.password.getText()).length() > 0 &&
                Objects.requireNonNull(binding.passwordConfirm.getText()).length() > 0;
        binding.register.setEnabled(enabled);
    }

    @Override
    protected void onDestroy() {
        registerViewModel.stopTimer();
        super.onDestroy();
    }

    @Override
    protected RegisterBinding createViewBinding() {
        return RegisterBinding.inflate(getLayoutInflater());
    }
}
