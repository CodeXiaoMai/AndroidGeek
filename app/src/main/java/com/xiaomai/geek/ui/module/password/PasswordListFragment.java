
package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.PasswordListPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.geek.view.IPasswordSearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/3/30 10:26.
 */

public class PasswordListFragment extends BaseFragment implements IPasswordSearchView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.empty_title)
    TextView emptyTitle;

    @BindView(R.id.empty_desc)
    TextView emptyDesc;

    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;

    private SearchView mSearchView;

    private PasswordListAdapter mAdapter;

    private String mKeywords;

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
        setHasOptionsMenu(true);
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
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPresenter.getAllPasswords(mContext);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showContent(List<Password> data) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyRootLayout.setVisibility(View.GONE);
        mAdapter.setNewData(data);
        mAdapter.setKeyWords(mKeywords);
    }

    @Override
    public void showError(Throwable e) {

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
            case PasswordEvent.TYPE_ADD:
                mPresenter.getAllPasswords(mContext);
                break;
            case PasswordEvent.TYPE_UPDATE:
                mPresenter.getAllPasswords(mContext);
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.password_container_menu, menu);
        MenuItem item = menu.findItem(R.id.search_view);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setQueryHint("搜索...");
        final SearchView.SearchAutoComplete completeText = (SearchView.SearchAutoComplete) mSearchView
                .findViewById(R.id.search_src_text);
        completeText.setAdapter(new ArrayAdapter<>(mContext, R.layout.list_item_textview,
                R.id.text, new String[] {
                        "小米", "百度", "QQ", "微信"
                }));
        completeText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                completeText.setText(parent.getItemAtPosition(position).toString());
            }
        });
        completeText.setThreshold(0);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mKeywords = query;
                mPresenter.getPasswordsByKeywords(mContext, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mKeywords = newText;
                if (TextUtils.isEmpty(newText)) {
                    mPresenter.getAllPasswords(mContext);
                } else {
                    mPresenter.getPasswordsByKeywords(mContext, newText);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_view:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onSearchEmpty() {
        emptyRootLayout.setVisibility(View.VISIBLE);
        emptyTitle.setText("没有搜索到任何内容");
        emptyDesc.setText("请重试！");
        recyclerView.setVisibility(View.GONE);
    }
}
