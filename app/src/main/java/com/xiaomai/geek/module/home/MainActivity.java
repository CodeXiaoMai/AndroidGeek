
package com.xiaomai.geek.module.home;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.module.base.BaseActivity;
import com.xiaomai.geek.module.news.NewsContainerFragment;

import butterknife.BindView;

/**
 * Create by XiaoMai on 2017/3/24 18:15.
 */
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START, true);
        // return true 菜单标为选中状态
        switchTab(getFragmentName(item.getItemId()));
        return true;
    }

    private String getFragmentName(int itemId) {
        switch (itemId) {
            case R.id.nav_git:
                return GitContainerFragment.class.getName();
            case R.id.nav_news:
                return NewsContainerFragment.class.getName();
            case R.id.nav_photos:
                return GitContainerFragment.class.getName();
            case R.id.nav_videos:
                return GitContainerFragment.class.getName();
            default:
                return GitContainerFragment.class.getName();
        }
    }

    /**
     * 根据Fragment的name，显示或者添加</br>
     * 如果找到的fragment不为null，判断是否为当前的fragment，如果是直接返回，否则显示fragment，并把
     * 上一个fragment隐藏，将这个fragment设置为当前的fragment。
     * 如果找到的fragment为null，则初始化一个fragment，并显示。
     * 
     * @param fragmentName
     */
    private void switchTab(String fragmentName) {
        Fragment fragment = mFragmentManager.findFragmentByTag(fragmentName);
        if (fragment != null) {
            if (fragment == mCurrentFragment) {
                return;
            }
            mFragmentManager.beginTransaction().show(fragment).commit();
        } else {
            fragment = Fragment.instantiate(this, fragmentName);
            mFragmentManager.beginTransaction().add(R.id.fl_container, fragment).commit();
        }

        if (mCurrentFragment != null) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).commit();
        }
        mCurrentFragment = fragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | attributes.flags;
            // 将侧边栏顶部延伸至status bar
            drawerLayout.setFitsSystemWindows(true);
            // 将主页面顶部延伸至status bar
            drawerLayout.setClipToPadding(false);
        }
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        navView.setCheckedItem(R.id.nav_git);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void showContent(Object data) {

    }
}
