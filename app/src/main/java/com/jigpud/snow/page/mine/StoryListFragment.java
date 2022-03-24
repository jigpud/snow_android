package com.jigpud.snow.page.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.jigpud.snow.base.BaseFragment;
import com.jigpud.snow.databinding.MineStoryListBinding;

/**
 * @author jigpud
 */
public class StoryListFragment extends BaseFragment<MineStoryListBinding> {
    private MineViewModel mineViewModel;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mineViewModel = getParentViewModel(MineViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected MineStoryListBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MineStoryListBinding.inflate(inflater, container, false);
    }
}
