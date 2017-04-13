
package com.xiaomai.geek.ui.module;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.MainActivity;
import com.xiaomai.geek.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/4/13 16:50.
 */

public class AboutUsFragment extends BaseFragment {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, null);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setSupportActionBar(toolBar);
        try {
            tvVersion.setText("版本号:" + getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity) getActivity()).openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
