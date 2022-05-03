package com.jigpud.snow.page.attractionpicturelist;

import com.jigpud.snow.databinding.AttractionPictureListBinding;
import com.jigpud.snow.page.base.BaseActivity;

/**
 * @author : jigpud
 */
public class AttractionPictureListActivity extends BaseActivity<AttractionPictureListBinding> {
    @Override
    protected AttractionPictureListBinding createViewBinding() {
        return AttractionPictureListBinding.inflate(getLayoutInflater());
    }
}
