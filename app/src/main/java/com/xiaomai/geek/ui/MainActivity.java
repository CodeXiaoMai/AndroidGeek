
package com.xiaomai.geek.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.pref.PasswordPref;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.module.password.PasswordContainerFragment;
import com.xiaomai.geek.ui.widget.EditTextDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private Fragment mCurrentFragment;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        navView.setNavigationItemSelectedListener(this);
        changeFragment(PasswordContainerFragment.class.getName());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_password_manage:
                openPassword();
                break;
            case R.id.menu_gitHub:
                changeFragment(PasswordContainerFragment.class.getName());
                break;
            case R.id.menu_video:
                changeFragment(PasswordContainerFragment.class.getName());
                break;
            case R.id.menu_article:
                changeFragment(PasswordContainerFragment.class.getName());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openPassword() {
        new EditTextDialog.Builder(mContext)
                .setTitle(PasswordPref.hasPassword(mContext) ? "打开密码箱" : "设置密码")
                .setCancelable(false).setOnPositiveButtonClickListener(
                        new EditTextDialog.Builder.OnPositiveButtonClickListener() {
                            @Override
                            public void onClick(EditTextDialog dialog,
                                    TextInputLayout textInputLayout, String password) {
                                if (PasswordPref.hasPassword(mContext)) {
                                    if (password.equals(PasswordPref.getPassword(mContext))) {
                                        dialog.dismiss();
                                        changeFragment(PasswordContainerFragment.class.getName());
                                    } else {
                                        textInputLayout.setError("密码错误");
                                    }
                                } else {
                                    if (password.length() < 6) {
                                        textInputLayout.setError("密码长度不能小于6");
                                    } else {
                                        PasswordPref.savePassword(mContext, password);
                                        dialog.dismiss();
                                        Snackbar.make(flContainer, "密码设置成功，请牢记密码",
                                                Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }
                        })
                .create().show();
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
}
