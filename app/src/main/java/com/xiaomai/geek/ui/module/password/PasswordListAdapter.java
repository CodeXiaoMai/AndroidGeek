
package com.xiaomai.geek.ui.module.password;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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

    private String keyWords;

    private SpannableString spannablePlatform;

    private SpannableString spannableUsername;

    private OnPublishClickListener listener;

    private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(
            Color.parseColor("#009ad6"));;

    public PasswordListAdapter(List<Password> data) {
        super(R.layout.item_password, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final Password password) {
        String platform = password.getPlatform();
        if (!TextUtils.isEmpty(keyWords) && platform.contains(keyWords)) {
            spannablePlatform = new SpannableString(platform);
            int start = platform.indexOf(keyWords);
            int end = start + keyWords.length();
            spannablePlatform.setSpan(foregroundColorSpan, start, end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            holder.setText(R.id.tv_platform, spannablePlatform);
        } else {
            holder.setText(R.id.tv_platform, platform);
        }

        String userName = password.getUserName();
        if (!TextUtils.isEmpty(keyWords) && userName.contains(keyWords)) {
            spannableUsername = new SpannableString(userName);
            int start = userName.indexOf(keyWords);
            int end = start + keyWords.length();
            spannableUsername.setSpan(foregroundColorSpan, start, end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            holder.setText(R.id.tv_userName, spannableUsername);
        } else {
            holder.setText(R.id.tv_userName, userName);
        }
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

        holder.getView(R.id.iv_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPublicClick(password);
                }
            }
        });
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public void setOnPublishClickListener(OnPublishClickListener listener) {
        this.listener = listener;
    }

    public interface OnPublishClickListener {
        void onPublicClick(Password password);
    }
}
