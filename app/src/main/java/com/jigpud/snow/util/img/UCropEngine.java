package com.jigpud.snow.util.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.utils.ActivityCompatHelper;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author : jigpud
 */
public class UCropEngine implements CropFileEngine {
    private static volatile UCropEngine instance;

    private UCropEngine() {}

    @Override
    public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
        UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
        uCrop.setImageEngine(CropImageEngin.createImageEngin());
        uCrop.start(fragment.requireContext(), fragment, requestCode);
    }

    public static UCropEngine createCropEngine() {
        if (instance == null) {
            synchronized (UCropEngine.class) {
                if (instance == null) {
                    instance = new UCropEngine();
                }
            }
        }
        return instance;
    }

    private static class CropImageEngin implements UCropImageEngine {
        private static volatile CropImageEngin instance;

        private CropImageEngin() {}

        @Override
        public void loadImage(Context context, String url, ImageView imageView) {
            if (!ActivityCompatHelper.assertValidRequest(context)) {
                return;
            }
            Glide.with(context).load(url).into(imageView);
        }

        @Override
        public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
            if (!ActivityCompatHelper.assertValidRequest(context)) {
                return;
            }
            Glide.with(context).asBitmap().override(maxWidth, maxHeight).load(url).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(
                        @NonNull @NotNull Bitmap resource,
                        @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                    if (call != null) {
                        call.onCall(resource);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {}

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    if (call != null) {
                        call.onCall(null);
                    }
                }
            });
        }

        public static CropImageEngin createImageEngin() {
            if (instance == null) {
                synchronized (CropImageEngin.class) {
                    if (instance == null) {
                        instance = new CropImageEngin();
                    }
                }
            }
            return instance;
        }
    }
}
