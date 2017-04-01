package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseFragment;

/**
 * Created by XiaoMai on 2017/4/1 9:49.
 */

public class PasswordSettingFragment extends BaseFragment {

    public static PasswordSettingFragment newInstance() {
        PasswordSettingFragment fragment = new PasswordSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_setting, null);
        return view;
    }
}
