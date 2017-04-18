
package com.xiaomai.geek.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.pref.PasswordPref;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.module.AboutUsFragment;
import com.xiaomai.geek.ui.module.password.PasswordContainerFragment;
import com.xiaomai.geek.ui.widget.EditTextDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private Fragment mCurrentFragment;

    private int mCurrentPosition = 0;

    public static final String SYSTEM_REASON = "reason";

    public static final String SYSTEM_HOME_KEY = "homekey";

    public static final String SYSTEM_HOME_KEY_LONG = "recentapps";

    private boolean runInBackground;

    private boolean mIsDialogShowing;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    // 表示按了home键
                    Log.e(TAG, "onReceive: home press");
                    runInBackground = true;
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    // 表示长按home键
                }
            }
        }
    };

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void initViews() {
        navView.setNavigationItemSelectedListener(this);
        openPassword();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.menu_password_manage:
                openPassword();
                break;
            case R.id.menu_about:
                changeFragment(AboutUsFragment.class.getName());
                mCurrentPosition = 1;
                break;
        }
        return true;
    }

    private void openPassword() {
        new EditTextDialog.Builder(mContext)
                .setTitle(PasswordPref.hasPassword(mContext) ? "打开密码箱" : "设置密码")
                .setCancelable(!runInBackground)
                .setOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!runInBackground) {
                            navView.getMenu().getItem(1).setChecked(true);
                        } else {
                            finish();
                        }
                    }
                }).setOnPositiveButtonClickListener(
                        new EditTextDialog.Builder.OnPositiveButtonClickListener() {
                            @Override
                            public void onClick(EditTextDialog dialog,
                                    TextInputLayout textInputLayout, String password) {
                                if (PasswordPref.hasPassword(mContext)) {
                                    if (PasswordPref.isPasswordCorrect(mContext, password)) {
                                        dialog.dismiss();
                                        changeFragment(PasswordContainerFragment.class.getName());
                                        mCurrentPosition = 0;
                                        runInBackground = false;
                                        mIsDialogShowing = false;
                                    } else {
                                        textInputLayout.setError("密码错误");
                                    }
                                } else {
                                    if (password.length() < 6) {
                                        textInputLayout.setError("密码长度不能小于6");
                                    } else {
                                        PasswordPref.savePassword(mContext, password);
                                        dialog.dismiss();
                                        changeFragment(PasswordContainerFragment.class.getName());
                                        mCurrentPosition = 0;
                                        Snackbar.make(flContainer, "密码设置成功，请牢记密码",
                                                Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }
                        })
                .create().show();
        mIsDialogShowing = true;
    }

    private void changeFragment(String fragmentName) {
        Fragment fragment = mFragmentManager.findFragmentByTag(fragmentName);
        if (fragment != null) {
            if (fragment == mCurrentFragment) {
                return;
            } else {
                mFragmentManager.beginTransaction().show(fragment).commit();
            }
        } else {
            fragment = Fragment.instantiate(mContext, fragmentName);
            mFragmentManager.beginTransaction().add(R.id.fl_container, fragment, fragmentName)
                    .commit();
        }

        if (mCurrentFragment != null) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).commit();
        }

        mCurrentFragment = fragment;
    }

    private long mLastBackTime = 0L;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (System.currentTimeMillis() - mLastBackTime < 1000) {
            finish();
        } else {
            mLastBackTime = System.currentTimeMillis();
            Snackbar.make(flContainer, "再按一次退出应用", Snackbar.LENGTH_LONG).show();
        }
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentFragment == mFragmentManager
                .findFragmentByTag(PasswordContainerFragment.class.getName()) && runInBackground
                && !mIsDialogShowing) {
            openPassword();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }
}