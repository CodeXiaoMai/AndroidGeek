package com.xiaomai.geek.ui.module.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Video;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerMainComponent;
import com.xiaomai.geek.di.component.MainComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.VideoModule;
import com.xiaomai.geek.presenter.video.VideoPresenter;
import com.xiaomai.geek.ui.MainActivity;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by XiaoMai on 2017/4/8 16:53.
 */

public class VideoContainerFragment extends BaseFragment implements ILceView<List<Video>>, IComponent<MainComponent> {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;
    @BindView(R.id.error_root_layout)
    RelativeLayout errorRootLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    Unbinder unbinder;

    @Inject
    VideoPresenter mPresenter;

    private VideoListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_container, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        loadData();
        return view;
    }

    private void initView() {
        ((MainActivity) getActivity()).setSupportActionBar(toolBar);
        getActivity().setTitle("视频");
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mAdapter = new VideoListAdapter(null);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void loadData() {
        mPresenter.getVideos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void dismissLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showContent(List<Video> data) {
        recyclerView.setVisibility(View.VISIBLE);
        errorRootLayout.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.GONE);

        mAdapter.setNewData(data);
    }

    @Override
    public void showError(Throwable e) {
        recyclerView.setVisibility(View.GONE);
        errorRootLayout.setVisibility(View.VISIBLE);
        emptyRootLayout.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        errorRootLayout.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public MainComponent getComponent() {
        return DaggerMainComponent.builder()
                .applicationComponent(GeekApplication.get(getContext()).getComponent())
                .activityModule(new ActivityModule(this))
                .videoModule(new VideoModule())
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity) getActivity()).openDrawer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.empty_root_layout, R.id.error_root_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.empty_root_layout:
                loadData();
                break;
            case R.id.error_root_layout:
                loadData();
                break;
        }
    }
}
