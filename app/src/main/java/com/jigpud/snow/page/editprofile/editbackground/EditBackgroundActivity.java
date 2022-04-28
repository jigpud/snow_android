package com.jigpud.snow.page.editprofile.editbackground;

import android.content.Intent;
import android.view.View;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.EditBackgroundBinding;
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
public class EditBackgroundActivity extends BaseActivity<EditBackgroundBinding> {
    private String background;

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        Intent intent = getIntent();
        String background = "";
        if (intent != null) {
            background = intent.getStringExtra(KeyConstant.KEY_BACKGROUND);
        }
        onBackgroundChanged(background);

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
                        String background = result.get(0).getCompressPath();
                        onBackgroundChanged(background);
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
                        String background = result.get(0).getCompressPath();
                        onBackgroundChanged(background);
                    }

                    @Override
                    public void onCancel() {}
                });
    }

    private void onFinishEditClick(View target) {
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.KEY_BACKGROUND, background);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onBackgroundChanged(String background) {
        this.background = background;
        ImageLoader.loadImgFromUrl(
                binding.background,
                background,
                R.drawable.ic_placeholder_user_profile_bg,
                R.drawable.ic_placeholder_user_profile_bg
        );
        updateFinishEditButtonEnabled();
    }

    private void updateFinishEditButtonEnabled() {
        boolean enabled = background != null && !background.isEmpty();
        binding.finishEdit.setEnabled(enabled);
    }

    @Override
    protected EditBackgroundBinding createViewBinding() {
        return EditBackgroundBinding.inflate(getLayoutInflater());
    }
}
