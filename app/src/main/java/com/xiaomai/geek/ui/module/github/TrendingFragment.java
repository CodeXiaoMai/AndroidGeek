package com.xiaomai.geek.ui.module.github;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by XiaoMai on 2017/4/21.
 */

public class TrendingFragment extends BaseFragment {

    public static final String EXTRA_LANGUAGE = "extra_language";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.empty_title)
    TextView emptyTitle;
    @BindView(R.id.empty_desc)
    TextView emptyDesc;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;
    @BindView(R.id.error_title)
    TextView errorTitle;
    @BindView(R.id.error_desc)
    TextView errorDesc;
    @BindView(R.id.retry_btn)
    TextView retryBtn;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.error_root_layout)
    RelativeLayout errorRootLayout;

    public static TrendingFragment newInstance(String tag) {
        TrendingFragment fragment = new TrendingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_LANGUAGE, tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_github_trending, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.empty_root_layout, R.id.error_root_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.empty_root_layout:
                break;
            case R.id.error_root_layout:
                break;
        }
    }
}
