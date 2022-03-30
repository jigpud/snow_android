package com.jigpud.snow.page.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import bolts.Task;
import com.jigpud.snow.base.BaseFragment;
import com.jigpud.snow.databinding.MainBinding;

/**
 * @author jigpud
 */
public class MainFragment extends BaseFragment<MainBinding> {
    @Override
    public void onResume() {
        super.onResume();
        useLightStatusBar();
    }

    private void useLightStatusBar() {
        Task.call(() -> {
            setUseLightStatusBar(true);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected MainBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MainBinding.inflate(inflater, container, false);
    }
}
