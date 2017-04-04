
package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/3/29 18:58.
 */

public class PasswordContainerFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    private PasswordContainerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_container, null);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);
        mAdapter = new PasswordContainerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(PasswordListFragment.newInstance());
        fragments.add(PasswordSettingFragment.newInstance());
//        fragments.add(PasswordListFragment.newInstance());
        mAdapter.setFragments(fragments);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 只在密码列表页面显示添加按钮
                Fragment fragment = mAdapter.getFragments().get(position);
                if (fragment instanceof PasswordSettingFragment) {
                    fabAdd.setVisibility(View.GONE);
                } else if (fragment instanceof PasswordListFragment) {
                    fabAdd.setVisibility(View.VISIBLE);
                    fabAdd.setTranslationY(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fabAdd.setVisibility(View.VISIBLE);
        fabAdd.setTranslationY(0);
    }

    @OnClick(R.id.fab_add)
    public void onClick() {
        EditAccountActivity.launch(mContext);
    }

}
