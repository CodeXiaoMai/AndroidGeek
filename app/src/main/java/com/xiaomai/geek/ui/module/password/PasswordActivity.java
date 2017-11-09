package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseActivity;

/**
 * Created by XiaoMai on 2017/11/7.
 */

public class PasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AddEditPasswordFragment addEditPasswordFragment = AddEditPasswordFragment.newInstance();

        PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container_view, addEditPasswordFragment);
        transaction.add(R.id.container_view, passwordListFragment);
        transaction.commit();
    }
}