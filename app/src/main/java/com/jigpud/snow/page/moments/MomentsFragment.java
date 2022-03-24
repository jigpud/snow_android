package com.jigpud.snow.page.moments;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.jigpud.snow.base.BaseFragment;
import com.jigpud.snow.databinding.MomentsBinding;

/**
 * @author jigpud
 */
public class MomentsFragment extends BaseFragment<MomentsBinding> {
    @Override
    protected MomentsBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MomentsBinding.inflate(inflater, container, false);
    }
}
