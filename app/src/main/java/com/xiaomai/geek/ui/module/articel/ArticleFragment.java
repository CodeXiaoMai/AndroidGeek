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
        final List<Chapter> chapters = new ArrayList<>();
        List<Article> rxJavaArticles = new ArrayList<>();
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(一)", "http://www.jianshu.com/p/464fa025229e"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(二)", "http://www.jianshu.com/p/8818b98c44e2"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(三)", "http://www.jianshu.com/p/128e662906af"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(四)", "http://www.jianshu.com/p/bb58571cdb64"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(五)", "http://www.jianshu.com/p/0f2d6c2387c9"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(六)", "http://www.jianshu.com/p/e4c6d7989356"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(七)", "http://www.jianshu.com/p/9b1304435564"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(八)", "http://www.jianshu.com/p/a75ecf461e02"));
        rxJavaArticles.add(new Article("给初学者的RxJava2.0教程(九)", "http://www.jianshu.com/p/36e0f7f43a51"));
        chapters.add(new Chapter("RxJava",
                "RxJava Description",
                "http://reactivex.io/assets/Rx_Logo_S.png",
                "https://github.com/ReactiveX/RxJava",
                rxJavaArticles));
        mAdapter.setNewData(chapters);
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
