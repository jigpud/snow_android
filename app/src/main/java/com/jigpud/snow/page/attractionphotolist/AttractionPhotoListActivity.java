package com.jigpud.snow.page.attractionpicturelist;

import com.jigpud.snow.databinding.AttractionPhotoListBinding;
import com.jigpud.snow.page.base.BaseActivity;

/**
 * @author : jigpud
 */
public class AttractionPhotoListActivity extends BaseActivity<AttractionPhotoListBinding> {
    @Override
    protected AttractionPhotoListBinding createViewBinding() {
        return AttractionPhotoListBinding.inflate(getLayoutInflater());
    }
}
