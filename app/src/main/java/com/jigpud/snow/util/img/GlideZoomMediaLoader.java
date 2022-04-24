package com.jigpud.snow.util.img;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jigpud.snow.R;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class GlideZoomMediaLoader implements IZoomMediaLoader {
    private GlideZoomMediaLoader() {}

    @Override
    public void displayImage(
            @NonNull @NotNull Fragment fragment,
            @NonNull @NotNull String s,
            ImageView imageView,
            @NonNull @NotNull MySimpleTarget mySimpleTarget) {
        Glide.with(fragment)
                .load(s)
                .error(R.drawable.ps_image_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable @org.jetbrains.annotations.Nullable GlideException e,
                            Object model,
                            Target<Drawable> target,
                            boolean isFirstResource) {
                        mySimpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            Drawable resource,
                            Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource
                    ) {
                        mySimpleTarget.onResourceReady();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void displayGifImage(
            @NonNull @NotNull Fragment fragment,
            @NonNull @NotNull String s,
            ImageView imageView,
            @NonNull @NotNull MySimpleTarget mySimpleTarget
    ) {
        Glide.with(fragment)
                .load(s)
                .error(R.drawable.ps_image_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable @org.jetbrains.annotations.Nullable GlideException e,
                            Object model,
                            Target<Drawable> target,
                            boolean isFirstResource
                    ) {
                        mySimpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            Drawable resource,
                            Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource
                    ) {
                        mySimpleTarget.onResourceReady();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void onStop(@NonNull @NotNull Fragment fragment) {
        Glide.with(fragment).onStop();
    }

    @Override
    public void clearMemory(@NonNull @NotNull Context c) {
        Glide.get(c).clearMemory();
    }

    public static GlideZoomMediaLoader createMediaLoader() {
        return new GlideZoomMediaLoader();
    }
}
