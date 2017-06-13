package com.xiaomai.geek.ui.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.InputMethodUtils;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.pref.AccountPref;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.AccountComponent;
import com.xiaomai.geek.di.component.DaggerAccountComponent;
import com.xiaomai.geek.di.module.AccountModule;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.presenter.github.LoginPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.view.ILoginView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public class LoginActivity extends BaseLoadActivity implements ILoginView, IComponent<AccountComponent> {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.userName_layout)
    TextInputLayout userNameLayout;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login_btn)
    Button loginBtn;

    @Inject
    LoginPresenter mPresenter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();
        mPresenter.attachView(this);
    }

    private void initViews() {
        setTitle("登录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String name = AccountPref.getLoginUserName(this);
        userName.setText(name);
        if (!TextUtils.isEmpty(name)) {
            password.requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public AccountComponent getComponent() {
        return DaggerAccountComponent.builder()
                .applicationComponent(GeekApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .accountModule(new AccountModule())
                .build();
    }

    @Override
    public void loginSuccess(User user) {
        Snackbar.make(loginBtn, "登录成功", Snackbar.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void error(Throwable e) {
        Snackbar.make(loginBtn, "登录失败！请检查用户名或密码", Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        String name = userName.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
            InputMethodUtils.hideSoftInput(this);
            mPresenter.login(name, pass);
        } else {
            Snackbar.make(userName, "用户名或密码不能为空", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public String getLoadingMessage() {
        return "登录中...";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
