package com.jigpud.snow.page.editprofile.editsignature;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.EditSignatureBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.format.IntegerFormatter;

import java.util.Objects;

/**
 * @author : jigpud
 */
public class EditSignatureActivity extends BaseActivity<EditSignatureBinding> {
    private static final int MAX_SIGNATURE_LENGTH = 80;

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.signature.addTextChangedListener(onSignatureChanged());
        Intent intent = getIntent();
        if (intent != null) {
            binding.signature.setText(intent.getStringExtra(KeyConstant.KEY_SIGNATURE));
        }

        binding.finishEdit.setOnClickListener(this::onFinishEditClick);
    }

    private TextWatcher onSignatureChanged() {
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
        intent.putExtra(KeyConstant.KEY_SIGNATURE, Objects.requireNonNull(binding.signature.getText()).toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateLengthIndicator() {
        int signatureLength = binding.signature.length();
        int lengthIndicatorColor = ContextCompat.getColor(this, R.color.text_dark_light);
        if (signatureLength > 0 && signatureLength <= MAX_SIGNATURE_LENGTH) {
            lengthIndicatorColor = ContextCompat.getColor(this, R.color.text_dark_mid);
        } else if (signatureLength > MAX_SIGNATURE_LENGTH) {
            lengthIndicatorColor = ContextCompat.getColor(this, R.color.text_error);
        }
        binding.lengthIndicator.setText(IntegerFormatter.formatFraction(signatureLength, MAX_SIGNATURE_LENGTH));
        binding.lengthIndicator.setTextColor(lengthIndicatorColor);
    }

    private void updateFinishEditButtonEnabled() {
        int signatureLength = binding.signature.length();
        boolean enabled = signatureLength >= 0 && signatureLength <= MAX_SIGNATURE_LENGTH;
        binding.finishEdit.setEnabled(enabled);
    }

    @Override
    protected EditSignatureBinding createViewBinding() {
        return EditSignatureBinding.inflate(getLayoutInflater());
    }
}
