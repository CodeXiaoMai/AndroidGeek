package com.xiaomai.geek.ui.module.articel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Article;
import com.xiaomai.geek.data.module.Chapter;
import com.xiaomai.geek.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ArticleFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    ChapterListAdapter mAdapter;

    public static ArticleFragment newInstance() {
        ArticleFragment fragment = new ArticleFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_article, null, false);
        unbinder = ButterKnife.bind(this, contentView);
        initViews();
        return contentView;
    }

    private void initViews() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ChapterListAdapter(null);
        recyclerView.setAdapter(mAdapter);
        final List<Chapter> list = new ArrayList<>();
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("RxJava 一", "http://www.jianshu.com/p/464fa025229e"));
        list.add(new Chapter("RxJava",
                "RxJava Description",
                "http://reactivex.io/assets/Rx_Logo_S.png",
                "https://github.com/ReactiveX/RxJava",
                articles));
        mAdapter.setNewData(list);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Chapter chapter = mAdapter.getItem(i);
                ChapterActivity.launch(getActivity(), chapter);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
