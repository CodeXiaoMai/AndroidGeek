package com.xiaomai.geek.ui.module.github;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.data.api.TrendingApi;
import com.xiaomai.geek.ui.base.BaseFragmentPagerAdapter;

/**
 * Created by XiaoMai on 2017/4/21.
 */

public class GitHubContainerAdapter extends BaseFragmentPagerAdapter<String> {

    public GitHubContainerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TrendingFragment.newInstance(list.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String language = list.get(position);
        switch (language) {
            case TrendingApi.LANG_JAVA:
                return "Java";

            case TrendingApi.LANG_OC:
                return "Objective-C";

            case TrendingApi.LANG_SWIFT:
                return "Swift";

            case TrendingApi.LANG_HTML:
                return "HTML";

            case TrendingApi.LANG_PYTHON:
                return "Python";

            case TrendingApi.LANG_BASH:
                return "Shell";

            default:
                AppLog.w("未知语言");
                break;
        }
        return "";
    }
}
