package com.jigpud.snow.page.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.jigpud.snow.base.BaseFragment;
import com.jigpud.snow.databinding.MainBinding;

/**
 * @author jigpud
 */
public class MainFragment extends BaseFragment<MainBinding> {
    @Override
    protected MainBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MainBinding.inflate(inflater, container, false);
    }
}
