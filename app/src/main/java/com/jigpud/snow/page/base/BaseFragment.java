package com.jigpud.snow.page.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.*;
import androidx.viewbinding.ViewBinding;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

/**
 * @author jigpud
 */
public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {
    private boolean useLightStatusBar = true;

    protected VB binding;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(
            @NonNull @NotNull LayoutInflater inflater,
            @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
            @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState
    ) {
        binding = createViewBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull @NotNull View view,
            @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected void setUseLightStatusBar(boolean useLightStatusBar) {
        this.useLightStatusBar = useLightStatusBar;
        setStatusBar();
    }

    @SuppressWarnings("deprecation")
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (useLightStatusBar) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void initView() {}

    protected boolean initEventBus() {
        return false;
    }

    protected abstract VB createViewBinding(LayoutInflater inflater, ViewGroup container);

    protected <VM extends ViewModel> VM getViewModel(Class<VM> viewModelClass, ViewModelProvider.Factory factory) {
        return getViewModel(this, viewModelClass, factory);
    }

    protected <VM extends ViewModel> VM getViewModel(Class<VM> viewModelClass) {
        return getViewModel(this, viewModelClass, null);
    }

    protected <VM extends ViewModel> VM getParentViewModel(Class<VM> viewModelClass, ViewModelProvider.Factory factory) {
        return getViewModel(requireParentFragment(), viewModelClass, factory);
    }

    protected <VM extends ViewModel> VM getParentViewModel(Class<VM> viewModelClass) {
        return getViewModel(requireParentFragment(), viewModelClass, null);
    }

    protected <VM extends ViewModel> VM getActivityViewModel(Class<VM> viewModelClass, ViewModelProvider.Factory factory) {
        return getViewModel(requireActivity(), viewModelClass, factory);
    }

    protected <VM extends ViewModel> VM getActivityViewModel(Class<VM> viewModelClass) {
        return getViewModel(requireActivity(), viewModelClass, null);
    }

    protected <T> void observeNotNull(LiveData<T> liveData, Observer<T> observer) {
        liveData.observe(this, data -> {
            if (data != null) {
                observer.onChanged(data);
            }
        });
    }

    protected <T> void observeForeverNotNull(LiveData<T> liveData, Observer<T> observer) {
        liveData.observeForever(data -> {
            if (data != null) {
                observer.onChanged(data);
            }
        });
    }

    private <VM extends ViewModel> VM getViewModel(
            ViewModelStoreOwner owner,
            Class<VM> viewModelClass,
            ViewModelProvider.Factory factory
    ) {
        VM activityViewModel;
        if (factory != null) {
            activityViewModel = new ViewModelProvider(owner, factory).get(viewModelClass);
        } else {
            activityViewModel = new ViewModelProvider(owner).get(viewModelClass);
        }
        return activityViewModel;
    }

    private Window getWindow() {
        return requireActivity().getWindow();
    }
}
