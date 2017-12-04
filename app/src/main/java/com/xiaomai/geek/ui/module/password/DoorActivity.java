package com.xiaomai.geek.ui.module.password;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaomai.geek.BuildConfig;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.ActivityStacks;
import com.xiaomai.geek.contract.password.DoorContract;
import com.xiaomai.geek.presenter.password.DoorPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;

/**
 * Created by XiaoMai on 2017/11/24.
 */

public class DoorActivity extends BaseActivity implements DoorContract.View {
    public static final String EXTRA_CLEAR = "EXTRA_CLEAR";
    private TextView mTitleView;
    private TextInputLayout mPasswordLayout;
    private EditText mPasswordView;
    private TextView mCancelView;
    private TextView mConfirmView;

    private DoorContract.Presenter mPresenter;

    private boolean mCancel;

    public static void launch(@NonNull Context context, boolean clear) {
        final Intent intent = new Intent(context, DoorActivity.class);
        intent.putExtra(EXTRA_CLEAR, clear);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_door;
    }

    @Override
    protected void initViews() {
        super.initViews();

        mTitleView = findViewById(R.id.tv_title);
        mPasswordLayout = findViewById(R.id.layout_password);
        mPasswordView = findViewById(R.id.edit_password);
        mCancelView = findViewById(R.id.bt_cancel);
        mConfirmView = findViewById(R.id.bt_ok);

        if (BuildConfig.DEBUG) {
            mPasswordView.setText("123456");
        }

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPasswordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordLayout.setCounterEnabled(true);
        mPasswordLayout.setCounterMaxLength(16);
        mPasswordLayout.setPasswordVisibilityToggleEnabled(true);
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void afterInitViews() {
        super.afterInitViews();
        mPresenter = new DoorPresenter(mContext);
        mPresenter.attachView(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void configView(final boolean isInit) {
        if (isInit) {
            mTitleView.setText(R.string.please_set_password);
        } else {
            mPasswordLayout.setHint(getString(R.string.please_input_password));
        }
        mConfirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = mPasswordView.getText().toString().trim();
                if (isInit) {
                    if (TextUtils.isEmpty(password) || password.length() < 6) {
                        mPasswordLayout.setError(getString(R.string.password_length_must_more_than_six));
                        return;
                    }
                    mPresenter.savePassword(password);
                } else {
                    mPresenter.verifyPassword(password);
                }
            }
        });
    }

    @Override
    public void onSavePasswordFinish() {
        startActivity(new Intent(mContext, PasswordListActivity.class));
        finish();
    }

    @Override
    public void onVerifyFinish(boolean pass) {
        if (pass) {
            if (!getIntent().getBooleanExtra(EXTRA_CLEAR, false)) {
                startActivity(new Intent(mContext, PasswordListActivity.class));
            }
            finish();
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        } else {
            mPasswordLayout.setError(getString(R.string.password_error));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getIntent().getBooleanExtra(EXTRA_CLEAR, false)) {
            ActivityStacks.clear();
        }
        finish();
    }
}
