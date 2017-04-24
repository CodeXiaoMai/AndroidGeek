package com.xiaomai.geek.ui.module.github;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.data.api.GitHubApi;
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
            case GitHubApi.LANG_JAVA:
                return "Java";

            case GitHubApi.LANG_HTML:
                return "Html";

            case GitHubApi.LANG_PYTHON:
                return "Python";

            case GitHubApi.LANG_OC:
                return "Objective-C";

            case GitHubApi.LANG_SWIFT:
                return "Swift";

            case GitHubApi.LANG_BASH:
                return "Shell";

            default:
                AppLog.w("未知语言");
                break;
        }
        return "";
    }
}
