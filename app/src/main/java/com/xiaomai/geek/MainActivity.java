package com.xiaomai.geek;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.module.password.PasswordListActivity;
import com.xiaomai.geek.ui.widget.MenuItemView;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        super.initViews();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        initHeadViews();
    }

    private void initHeadViews() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        MenuItemView menuItemArticle = (MenuItemView) headerView.findViewById(R.id.menu_item_article);
        menuItemArticle.setSelected(true);
        menuItemArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final MenuItemView menuItemPassword = (MenuItemView) headerView.findViewById(R.id.menu_item_password);
        menuItemPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PasswordListActivity.class));
            }
        });

        MenuItemView menuItemSettings = (MenuItemView) headerView.findViewById(R.id.menu_item_settings);
        menuItemSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}