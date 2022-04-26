package com.jigpud.snow.util.img;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import com.bumptech.glide.Glide;

/**
 * @author : jigpud
 */
public class ImageLoader {
    public static void loadImgFromUrl(ImageView target, String url) {
        loadImgFromUrl(target, url, null, null);
    }

    public static void loadImgFromUrl(ImageView target, String url, @DrawableRes int placeholder, @DrawableRes int error) {
        Glide.with(target)
                .load(Uri.decode(url))
                .placeholder(placeholder)
                .error(error)
                .into(target);
    }

    public static void loadImgFromUrl(ImageView target, String url, Drawable placeholder, Drawable error) {
        Glide.with(target)
                .load(Uri.decode(url))
                .placeholder(placeholder)
                .error(error)
                .into(target);
    }
}
