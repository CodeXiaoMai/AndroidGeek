package com.xiaomai.geek.ui.module.article;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomai.geek.R;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.ui.MainActivity;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.geek.ui.widget.TitleView;

/**
 * Created by xiaomai on 2017/12/7.
 */

public class ArticleListFragment extends BaseFragment {

    public static ArticleListFragment newInstance() {
        return new ArticleListFragment();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        TitleView titleView = view.findViewById(R.id.title_view);
        titleView.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).openDrawer();
                }
            }
        });
    }
}
