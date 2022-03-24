package com.jigpud.snow.page.pwdlogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import com.jigpud.snow.base.BaseActivity;
import com.jigpud.snow.databinding.PasswordLoginBinding;
import com.jigpud.snow.page.home.HomeActivity;
import com.jigpud.snow.page.register.RegisterActivity;
import com.jigpud.snow.page.retrievepwd.RetrievePasswordActivity;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;

import java.util.Objects;

/**
 * @author jigpud
 */
public class PasswordLoginActivity extends BaseActivity<PasswordLoginBinding> {
    private static final String TAG = "LoginActivity";
    private static final int USERNAME_LENGTH = 11;

    private PasswordLoginViewModel viewModel;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        viewModel = getViewModel(PasswordLoginViewModel.class, PasswordLoginViewModelFactory.create());
    }

    @Override
    protected void initView() {
        super.initView();

        binding.close.setOnClickListener(target -> finish());

        binding.username.addTextChangedListener(onUsernameChanged());

        binding.password.addTextChangedListener(onPasswordChanged());

        binding.login.setOnClickListener(this::onLoginClick);

        binding.forgetPassword.setOnClickListener(this::onForgetPasswordClick);

        binding.register.setOnClickListener(this::onRegisterClick);
    }

    private void onForgetPasswordClick(View target) {
        startActivity(new Intent(this, RetrievePasswordActivity.class));
    }

    private void onRegisterClick(View target) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private TextWatcher onUsernameChanged() {
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

    private TextWatcher onPasswordChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

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
                Objects.requireNonNull(binding.password.getText()).length() > 0;
        binding.login.setEnabled(enabled);
    }

    private void onLoginClick(View target) {
        loading();
        Editable username = binding.username.getText();
        Editable password = binding.password.getText();
        if (username == null || password == null) {
            return;
        }
        Logger.d(TAG, "username: %s, password: %s", username, password);
        viewModel.loginByPassword(username.toString(), password.toString()).observe(this, loginStatus -> {
            unLoading();
            if (loginStatus.first) {
                Logger.d(TAG, "login success! current %s.",
                        CurrentUser.getInstance(getApplicationContext()).getCurrentUsername());
                startActivity(new Intent(this, HomeActivity.class));
                finishAffinity();
            } else {
                Logger.d(TAG, "login failed! cause by %s.", loginStatus.second);
            }
        });
    }

    @Override
    protected PasswordLoginBinding createViewBinding() {
        return PasswordLoginBinding.inflate(getLayoutInflater());
    }
}
