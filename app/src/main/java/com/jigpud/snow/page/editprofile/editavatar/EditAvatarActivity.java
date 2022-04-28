package com.jigpud.snow.page.editprofile.editavatar;

import android.content.Intent;
import android.view.View;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.EditAvatarBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.img.GlideImageEngine;
import com.jigpud.snow.util.img.ImageLoader;
import com.jigpud.snow.util.img.LubanCompressEngine;
import com.jigpud.snow.util.img.UCropEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.util.ArrayList;

/**
 * @author : jigpud
 */
public class EditAvatarActivity extends BaseActivity<EditAvatarBinding> {
    private String avatar;

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        Intent intent = getIntent();
        String avatar = "";
        if (intent != null) {
            avatar = intent.getStringExtra(KeyConstant.KEY_AVATAR);
        }
        onAvatarChanged(avatar);

        binding.fromCamera.setOnClickListener(this::onFromCameraClick);

        binding.fromGallery.setOnClickListener(this::onFromGalleryClick);

        binding.finishEdit.setOnClickListener(this::onFinishEditClick);
    }

    private void onFromCameraClick(View target) {
        PictureSelector.create(this)
                .openCamera(SelectMimeType.ofImage())
                .setCropEngine(UCropEngine.createCropEngine())
                .setCompressEngine(LubanCompressEngine.createCompressEngin())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        String avatar = result.get(0).getCompressPath();
                        onAvatarChanged(avatar);
                    }

                    @Override
                    public void onCancel() {}
                });
    }

    private void onFromGalleryClick(View target) {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(1)
                .setImageEngine(GlideImageEngine.createEngine())
                .setCropEngine(UCropEngine.createCropEngine())
                .setCompressEngine(LubanCompressEngine.createCompressEngin())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        String avatar = result.get(0).getCompressPath();
                        onAvatarChanged(avatar);
                    }

                    @Override
                    public void onCancel() {}
                });
    }

    private void onFinishEditClick(View target) {
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.KEY_AVATAR, avatar);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onAvatarChanged(String avatar) {
        this.avatar = avatar;
        ImageLoader.loadImgFromUrl(
                binding.avatar,
                avatar,
                R.drawable.ic_placeholder_avatar,
                R.drawable.ic_placeholder_avatar
        );
        updateFinishEditButtonEnabled();
    }

    private void updateFinishEditButtonEnabled() {
        boolean enabled = avatar != null && !avatar.isEmpty();
        binding.finishEdit.setEnabled(enabled);
    }

    @Override
    protected EditAvatarBinding createViewBinding() {
        return EditAvatarBinding.inflate(getLayoutInflater());
    }
}
