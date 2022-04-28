package com.jigpud.snow.page.editprofile.editnickname;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.EditNicknameBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.editprofile.EditProfileViewModel;
import com.jigpud.snow.page.editprofile.EditProfileViewModelFactory;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.format.IntegerFormatter;

import java.util.Objects;

/**
 * @author : jigpud
 */
public class EditNicknameActivity extends BaseActivity<EditNicknameBinding> {
    private static final int MAX_LENGTH_NICKNAME = 20;

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.nickname.addTextChangedListener(onNicknameChanged());
        Intent intent = getIntent();
        if (intent != null) {
            binding.nickname.setText(intent.getStringExtra(KeyConstant.KEY_NICKNAME));
        }

        binding.finishEdit.setOnClickListener(this::onFinishEditClick);
    }

    private TextWatcher onNicknameChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateFinishEditButtonEnabled();
                updateLengthIndicator();
            }
        };
    }

    private void onFinishEditClick(View target) {
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.KEY_NICKNAME, Objects.requireNonNull(binding.nickname.getText()).toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateLengthIndicator() {
        int nicknameLength = binding.nickname.length();
        int lengthIndicatorColor = ContextCompat.getColor(this, R.color.text_dark_light);
        if (nicknameLength > 0 && nicknameLength <= MAX_LENGTH_NICKNAME) {
            lengthIndicatorColor = ContextCompat.getColor(this, R.color.text_dark_mid);
        } else if (nicknameLength > MAX_LENGTH_NICKNAME) {
            lengthIndicatorColor = ContextCompat.getColor(this, R.color.text_error);
        }
        binding.lengthIndicator.setText(IntegerFormatter.formatFraction(nicknameLength, MAX_LENGTH_NICKNAME));
        binding.lengthIndicator.setTextColor(lengthIndicatorColor);
    }

    private void updateFinishEditButtonEnabled() {
        int nicknameLength = binding.nickname.length();
        boolean enabled = nicknameLength > 0 && nicknameLength <= MAX_LENGTH_NICKNAME;
        binding.finishEdit.setEnabled(enabled);
    }

    @Override
    protected EditNicknameBinding createViewBinding() {
        return EditNicknameBinding.inflate(getLayoutInflater());
    }
}
