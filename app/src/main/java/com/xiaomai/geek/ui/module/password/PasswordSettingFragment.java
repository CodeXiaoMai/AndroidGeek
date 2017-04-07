
package com.xiaomai.geek.ui.module.password;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.data.pref.PasswordPref;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.PasswordSettingPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.geek.ui.widget.EditTextDialog;
import com.xiaomai.geek.view.IPasswordSettingView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/4/1 9:49.
 */

public class PasswordSettingFragment extends BaseFragment implements IPasswordSettingView {

    @BindView(R.id.layout_clear_data)
    LinearLayout layoutClearData;

    @BindView(R.id.layout_backup)
    LinearLayout layoutBackup;

    @BindView(R.id.layout_modify_password)
    LinearLayout layoutModifyPassword;

    private PasswordSettingPresenter mPresenter = new PasswordSettingPresenter();

    public static PasswordSettingFragment newInstance() {
        return new PasswordSettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_setting, null);
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
        return view;
    }

    @OnClick({
            R.id.layout_clear_data, R.id.layout_backup, R.id.layout_modify_password
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_clear_data:
                clearData();
                break;
            case R.id.layout_backup:
                mPresenter.backupPasswords(mContext);
                break;
            case R.id.layout_modify_password:
                showModifyDialog();
                break;
        }
    }

    private EditTextDialog mDialog;

    private void showModifyDialog() {
        mDialog = new EditTextDialog.Builder(mContext).setTitle("修改密码")
                .setHint("请输入新密码")
                .setOnPositiveButtonClickListener(
                        new EditTextDialog.Builder.OnPositiveButtonClickListener() {
                            @Override
                            public void onClick(TextInputLayout textInputLayout, String password) {
                                if (password.length() < 6) {
                                    textInputLayout.setError("密码长度不能小于6");
                                } else {
                                    PasswordPref.savePassword(mContext, password);
                                    mDialog.dismiss();
                                    Snackbar.make(layoutBackup, "密码修改成功！", Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            }
                        })
                .create();
        mDialog.show();
    }

    private void clearData() {
        new AlertDialog.Builder(mContext).setMessage("您确定要清空所有数据吗？").setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteAllPasswords(mContext);
                    }
                }).create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onDeleteAllPasswords(int count) {
        if (count > 0) {
            Snackbar.make(layoutClearData, "成功删除" + count + "条数据", Snackbar.LENGTH_LONG).show();
            EventBus.getDefault().post(new PasswordEvent(PasswordEvent.TYPE_CLEAR, new Password()));
        } else {
            Snackbar.make(layoutClearData, "没有删除任何数据", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackupComplete(int count) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        Snackbar.make(layoutBackup, "成功备份" + count + "条数据", Snackbar.LENGTH_LONG).show();
    }

    private AlertDialog mProgressDialog;


    @Override
    public void showBackupIng() {
        mProgressDialog = new ProgressDialog.Builder(mContext).setMessage("正在备份中").create();
        mProgressDialog.show();
    }
}
