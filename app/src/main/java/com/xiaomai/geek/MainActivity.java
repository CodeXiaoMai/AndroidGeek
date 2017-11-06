package com.xiaomai.geek;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xiaomai.geek.ui.module.password.PasswordListFragment;
import com.xiaomai.geek.ui.widget.MenuItemView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View mMenuItemArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = mNavigationView.getHeaderView(0);
        mMenuItemArticle = headerView.findViewById(R.id.menu_item_article);
        mMenuItemArticle.setSelected(true);
        mMenuItemArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final MenuItemView menuItemPassword = (MenuItemView) headerView.findViewById(R.id.menu_item_password);
        menuItemPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_container, passwordListFragment);
        transaction.commit();
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}