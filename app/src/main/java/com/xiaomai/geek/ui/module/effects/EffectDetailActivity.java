package com.xiaomai.geek.ui.module.effects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.xiaomai.geek.data.module.Effect;
import com.xiaomai.geek.ui.base.BaseActivity;

/**
 * Created by XiaoMai on 2017/11/22.
 */

public class EffectDetailActivity extends BaseActivity {

    public static final String EFFECT_EXTRA = "EFFECT_EXTRA";

    public static void launch(Context context, Effect effect) {
        Intent intent = new Intent(context, EffectDetailActivity.class);
        intent.putExtra(EFFECT_EXTRA, effect);
        context.startActivity(intent);
    }

    @Override
    protected void setContainerView(FrameLayout containerView) {
        super.setContainerView(containerView);
        Effect effect = (Effect) getParcelableFromIntent(EFFECT_EXTRA);
        final View view = LayoutInflater.from(mContext).inflate(effect.getLayoutRes(), containerView, false);
        containerView.addView(view);
        setTitle(effect.getTitle());
    }
}