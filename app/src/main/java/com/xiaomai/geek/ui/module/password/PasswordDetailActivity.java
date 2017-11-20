package com.xiaomai.geek.ui.module.password;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.NotificationUtils;
import com.xiaomai.geek.common.utils.ShareUtils;
import com.xiaomai.geek.contract.password.PasswordDetailContract;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.password.PasswordDetailPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by XiaoMai on 2017/11/15.
 */

public class PasswordDetailActivity extends BaseActivity implements PasswordDetailContract.View {
    public static final String EXTRA_PASSWORD = "extra_password";

    private CollapsingToolbarLayout collapsingToolBar;
    private TextView mUserNameView;
    private TextView mPasswordView;
    private TextView mNoteView;

    private Password mPassword;

    private PasswordDetailPresenter mPresenter;


    public static void launch(@NonNull Context context, @NonNull Password password) {
        Intent intent = new Intent(context, PasswordDetailActivity.class);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        mPresenter = new PasswordDetailPresenter(PasswordRepository.getInstance(this));
        mPresenter.attachView(this);
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_password_detail;
    }

    @Override
    public void initViews() {
        super.initViews();

        Toolbar toolBar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);

        collapsingToolBar = findViewById(R.id.collapsingToolBar);

        FloatingActionButton floatingActionButton = findViewById(R.id.flb_edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEditPasswordActivity.launch(mContext, mPassword);
            }
        });

        View userNameLayoutView = findViewById(R.id.layout_userName);
        mUserNameView = findViewById(R.id.tv_userName);
        View passwordLayoutView = findViewById(R.id.layout_password);
        mPasswordView = findViewById(R.id.tv_password);
        mNoteView = findViewById(R.id.tv_note);

        userNameLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard("userName", mUserNameView.getText().toString(), "账号已复制到剪切板");
            }
        });

        passwordLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard("password", mPasswordView.getText().toString(), "密码已复制到剪切板");
            }
        });

        mNoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard("note", mNoteView.getText().toString(), "备注已复制到剪切板");
            }
        });
    }

    private void copyToClipboard(String label, String value, String snack) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, value));
        Snackbar.make(mNoteView, snack, Snackbar.LENGTH_LONG).show();
    }

    private void loadData() {
        showContent((Password) getParcelableFromIntent(EXTRA_PASSWORD));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_password_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_publish:
                // TODO: 2017/11/20 检查通知权限
                NotificationUtils.showNotification(mContext, mPassword);
                Snackbar.make(mNoteView, "账号密码已发送到通知栏", Snackbar.LENGTH_LONG).show();
                return true;
            case R.id.menu_delete:
                deletePassword();
                return true;
            case R.id.menu_share:
                sharePassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sharePassword() {
        ShareUtils.share(this, mPassword.getPlatform() + "\n账号："
                + mPassword.getUserName() + "\n密码：" + mPassword.getPassword());
    }

    private void deletePassword() {
        new AlertDialog.Builder(mContext).setMessage("确定删除此条记录？").setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deletePassword(mPassword);
                    }
                }).create().show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showContent(Password data) {
        mPassword = data;
        invalidateOptionsMenu();

        collapsingToolBar.setTitle(data.getPlatform());
        setValueOrNull(mUserNameView, data.getUserName());
        setValueOrNull(mPasswordView, data.getPassword());
        setValueOrNull(mNoteView, data.getNote());
    }

    private void setValueOrNull(TextView textView, String value) {
        if (TextUtils.isEmpty(value)) {
            textView.setText(R.string.empty);
        } else {
            textView.setText(value);
        }
    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(mNoteView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void deleteSuccess(@NonNull Password password) {
        EventBus.getDefault().post(new PasswordEvent(PasswordEvent.TYPE_DELETE, password));
        finish();
    }

    @Subscribe
    public void onHandlePasswordEvent(PasswordEvent passwordEvent) {
        if (passwordEvent.getType() == PasswordEvent.TYPE_UPDATE) {
            showContent(passwordEvent.getPassword());
        }
    }
}