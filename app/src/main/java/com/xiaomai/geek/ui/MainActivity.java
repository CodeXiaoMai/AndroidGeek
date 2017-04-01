
package com.xiaomai.geek.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.module.password.PasswordContainerFragment;

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
                changeFragment(PasswordContainerFragment.class.getName());
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
            mFragmentManager.beginTransaction().add(R.id.fl_container, fragment).commit();
        }

        if (mCurrentFragment != null) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).commit();
        }

        mCurrentFragment = fragment;
    }

}
