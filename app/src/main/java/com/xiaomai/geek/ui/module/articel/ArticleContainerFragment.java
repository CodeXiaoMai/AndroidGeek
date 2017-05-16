package com.xiaomai.geek.ui.module.articel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.MainActivity;
import com.xiaomai.geek.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XiaoMai on 2017/4/8 16:53.
 */

public class ArticleContainerFragment extends BaseFragment {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.contentView)
    FrameLayout contentView;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_article_container, null, false);
        unbinder = ButterKnife.bind(this, contentView);
        initViews();
        return contentView;
    }

    private void initViews() {
        toolBar.setTitle("干货");
        ((MainActivity)getActivity()).setSupportActionBar(toolBar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArticleFragment fragment = ArticleFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentView, fragment, fragment.getClass().getName()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity)getActivity()).openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
