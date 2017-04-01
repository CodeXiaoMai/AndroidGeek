
package com.xiaomai.geek.ui.module.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.ActivityComponent;
import com.xiaomai.geek.di.component.DaggerActivityComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.EditAccountPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.widget.CircleView;
import com.xiaomai.geek.view.IEditAccountView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/3/30 14:01.
 */

public class EditAccountActivity extends BaseActivity
        implements IEditAccountView, IComponent<ActivityComponent> {

    public static final int MODE_CREATE = 1;

    public static final int MODE_UPDATE = 2;

    private static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";

    private int mCurrentMode = MODE_CREATE;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.edit_platform)
    EditText editPlatform;

    @BindView(R.id.edit_userName)
    EditText editUserName;

    @BindView(R.id.edit_password)
    EditText editPassword;

    @BindView(R.id.edit_note)
    EditText editNote;

    @BindView(R.id.layout_platform)
    TextInputLayout layoutPlatform;

    @BindView(R.id.layout_userName)
    TextInputLayout layoutUserName;

    @BindView(R.id.layout_password)
    TextInputLayout layoutPassword;

    @BindView(R.id.circle_view_icon)
    CircleView circleViewIcon;

    private String mPlatform;

    private String mUserName;

    private String mPassword;

    private String mNote;

    private int mPasswordId;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, EditAccountActivity.class));
    }

    public static void launch(Context context, Password password) {
        Intent intent = new Intent(context, EditAccountActivity.class);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
        initViews();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        Password password = intent.getParcelableExtra(EXTRA_PASSWORD);
        if (password == null) {
            return;
        }
        mCurrentMode = MODE_UPDATE;
        mPasswordId = password.getId();
        editPlatform.setText(password.getPlatform());
        editUserName.setText(password.getUserName());
        editPassword.setText(password.getPassword());
        editNote.setText(password.getNote());
    }

    private void initViews() {
        mPresenter = new EditAccountPresenter();
        mPresenter.attachView(this);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editPlatform.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String password = editPlatform.getText().toString().trim();
                if (password.length() > 0) {
                    String substring = password.substring(0, 1);
                    circleViewIcon.setText(substring);
                } else {
                    circleViewIcon.setText("");
                    circleViewIcon.setImageResource(R.drawable.ic_default_platform);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                layoutPlatform.setErrorEnabled(false);
            }
        });
        editUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                layoutUserName.setErrorEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 必须先调用setSupportActionBar(toolBar),否则菜单不显示
         */
        getMenuInflater().inflate(R.menu.edit_account_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_ok:
                saveInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveInfo() {
        mPlatform = editPlatform.getText().toString().trim();
        mUserName = editUserName.getText().toString().trim();
        mPassword = editPassword.getText().toString().trim();
        mNote = editNote.getText().toString().trim();
        if (mCurrentMode == MODE_CREATE) {
            ((EditAccountPresenter) mPresenter).savePassword(mContext, mPlatform, mUserName,
                    mPassword, mNote);
        } else if (mCurrentMode == MODE_UPDATE) {
            ((EditAccountPresenter) mPresenter).updatePassword(mContext, mPasswordId, mPlatform,
                    mUserName, mPassword, mNote);
        }
    }

    @Override
    public void onSaveComplete(boolean save) {
        if (save) {
            EventBus.getDefault().post(new PasswordEvent(PasswordEvent.TYPE_ADD, new Password()));
            Snackbar.make(toolBar, "保存成功", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(toolBar, "保存失败", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpdateComplete(boolean update) {
        if (update) {
            Snackbar.make(toolBar, "修改成功", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(toolBar, "修改失败", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPlatformError() {
        layoutPlatform.setError("不能为空");
        layoutPlatform.setEnabled(true);
    }

    @Override
    public void onUserNameError() {
        layoutUserName.setError("不能为空");
        layoutUserName.setEnabled(true);
    }

    @Override
    public void onPasswordError() {
        layoutPassword.setError("不能为空");
        layoutPassword.setEnabled(true);
    }

    @Override
    public Password generateRandomPassword() {
        return null;
    }

    @Override
    public ActivityComponent getComponent() {
        return DaggerActivityComponent.builder()
                .applicationComponent(GeekApplication.get(mContext).getComponent())
                .activityModule(new ActivityModule(this)).build();
    }
}
