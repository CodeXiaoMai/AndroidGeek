package com.xiaomai.geek.ui.module.password;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.xiaomai.geek.R;
import com.xiaomai.geek.contract.password.AddEditPasswordContract;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.password.AddEditPasswordPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.widget.GeneratePasswordView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xiaomai on 2017/10/29.
 */

public class AddEditPasswordActivity extends BaseActivity implements AddEditPasswordContract.View {
    private static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";
    public static final int MODE_CREATE = 1;
    public static final int MODE_UPDATE = 2;

    private EditText mEditPlatformView;
    private EditText mEditUserNameView;
    private EditText mEditPasswordView;
    private EditText mEditNoteView;
    private TextInputLayout mLayoutPlatformView;
    private TextInputLayout mLayoutUserNameView;
    private TextInputLayout mLayoutPasswordView;
    private ImageView circleViewIcon;

    private AlertDialog mDialog;

    private String mPasswordId;

    private String mPlatform;

    private String mUserName;

    private String mPassword;

    private String mNote;

    private final ColorGenerator mGenerator = ColorGenerator.MATERIAL;

    private int mCurrentMode = MODE_CREATE;

    private AddEditPasswordContract.Presenter mPresenter;

    public static void launch(@NonNull Context context, @NonNull Password password) {
        Intent intent = new Intent(context, AddEditPasswordActivity.class);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_password_edit;
    }

    @Override
    public void initViews() {
        super.initViews();

        circleViewIcon = findViewById(R.id.circle_view_icon);
        mEditPlatformView = (EditText) findViewById(R.id.edit_platform);
        mEditPlatformView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String platform = mEditPlatformView.getText().toString().trim();
                if (platform.length() > 0) {
                    String substring = platform.substring(0, 1);
                    TextDrawable textDrawable = TextDrawable.builder().beginConfig().toUpperCase()
                            .endConfig().buildRound(substring, mGenerator.getColor(platform));
                    circleViewIcon.setImageDrawable(textDrawable);
                } else {
                    circleViewIcon.setImageResource(R.mipmap.ic_launcher);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEditUserNameView = (EditText) findViewById(R.id.edit_userName);
        mEditPasswordView = (EditText) findViewById(R.id.edit_password);
        mEditNoteView = (EditText) findViewById(R.id.edit_note);

        findViewById(R.id.iv_generate_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneratePasswordView generatePasswordView = new GeneratePasswordView(mContext);
                generatePasswordView.setCallback(new GeneratePasswordView.Callback() {
                    @Override
                    public void onCancel() {
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onConfirm(String content) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mEditPasswordView.setText(content);
                        }
                    }
                });

                mDialog = new AlertDialog.Builder(mContext)
                        .setView(generatePasswordView)
                        .create();

                mDialog.show();
            }
        });
        findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });
    }

    @Override
    protected void afterInitViews() {
        super.afterInitViews();
        mPresenter = new AddEditPasswordPresenter(PasswordRepository.getInstance(mContext));
        mPresenter.attachView(this);
    }

    @Override
    protected void loadData() {
        Password password = (Password) getParcelableFromIntent(EXTRA_PASSWORD);
        if (password == null) {
            return;
        }

        mCurrentMode = MODE_UPDATE;

        mPasswordId = password.getId();

        mEditPlatformView.setText(password.getPlatform());
        mEditUserNameView.setText(password.getUserName());
        mEditPasswordView.setText(password.getPassword());
        mEditNoteView.setText(password.getNote());
    }

    private void savePassword() {
        mPlatform = mEditPlatformView.getText().toString().trim();
        mUserName = mEditUserNameView.getText().toString().trim();
        mPassword = mEditPasswordView.getText().toString().trim();
        mNote = mEditNoteView.getText().toString().trim();

        Password password = new Password(mPlatform, mUserName, mPassword, mNote);
        if (mCurrentMode == MODE_CREATE) {
            mPresenter.savePassword(password);
        } else {
            password.setId(mPasswordId);
            mPresenter.updatePassword(password);
        }
    }

    @Override
    public void onSaveSuccess() {
        EventBus.getDefault().post(new PasswordEvent(PasswordEvent.TYPE_ADD));
        finish();
    }

    @Override
    public void onUpdateSuccess(@NonNull Password password) {
        EventBus.getDefault().post(new PasswordEvent(PasswordEvent.TYPE_UPDATE, password));
        finish();
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
