package com.jigpud.snow.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jigpud.snow.R;
import org.greenrobot.eventbus.EventBus;

/**
 * @author jigpud
 */
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    private boolean useLightStatusBar = true;
    private MaterialAlertDialogBuilder loadingAlertDialogBuilder;
    private AlertDialog loadingAlertDialog;

    protected VB binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initEventBus()) {
            EventBus.getDefault().register(this);
        }
        SplashScreen.installSplashScreen(this);
        preSetContent(savedInstanceState);
        binding = createViewBinding();
        setContentView(binding.getRoot());
        postSetContent(savedInstanceState);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        binding = null;
    }

    protected void loading() {
        loadingAlertDialog = loadingAlertDialogBuilder.setView(R.layout.loading_alert)
                .setCancelable(false)
                .show();
    }

    protected void unLoading() {
        LottieAnimationView loading = loadingAlertDialog.findViewById(R.id.loading);
        if (loading != null) {
            loading.cancelAnimation();
        }
        loadingAlertDialog.dismiss();
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

    @CallSuper
    protected void preSetContent(@Nullable Bundle savedInstanceState) {
        setStatusBar();
        loadingAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
    }

    @CallSuper
    protected void postSetContent(@Nullable Bundle savedInstanceState) {}

    protected void initView() {}

    protected boolean initEventBus() {
        return false;
    }

    protected abstract VB createViewBinding();

    protected <VM extends ViewModel> VM getViewModel(Class<VM> viewModelClass, ViewModelProvider.Factory factory) {
        VM viewModel;
        if (factory != null) {
            viewModel = new ViewModelProvider(this, factory).get(viewModelClass);
        } else {
            viewModel = new ViewModelProvider(this).get(viewModelClass);
        }
        return viewModel;
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
}
