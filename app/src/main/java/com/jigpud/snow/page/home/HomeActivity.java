package com.jigpud.snow.page.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.HomeBinding;
import com.jigpud.snow.event.OnLoginExpiredEvent;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.SimpleFragmentStateAdapter;
import com.jigpud.snow.page.main.MainFragment;
import com.jigpud.snow.page.mine.MineFragment;
import com.jigpud.snow.page.moments.MomentsFragment;
import com.jigpud.snow.page.vclogin.VerificationCodeLoginActivity;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author jigpud
 */
public class HomeActivity extends BaseActivity<HomeBinding> {
    private static final String TAG = "HomeActivity";
    private static final Fragment[] fragments = new Fragment[] {
            new MainFragment(),
            new MomentsFragment(),
            new MineFragment()
    };

    @Override
    protected void initView() {
        super.initView();

        binding.content.setSaveEnabled(false);
        binding.content.setUserInputEnabled(false);
        binding.content.setAdapter(new SimpleFragmentStateAdapter(fragments, this));

        binding.navigation.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        if (needLogin(item) && !CurrentUser.getInstance(getApplicationContext()).isLogin()) {
            gotoLogin();
            return false;
        } else {
            binding.content.setCurrentItem(getContentPosition(item), false);
            return true;
        }
    }

    private boolean needLogin(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.nav_moments || id == R.id.nav_mine;
    }

    @SuppressLint("NonConstantResourceId")
    private int getContentPosition(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_moments:
                return 1;
            case R.id.nav_mine:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    protected HomeBinding createViewBinding() {
        return HomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected boolean needEventBus() {
        return true;
    }

    private void gotoLogin() {
        Logger.d(TAG, "goto login");
        startActivity(new Intent(this, VerificationCodeLoginActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onLoginExpired(OnLoginExpiredEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        CurrentUser.getInstance(getApplicationContext()).logout();
        gotoLogin();
    }
}
