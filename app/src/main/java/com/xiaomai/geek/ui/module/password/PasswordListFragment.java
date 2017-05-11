
package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.NotificationUtils;
import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.password.PasswordListPresenter;
import com.xiaomai.geek.ui.base.BaseLoadFragment;
import com.xiaomai.mvp.lce.ILceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/3/30 10:26.
 */

public class PasswordListFragment extends BaseLoadFragment implements ILceView<List<Password>>{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.empty_title)
    TextView emptyTitle;

    @BindView(R.id.empty_desc)
    TextView emptyDesc;

    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;

    private PasswordListAdapter mAdapter;

    PasswordListPresenter mPresenter = new PasswordListPresenter();

    public static PasswordListFragment newInstance() {
        PasswordListFragment fragment = new PasswordListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_list, null);
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        initViews();
        return view;
    }

    private void initViews() {
        mAdapter = new PasswordListAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Password password = mAdapter.getItem(i);
                        PasswordDetailActivity.launch(mContext, password);
                    }
                });
        mAdapter.setOnPublishClickListener(new PasswordListAdapter.OnPublishClickListener() {
            @Override
            public void onPublicClick(Password password) {
                NotificationUtils.showNotification(mContext, password,
                        NotificationUtils.TYPE_PASSWORD);
                NotificationUtils.showNotification(mContext, password,
                        NotificationUtils.TYPE_USER_NAME);
                Snackbar.make(recyclerView, "账号密码已发送到通知栏", Snackbar.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPresenter.getAllPasswords(mContext);
    }

    @Override
    public void showContent(List<Password> data) {
        Collections.sort(data);
        recyclerView.setVisibility(View.VISIBLE);
        emptyRootLayout.setVisibility(View.GONE);
        mAdapter.setNewData(data);
    }

    @Override
    public void showError(Throwable e) {
        error(e);
    }

    @Override
    public void showEmpty() {
        emptyRootLayout.setVisibility(View.VISIBLE);
        emptyTitle.setText("密码箱是空的！");
        emptyDesc.setText("赶快创建一个吧！");
        recyclerView.setVisibility(View.GONE);
    }

    @Subscribe
    public void onHandlePasswordEvent(final PasswordEvent passwordEvent) {
        final Password password = passwordEvent.getPassword();
        switch (passwordEvent.getType()) {
            case PasswordEvent.TYPE_DELETE:
                Snackbar.make(recyclerView, "删除成功", Snackbar.LENGTH_LONG)
                        .setAction("撤消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PasswordDBHelper.getInstance(mContext).insert(password);
                                mPresenter.getAllPasswords(mContext);
                            }
                        }).show();
            case PasswordEvent.TYPE_CLEAR:
            case PasswordEvent.TYPE_IMPORT:
            case PasswordEvent.TYPE_UPDATE:
                mPresenter.getAllPasswords(mContext);
                break;
            case PasswordEvent.TYPE_ADD:
                mPresenter.getAllPasswords(mContext);
                Snackbar.make(recyclerView, "保存成功", Snackbar.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
