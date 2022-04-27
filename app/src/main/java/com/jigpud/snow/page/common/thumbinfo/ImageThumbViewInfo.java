package com.jigpud.snow.page.common.thumbinfo;

import android.graphics.Rect;
import android.os.Parcel;
import androidx.annotation.Nullable;
import com.previewlibrary.enitity.IThumbViewInfo;

/**
 * @author : jigpud
 */
public class ImageThumbViewInfo implements IThumbViewInfo {
    private String url;
    private Rect bounds;

    public ImageThumbViewInfo(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public String getVideoUrl() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.bounds, flags);
    }

    protected ImageThumbViewInfo(Parcel in) {
        this.url = in.readString();
        this.bounds = in.readParcelable(Rect.class.getClassLoader());
    }

    public static final Creator<ImageThumbViewInfo> CREATOR = new Creator<ImageThumbViewInfo>() {
        @Override
        public ImageThumbViewInfo createFromParcel(Parcel source) {
            return new ImageThumbViewInfo(source);
        }

        @Override
        public ImageThumbViewInfo[] newArray(int size) {
            return new ImageThumbViewInfo[size];
        }
    };
}
