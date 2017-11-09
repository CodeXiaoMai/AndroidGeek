package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaomai.geek.contract.password.AddEditPasswordContract;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.presenter.password.AddEditPasswordPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;

import java.util.List;

/**
 * Created by xiaomai on 2017/10/29.
 */

public class AddEditPasswordFragment extends BaseFragment implements AddEditPasswordContract.View{

    private AddEditPasswordContract.Presenter mPresenter;

    public static AddEditPasswordFragment newInstance() {
        return new AddEditPasswordFragment();
    }

    @Override
    protected BasePresenter getPresenter() {
        mPresenter = new AddEditPasswordPresenter(PasswordRepository.getInstance(mContext));
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter.savePassword("xiaomai", "username", "password", "note");
        mPresenter.savePassword("baidu", "username", "password", "note");
        mPresenter.savePassword("qq", "username", "password", "note");
        mPresenter.savePassword("xiaomai1", "username", "password", "note");
        mPresenter.savePassword("baidu2", "username", "password", "note");
        mPresenter.savePassword("qq3", "username", "password", "note");
        return super.onCreateView(inflater, container, savedInstanceState);
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
