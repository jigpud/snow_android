package com.jigpud.snow.page.newstory;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import com.jigpud.snow.databinding.NewStoryBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.thumbinfo.PictureThumbViewInfo;
import com.jigpud.snow.util.img.GlideImageEngine;
import com.jigpud.snow.util.img.LubanCompressEngine;
import com.jigpud.snow.util.img.UCropEngine;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.toast.ToastMaker;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.previewlibrary.GPreviewBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : jigpud
 */
public class NewStoryActivity extends BaseActivity<NewStoryBinding> implements StoryPictureListAdapter.StoryPictureListCallback {
    private static final String TAG = "NewStoryActivity";

    private NewStoryViewModel newStoryViewModel;
    private StoryPictureListAdapter storyPictureListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        newStoryViewModel = getViewModel(NewStoryViewModel.class, NewStoryViewModelFactory.create());
        storyPictureListAdapter = new StoryPictureListAdapter(this);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.storyTitle.addTextChangedListener(onTitleOrContentChanged());

        binding.storyContent.addTextChangedListener(onTitleOrContentChanged());

        binding.storyPictureList.setAdapter(storyPictureListAdapter);
        binding.storyPictureList.setLayoutManager(new GridLayoutManager(this, 3));

        binding.releaseStory.setOnClickListener(this::onReleaseStoryClick);
    }

    @Override
    public void onAddPicture() {
        Logger.d(TAG, "select story picture");
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(Integer.MAX_VALUE)
                .setImageEngine(GlideImageEngine.createEngine())
                .setCropEngine(UCropEngine.createCropEngine())
                .setCompressEngine(LubanCompressEngine.createCompressEngin())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        List<String> pictureList = new ArrayList<>();
                        for (LocalMedia localMedia : result) {
                            pictureList.add(localMedia.getCompressPath());
                        }
                        storyPictureListAdapter.addRecords(pictureList);
                    }

                    @Override
                    public void onCancel() {}
                });
    }

    @Override
    public void onPictureClick(int position) {
        List<PictureThumbViewInfo> data = new ArrayList<>();
        for (String path : storyPictureListAdapter.getRecords()) {
            data.add(new PictureThumbViewInfo(path));
        }
        GPreviewBuilder.from(this)
                .setData(data)
                .setCurrentIndex(position)
                .setSingleFling(false)
                .isDisableDrag(true)
                .start();
    }

    @Override
    public void onPictureDelete(int position) {
        List<String> pictureList = new ArrayList<>(storyPictureListAdapter.getRecords());
        pictureList.remove(position);
        storyPictureListAdapter.setRecords(pictureList);
    }

    private void onReleaseStoryClick(View target) {
        String title = Objects.requireNonNull(binding.storyTitle.getText()).toString();
        String content = Objects.requireNonNull(binding.storyContent.getText()).toString();
        String attractionId = "8c25853d46fc4b189b11d9c86683a0d2";
        List<String> pictureList = storyPictureListAdapter.getRecords();
        loading();
        observeNotNull(newStoryViewModel.releaseStory(title, content, pictureList, attractionId), releaseStoryStatus -> {
            unLoading();
            if (releaseStoryStatus.first) {
                Logger.d(TAG, "release story success");
                finish();
            } else {
                Logger.d(TAG, "release story failed! caused by: %s", releaseStoryStatus.second);
                ToastMaker.makeToast("发布失败");
            }
        });
    }

    private TextWatcher onTitleOrContentChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateReleaseButtonEnabled();
            }
        };
    }

    private void updateReleaseButtonEnabled() {
        boolean enabled = Objects.requireNonNull(binding.storyTitle.getText()).length() > 0 &&
                Objects.requireNonNull(binding.storyContent.getText()).length() > 0;
        binding.releaseStory.setEnabled(enabled);
    }

    @Override
    protected NewStoryBinding createViewBinding() {
        return NewStoryBinding.inflate(getLayoutInflater());
    }
}
