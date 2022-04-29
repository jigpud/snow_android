package com.jigpud.snow.page.common.adapter;

import androidx.annotation.DrawableRes;
import com.jigpud.snow.R;
import com.jigpud.snow.util.img.ImageLoader;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

/**
 * @author : jigpud
 */
public class ImageAdapter extends BannerImageAdapter<String> {
    private final ImageClickListener clickListener;
    private final int placeholder;

    public ImageAdapter(List<String> urlList, ImageClickListener clickListener) {
        this(R.drawable.ps_image_placeholder, urlList, clickListener);
    }

    public ImageAdapter(@DrawableRes int placeholder, List<String> urlList, ImageClickListener clickListener) {
        super(urlList);
        this.placeholder = placeholder;
        this.clickListener = clickListener;
    }

    @Override
    public void onBindView(BannerImageHolder holder, String url, int position, int size) {
        holder.imageView.setOnClickListener(target -> clickListener.onImageClick(position));
        ImageLoader.loadImgFromUrl(holder.imageView, url, placeholder, placeholder);
    }

    public List<String> getImageUrlList() {
        return mDatas;
    }

    public interface ImageClickListener {
        void onImageClick(int position);
    }
}
