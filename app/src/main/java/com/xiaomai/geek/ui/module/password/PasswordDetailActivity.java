
package com.xiaomai.geek.ui.module.password;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.NotificationUtils;
import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.PasswordDetailPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.view.IPasswordDetailView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/3/30 18:31.
 */

public class PasswordDetailActivity extends BaseActivity implements IPasswordDetailView {

    public static final String EXTRA_PASSWORD = "extra_password";

    @BindView(R.id.iv_icon)
    ImageView ivIcon;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @BindView(R.id.flb_edit)
    FloatingActionButton flbEdit;

    @BindView(R.id.collapsingToolBar)
    CollapsingToolbarLayout collapsingToolBar;

    @BindView(R.id.tv_userName)
    TextView tvUserName;

    @BindView(R.id.tv_password)
    TextView tvPassword;

    @BindView(R.id.tv_category)
    TextView tvCategory;

    @BindView(R.id.tv_note)
    TextView tvNote;

    @BindView(R.id.layout_category)
    RelativeLayout layoutCategory;

    @BindView(R.id.layout_note)
    CardView layoutNote;

    @BindView(R.id.ic_category)
    ImageView icCategory;

    @BindView(R.id.layout_userName)
    RelativeLayout layoutUserName;

    @BindView(R.id.layout_password)
    RelativeLayout layoutPassword;

    private Password mPassword;

    private PasswordDetailPresenter mPresenter;

    public static void launch(Context context, Password password) {
        Intent intent = new Intent(context, PasswordDetailActivity.class);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);
        ButterKnife.bind(this);
        mPresenter = new PasswordDetailPresenter();
        mPresenter.attachView(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        if (null != intent) {
            mPassword = intent.getParcelableExtra(EXTRA_PASSWORD);
        }
        mPresenter.getPasswordDetail(mContext, mPassword.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.password_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_star:
                starPassword();
                return true;
            case R.id.menu_delete:
                deletePassword();
                return true;
            case R.id.menu_publish:
                NotificationUtils.showNotification(mContext, mPassword,
                        NotificationUtils.TYPE_PASSWORD);
                NotificationUtils.showNotification(mContext, mPassword,
                        NotificationUtils.TYPE_USER_NAME);
                Snackbar.make(toolBar, "账号密码已发送到通知栏", Snackbar.LENGTH_LONG).show();
                return true;
            case R.id.menu_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mPassword.getPlatform() + "\n账号："
                        + mPassword.getUserName() + "\n密码：" + mPassword.getPassword());
                startActivity(Intent.createChooser(intent, "分享到"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePassword() {
        new AlertDialog.Builder(mContext).setMessage("确定删除此条记录？").setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deletePassword(mContext, mPassword.getId());
                    }
                }).create().show();
    }

    private void starPassword() {
        boolean isStar = mPassword.isStar();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PasswordDBHelper.COLUMN_STAR, !isStar);
        mPresenter.startPassword(
                isStar ? PasswordDetailPresenter.TYPE_UN_STAR : PasswordDetailPresenter.TYPE_STAR,
                mContext, mPassword.getId(), contentValues);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mPassword.isStar()) {
            menu.findItem(R.id.menu_star).setIcon(R.drawable.ic_favorite_on);
        } else {
            menu.findItem(R.id.menu_star).setIcon(R.drawable.ic_favorite_off);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void showContent(Password password) {
        mPassword = password;
        // 这句话触发onPrepareOptionsMenu()方法
        invalidateOptionsMenu();

        collapsingToolBar.setTitle(password.getPlatform());
        tvUserName.setText(password.getUserName());
        tvPassword.setText(password.getPassword());
        String category = password.getCategory();
        tvCategory.setText(TextUtils.isEmpty(category) ? "默认" : category);
        String note = password.getNote();
        if (TextUtils.isEmpty(note)) {
            layoutNote.setVisibility(View.GONE);
        } else {
            layoutNote.setVisibility(View.VISIBLE);
            tvNote.setText(note);
        }
    }

    @Override
    public void onStarComplete(boolean success) {
        if (success) {
            Snackbar.make(toolBar, "收藏成功", Snackbar.LENGTH_LONG).show();
            mPassword.setStar(!mPassword.isStar());
            showContent(mPassword);
        } else {
            Snackbar.make(toolBar, "收藏失败", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUnStarComplete(boolean success) {
        if (success) {
            Snackbar.make(toolBar, "已取消收藏", Snackbar.LENGTH_LONG).show();
            mPassword.setStar(!mPassword.isStar());
            showContent(mPassword);
        } else {
            Snackbar.make(toolBar, "取消失败", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeleteComplete(boolean success) {
        if (success) {
            EventBus.getDefault().post(new PasswordEvent(PasswordEvent.TYPE_DELETE, mPassword));
            finish();
        } else {
            Snackbar.make(toolBar, "删除失败", Snackbar.LENGTH_LONG).show();
        }
    }

    @OnClick({
            R.id.layout_userName, R.id.layout_password, R.id.flb_edit
    })
    public void onClick(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        switch (view.getId()) {
            case R.id.flb_edit:
                EditAccountActivity.launch(mContext, mPassword);
                break;
            case R.id.layout_userName:
                clipboardManager.setPrimaryClip(
                        ClipData.newPlainText("userName", tvUserName.getText().toString()));
                Snackbar.make(layoutCategory, "账号已复制到剪切板", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.layout_password:
                clipboardManager.setPrimaryClip(
                        ClipData.newPlainText("password", tvPassword.getText().toString()));
                Snackbar.make(layoutCategory, "密码已复制到剪切板", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Subscribe
    public void onHandlePasswordEvent (PasswordEvent passwordEvent) {
        if (passwordEvent.getType() == PasswordEvent.TYPE_UPDATE) {
            mPresenter.getPasswordDetail(mContext, mPassword.getId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
