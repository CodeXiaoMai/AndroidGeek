package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomai.geek.contract.password.PasswordsContract;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.password.PasswordsPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;

import java.util.List;

/**
 * Created by xiaomai on 2017/10/26.
 */

public class PasswordListFragment extends BaseFragment implements PasswordsContract.View{

    private PasswordsContract.Presenter mPresenter;

    public static PasswordListFragment newInstance() {
        return new PasswordListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PasswordsPresenter(PasswordRepository.getInstance(mContext));
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter.loadPasswords("i");
        mPresenter.loadPasswords("1");
        mPresenter.loadPasswords("3");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showContent(List<Password> data) {

    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void showEmpty() {

    }
}
