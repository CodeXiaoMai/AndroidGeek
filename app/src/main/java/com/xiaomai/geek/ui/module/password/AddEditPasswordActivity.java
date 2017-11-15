package com.xiaomai.geek.ui.module.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.contract.password.AddEditPasswordContract;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.password.AddEditPasswordPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;

/**
 * Created by xiaomai on 2017/10/29.
 */

public class AddEditPasswordActivity extends BaseActivity implements AddEditPasswordContract.View {

    private EditText mEditPlatformView;
    private EditText mEditUserNameView;
    private EditText mEditPasswordView;
    private EditText mEditNoteView;
    private TextInputLayout mLayoutPlatformView;
    private TextInputLayout mLayoutUserNameView;
    private TextInputLayout mLayoutPasswordView;
    private ImageView circleViewIcon;

    private String mPlatform;

    private String mUserName;

    private String mPassword;

    private String mNote;


    private AddEditPasswordContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new AddEditPasswordPresenter(PasswordRepository.getInstance(mContext));
        mPresenter.attachView(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_password_edit;
    }

    @Override
    public void initViews() {
        super.initViews();

        mEditPlatformView = findViewById(R.id.edit_platform);
        mEditUserNameView = findViewById(R.id.edit_userName);
        mEditPasswordView = findViewById(R.id.edit_password);
        mEditNoteView = findViewById(R.id.edit_note);
        findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });
    }

    private void savePassword() {
        mPlatform = mEditPlatformView.getText().toString().trim();
        mUserName = mEditUserNameView.getText().toString().trim();
        mPassword = mEditPasswordView.getText().toString().trim();
        mNote = mEditNoteView.getText().toString().trim();
        mPresenter.savePassword(mPlatform, mUserName, mPassword, mNote);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showContent(Password data) {

    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(mEditNoteView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {

    }
}
