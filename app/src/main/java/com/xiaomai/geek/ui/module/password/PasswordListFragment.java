package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.contract.password.PasswordsContract;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.presenter.password.PasswordsPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.geek.ui.widget.ErrorView;

import java.util.List;

/**
 * Created by xiaomai on 2017/10/26.
 */

public class PasswordListFragment extends BaseFragment implements PasswordsContract.View {

    private PasswordsContract.Presenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ErrorView mErrorView;
    private ErrorView mEmptyView;

    private Adapter mAdapter;

    public static PasswordListFragment newInstance() {
        return new PasswordListFragment();
    }

    @Override
    protected BasePresenter getPresenter() {
        mPresenter = new PasswordsPresenter(PasswordRepository.getInstance(mContext));
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_password_list, container, false);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        mErrorView = rootView.findViewById(R.id.error_view);
        mEmptyView = rootView.findViewById(R.id.empty_view);

        loadData();
        return rootView;
    }

    private void loadData() {
        mPresenter.loadPasswords("i");
        mPresenter.loadPasswords("1");
        mPresenter.loadPasswords("3");
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent(List<Password> data) {
        mAdapter.setContent(data);

        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable e) {
        mErrorView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Password> passwords;

        public void setContent(List<Password> passwords) {
            this.passwords = passwords;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            Holder holder;
            if (viewHolder instanceof Holder) {
                holder = (Holder) viewHolder;
                Password password = getItem(position);
                holder.platformView.setText(password.getPlatform());
                holder.passwordView.setText(password.getPassword());
            }
        }

        @Override
        public int getItemCount() {
            return passwords == null ? 0 : passwords.size();
        }

        public Password getItem(int position) {
            return passwords.get(position);
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {
        TextView platformView;
        TextView passwordView;
        private Holder(View itemView) {
            super(itemView);

            platformView = itemView.findViewById(R.id.tv_platform);
            passwordView = itemView.findViewById(R.id.tv_password);
        }
    }
}
