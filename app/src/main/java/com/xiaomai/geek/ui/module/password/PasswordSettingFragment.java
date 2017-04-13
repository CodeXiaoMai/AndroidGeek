
package com.xiaomai.geek.ui.module.password;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.TimeUtils;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.data.pref.PasswordPref;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.PasswordSettingPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.geek.ui.widget.EditTextDialog;
import com.xiaomai.geek.view.IPasswordSettingView;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/4/1 9:49.
 */

public class PasswordSettingFragment extends BaseFragment implements IPasswordSettingView {

    private static final int REQUEST_CODE_IMPORT = 0x0001;

    private static final int REQUEST_CODE_BACKUP = 0x0002;

    @BindView(R.id.layout_clear_data)
    LinearLayout layoutClearData;

    @BindView(R.id.layout_backup)
    LinearLayout layoutBackup;

    @BindView(R.id.layout_modify_password)
    LinearLayout layoutModifyPassword;

    @BindView(R.id.layout_import)
    LinearLayout layoutImport;

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
            R.id.layout_clear_data, R.id.layout_backup, R.id.layout_modify_password,
            R.id.layout_import
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_clear_data:
                clearData();
                break;
            case R.id.layout_backup:
                Intent back = new Intent(mContext, FileExploreActivity.class);
                back.putExtra(FileExploreActivity.EXTRA_TYPE, FileExploreActivity.TYPE_FOLDER);
                startActivityForResult(back, REQUEST_CODE_BACKUP);
                break;
            case R.id.layout_modify_password:
                showModifyDialog();
                break;
            case R.id.layout_import:
                Intent intent = new Intent(mContext, FileExploreActivity.class);
                intent.putExtra(FileExploreActivity.EXTRA_TYPE, FileExploreActivity.TYPE_FILE);
                startActivityForResult(intent, REQUEST_CODE_IMPORT);
                break;
        }
    }

    private int mode = 0;

    private void showModifyDialog() {

        new EditTextDialog.Builder(mContext).setTitle("修改密码").setHint("请输入原密码")
                .setOnPositiveButtonClickListener(
                        new EditTextDialog.Builder.OnPositiveButtonClickListener() {
                            @Override
                            public void onClick(EditTextDialog dialog,
                                    TextInputLayout textInputLayout, String password) {
                                if (mode == 0) {
                                    if (PasswordPref.isPasswordCorrect(mContext, password)) {
                                        textInputLayout.setHint("请输入新密码");
                                        textInputLayout.getEditText().setText("");
                                        mode = 1;
                                    } else {
                                        textInputLayout.setError("密码错误！");
                                    }
                                } else {
                                    if (password.length() < 6) {
                                        textInputLayout.setError("密码长度不能小于6");
                                    } else {
                                        PasswordPref.savePassword(mContext, password);
                                        dialog.dismiss();
                                        Snackbar.make(layoutBackup, "密码修改成功！", Snackbar.LENGTH_LONG)
                                                .show();
                                        mode = 0;
                                    }
                                }
                            }
                        })
                .create().show();
    }

    private void clearData() {
        new EditTextDialog.Builder(mContext).setTitle("清空数据").setOnPositiveButtonClickListener(
                new EditTextDialog.Builder.OnPositiveButtonClickListener() {
                    @Override
                    public void onClick(EditTextDialog dialog, TextInputLayout textInputLayout,
                            String password) {
                        if (PasswordPref.isPasswordCorrect(mContext, password)) {
                            mPresenter.deleteAllPasswords(mContext);
                            dialog.dismiss();
                        } else {
                            textInputLayout.setError("密码错误，请重试！");
                        }
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
        if (count > 0) {
            Snackbar.make(layoutBackup, "成功备份" + count + "条数据", Snackbar.LENGTH_LONG).show();
        } else if (count == 0) {
            Snackbar.make(layoutBackup, "没有备份任何数据", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(layoutBackup, "备份失败，请检查路径或权限", Snackbar.LENGTH_LONG).show();
        }
    }

    private AlertDialog mProgressDialog;

    @Override
    public void showBackupIng() {
        mProgressDialog = new ProgressDialog.Builder(mContext).setMessage("正在备份中").create();
        mProgressDialog.show();
    }

    @Override
    public void importComplete(int count) {
        if (count > 0) {
            Snackbar.make(layoutBackup, "成功导入" + count + "条数据", Snackbar.LENGTH_LONG).show();
            EventBus.getDefault()
                    .post(new PasswordEvent(PasswordEvent.TYPE_IMPORT, new Password()));
        } else {
            Snackbar.make(layoutBackup, "没有导入任何数据，请检查文件是否正确", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void importFail(String message) {
        Snackbar.make(layoutBackup, "导入失败，请检查文件", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMPORT) {
            if (resultCode == Activity.RESULT_OK) {
                String filePath = data.getStringExtra(FileExploreActivity.EXTRA_PATH);
                mPresenter.importPassword(mContext, filePath);
            }
        } else if (requestCode == REQUEST_CODE_BACKUP) {
            if (resultCode == Activity.RESULT_OK) {
                final String filePath = data.getStringExtra(FileExploreActivity.EXTRA_PATH);
                new EditTextDialog.Builder(mContext).setTitle("您选择的存储目录为:" + filePath, 14)
                        .setIsPassword(false).setHint("请输入文件名")
                        .setEditTextContent(
                                "backup_" + TimeUtils.getDate(new Date(), "yyyy_MM_dd") + ".xml")
                        .setOnPositiveButtonClickListener(
                                new EditTextDialog.Builder.OnPositiveButtonClickListener() {
                                    @Override
                                    public void onClick(EditTextDialog dialog,
                                            TextInputLayout textInputLayout, String password) {
                                        mPresenter.backupPasswords(mContext,
                                                filePath + "/" + password);
                                        dialog.dismiss();
                                    }
                                })
                        .create().show();
            }
        }
    }
}
