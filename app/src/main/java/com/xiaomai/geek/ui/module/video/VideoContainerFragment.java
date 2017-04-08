package com.xiaomai.geek.ui.module.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaomai.geek.ui.base.BaseFragment;

/**
 * Created by XiaoMai on 2017/4/8 16:53.
 */

public class VideoContainerFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText("Video");
        return textView;
    }
}
