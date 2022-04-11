package com.jigpud.snow.page.common;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class SimpleFragmentStateAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments;

    public SimpleFragmentStateAdapter(Fragment[] fragments, @NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    public SimpleFragmentStateAdapter(Fragment[] fragments, @NonNull @NotNull Fragment fragment) {
        super(fragment);
        this.fragments = fragments;
    }

    public SimpleFragmentStateAdapter(
            Fragment[] fragments,
            @NonNull @NotNull FragmentManager fragmentManager,
            @NonNull @NotNull Lifecycle lifecycle
    ) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
