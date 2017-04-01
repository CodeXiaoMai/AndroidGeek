
package com.xiaomai.geek.ui.module.password;

import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.ui.widget.CircleView;

import java.util.List;

/**
 * Created by XiaoMai on 2017/3/30 11:24.
 */

public class PasswordListAdapter extends BaseQuickAdapter<Password> {
    public PasswordListAdapter(List<Password> data) {
        super(R.layout.item_password, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final Password password) {
        holder.setText(R.id.tv_platform, password.getPlatform());
        holder.setText(R.id.tv_userName, password.getUserName());
        CircleView icon = holder.getView(R.id.circle_view_icon);
        icon.setText(password.getPlatform().substring(0, 1));
        holder.getView(R.id.iv_eye).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.setText(R.id.tv_password, password.getPassword());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.setText(R.id.tv_password, "******");
                }
                return true;
            }
        });
    }
}
