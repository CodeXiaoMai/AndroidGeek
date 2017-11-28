package com.xiaomai.geek.ui.module.password;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseActivity;

/**
 * Created by XiaoMai on 2017/11/24.
 */

public class DoorActivity extends BaseActivity {

    private EditText mPasswordView;
    private TextView mCancelView;
    private TextView mConfirmView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_door;
    }

    @Override
    protected void initViews() {
        super.initViews();

        mPasswordView = findViewById(R.id.edit_password);
        mCancelView = findViewById(R.id.bt_cancel);
        mConfirmView = findViewById(R.id.bt_ok);

        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mConfirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PasswordListActivity.class));
                finish();
            }
        });
    }
}
