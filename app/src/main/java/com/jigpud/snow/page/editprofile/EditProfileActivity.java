package com.jigpud.snow.page.editprofile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.EditProfileBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.editprofile.editavatar.EditAvatarActivity;
import com.jigpud.snow.page.editprofile.editbackground.EditBackgroundActivity;
import com.jigpud.snow.page.editprofile.editnickname.EditNicknameActivity;
import com.jigpud.snow.page.editprofile.editsignature.EditSignatureActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.img.ImageLoader;
import com.jigpud.snow.util.logger.Logger;

/**
 * @author : jigpud
 */
public class EditProfileActivity extends BaseActivity<EditProfileBinding> {
    private static final String TAG = "EditProfileActivity";

    private EditProfileViewModel editProfileViewModel;
    private String oldBackground;
    private String newBackground;
    private String oldAvatar;
    private String newAvatar;
    private String oldNickname;
    private String newNickname;
    private String oldSignature;
    private String newSignature;
    private ActivityResultLauncher<Intent> startForEditAvatar;
    private ActivityResultLauncher<Intent> startForEditBackground;
    private ActivityResultLauncher<Intent> startForEditNickname;
    private ActivityResultLauncher<Intent> startForEditSignature;
    private boolean isFirstInit = true;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        editProfileViewModel = getViewModel(EditProfileViewModel.class, EditProfileViewModelFactory.create());

        startForEditBackground = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String newBackground = result.getData().getStringExtra(KeyConstant.KEY_BACKGROUND);
                        Logger.d(TAG, "newBackground: %s", newBackground);
                        onBackgroundChanged(newBackground);
                    }
                }
        );

        startForEditAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String newAvatar = result.getData().getStringExtra(KeyConstant.KEY_AVATAR);
                        Logger.d(TAG, "newAvatar: %s", newAvatar);
                        onAvatarChanged(newAvatar);
                    }
                }
        );

        startForEditNickname = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String newNickname = result.getData().getStringExtra(KeyConstant.KEY_NICKNAME);
                        Logger.d(TAG, "newNickname: %s", newNickname);
                        binding.nickname.setText(newNickname);
                    }
                }
        );

        startForEditSignature = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String newSignature = result.getData().getStringExtra(KeyConstant.KEY_SIGNATURE);
                        Logger.d(TAG, "newSignature: %s", newSignature);
                        binding.signature.setText(newSignature);
                    }
                }
        );
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        observeNotNull(editProfileViewModel.getSelfInfo(), user -> {
            if (!isFirstInit) {
                return;
            }
            isFirstInit = false;

            binding.backgroundContainer.setOnClickListener(this::onBackgroundClick);
            oldBackground = user.getBackground();
            onBackgroundChanged(oldBackground);

            binding.avatarContainer.setOnClickListener(this::onAvatarClick);
            oldAvatar = user.getAvatar();
            onAvatarChanged(oldAvatar);

            binding.nickname.addTextChangedListener(onNicknameChanged());
            binding.nicknameContainer.setOnClickListener(this::onNicknameClick);
            oldNickname = user.getNickname();
            binding.nickname.setText(user.getNickname());

            binding.signature.addTextChangedListener(onSignatureChanged());
            binding.signatureContainer.setOnClickListener(this::onSignatureClick);
            oldSignature = user.getSignature();
            binding.signature.setText(user.getSignature());
        });

        binding.saveProfile.setOnClickListener(this::onSaveProfileClick);
    }

    private TextWatcher onNicknameChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                newNickname = s.toString();
                updateSaveProfileButtonEnabled();
            }
        };
    }

    private TextWatcher onSignatureChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                newSignature = s.toString();
                updateSaveProfileButtonEnabled();
            }
        };
    }

    private void onBackgroundClick(View target) {
        Intent intent = new Intent(this, EditBackgroundActivity.class);
        intent.putExtra(KeyConstant.KEY_BACKGROUND, newBackground);
        startForEditBackground.launch(intent);
    }

    private void onAvatarClick(View target) {
        Intent intent = new Intent(this, EditAvatarActivity.class);
        intent.putExtra(KeyConstant.KEY_AVATAR, newAvatar);
        startForEditAvatar.launch(intent);
    }

    private void onNicknameClick(View target) {
        Intent intent = new Intent(this, EditNicknameActivity.class);
        intent.putExtra(KeyConstant.KEY_NICKNAME, newNickname);
        startForEditNickname.launch(intent);
    }

    private void onSignatureClick(View target) {
        Intent intent = new Intent(this, EditSignatureActivity.class);
        intent.putExtra(KeyConstant.KEY_SIGNATURE, newSignature);
        startForEditSignature.launch(intent);
    }

    private void onSaveProfileClick(View target) {
        Logger.d(TAG, "\n" +
                "newBackground: %s\n" +
                "newAvatar: %s\n" +
                "newNickname: %s\n" +
                "newSignature: %s", newBackground, newAvatar, newNickname, newSignature);
        loading();
        observeNotNull(editProfileViewModel.updateInfo(newBackground, newAvatar, newNickname, newSignature), updateSelfInfoStatus -> {
            unLoading();
            if (updateSelfInfoStatus.first) {
                Logger.d(TAG, "update information success");
                observeNotNull(editProfileViewModel.getSelfInfo(), user -> finish());
            } else {
                Logger.d(TAG, "update information failed! cause by: %s", updateSelfInfoStatus.second);
            }
        });
    }

    private void updateSaveProfileButtonEnabled() {
        boolean enabled = (newBackground != null && !newBackground.equals(oldBackground)) ||
                (newAvatar != null && !newAvatar.equals(oldAvatar)) ||
                (newNickname != null && !newNickname.equals(oldNickname)) ||
                (newSignature != null && !newSignature.equals(oldSignature));
        enabled = enabled && newNickname != null && !newNickname.isEmpty();
        binding.saveProfile.setEnabled(enabled);
    }

    private void onAvatarChanged(String avatar) {
        newAvatar = avatar;
        ImageLoader.loadImgFromUrl(
                binding.avatar,
                avatar,
                R.drawable.ic_placeholder_avatar,
                R.drawable.ic_placeholder_avatar
        );
        updateSaveProfileButtonEnabled();
    }

    private void onBackgroundChanged(String background) {
        newBackground = background;
        ImageLoader.loadImgFromUrl(
                binding.background,
                background,
                R.drawable.ic_placeholder_user_profile_bg,
                R.drawable.ic_placeholder_user_profile_bg
        );
        updateSaveProfileButtonEnabled();
    }

    @Override
    protected EditProfileBinding createViewBinding() {
        return EditProfileBinding.inflate(getLayoutInflater());
    }
}
