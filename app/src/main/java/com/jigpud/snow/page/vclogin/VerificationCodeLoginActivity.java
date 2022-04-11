package com.jigpud.snow.page.vclogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import com.jigpud.snow.databinding.VerificationCodeLoginBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.pwdlogin.PasswordLoginActivity;
import com.jigpud.snow.page.register.RegisterActivity;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;

import java.util.Objects;

/**
 * @author jigpud
 */
public class VerificationCodeLoginActivity extends BaseActivity<VerificationCodeLoginBinding> {
    private static final String TAG = "VerificationCodeLoginActivity";
    private static final int USERNAME_LENGTH = 11;
    private static final int VERIFICATION_CODE_LENGTH = 6;

    private VerificationCodeLoginViewModel verificationCodeLoginViewModel;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        verificationCodeLoginViewModel = getViewModel(VerificationCodeLoginViewModel.class, VerificationCodeLoginViewModelFactory.create());
    }

    @Override
    protected void initView() {
        super.initView();

        binding.close.setOnClickListener(target -> finish());

        binding.username.addTextChangedListener(onUsernameChanged());

        binding.verificationCode.addTextChangedListener(onVerificationCodeChanged());

        binding.sendVerificationCode.setOnClickListener(this::onSendVerificationCodeClick);
        observeNotNull(verificationCodeLoginViewModel.getSendVerificationTimer(), binding.sendVerificationCode::setText);
        observeNotNull(verificationCodeLoginViewModel.getSendVerificationCodeEnabled(), binding.sendVerificationCode::setEnabled);

        binding.login.setOnClickListener(this::onLoginClick);

        binding.passwordLogin.setOnClickListener(this::onPasswordLoginClick);

        binding.register.setOnClickListener(this::onRegisterClick);
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
                updateLoginEnabled();
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
                updateLoginEnabled();
            }
        };
    }

    private void updateLoginEnabled() {
        boolean enabled = Objects.requireNonNull(binding.username.getText()).length() == USERNAME_LENGTH &&
                Objects.requireNonNull(binding.verificationCode.getText()).length() == VERIFICATION_CODE_LENGTH;
        binding.login.setEnabled(enabled);
    }

    private void updateSendVerificationCodeEnabled() {
        boolean enabled = Objects.requireNonNull(binding.username.getText()).length() == USERNAME_LENGTH;
        binding.sendVerificationCode.setEnabled(enabled);
    }

    private void onPasswordLoginClick(View target) {
        startActivity(new Intent(this, PasswordLoginActivity.class));
    }

    private void onLoginClick(View target) {
        loading();
        Editable username = binding.username.getText();
        Editable verificationCode = binding.verificationCode.getText();
        if (username != null && verificationCode != null) {
            verificationCodeLoginViewModel.loginByVerificationCode(username.toString(), verificationCode.toString())
                    .observe(this, loginStatus -> {
                        if (loginStatus.first) {
                            Logger.d(TAG, "login success! current %s.",
                                    CurrentUser.getInstance(getApplicationContext()).getCurrentUserid());
                        } else {
                            Logger.d(TAG, "login failed! cause by %s.", loginStatus.second);
                        }
                        unLoading();
                    });
        }
    }

    private void onSendVerificationCodeClick(View target) {
        loading();
        Editable username = binding.username.getText();
        if (username != null) {
            verificationCodeLoginViewModel.sendLoginVerificationCode(username.toString()).observe(this, sendStatus -> {
                if (sendStatus.first) {
                    verificationCodeLoginViewModel.startTimer();
                    Logger.d(TAG, "send verification code success!");
                } else {
                    Logger.e(TAG, "send verification code failed! cause by %s.", sendStatus.second);
                }
                unLoading();
            });
        }
    }

    private void onRegisterClick(View target) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    protected void onDestroy() {
        verificationCodeLoginViewModel.stopTimer();
        super.onDestroy();
    }

    @Override
    protected VerificationCodeLoginBinding createViewBinding() {
        return VerificationCodeLoginBinding.inflate(getLayoutInflater());
    }
}
