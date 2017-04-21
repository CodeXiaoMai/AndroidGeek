
package com.xiaomai.geek.ui.module.password;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.xiaomai.geek.ui.base.BaseFragmentPagerAdapter;

/**
 * Created by XiaoMai on 2017/3/30 10:03.
 */

public class PasswordContainerAdapter extends BaseFragmentPagerAdapter<Fragment> {

    public static final String TITLE_PASSWORD_ALL = "全部";

    public static final String TITLE_PASSWORD_STAR = "收藏";

    public static final String TITLE_PASSWORD_CATEGORY = "分类";

    public static final String TITLE_PASSWORD_SETTINGS = "设置";

    public PasswordContainerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = list.get(position);
        if (fragment instanceof PasswordListFragment) {
            return TITLE_PASSWORD_ALL;
        } else if (fragment instanceof PasswordSettingFragment) {
            return TITLE_PASSWORD_SETTINGS;
        } else {
            return super.getPageTitle(position);
        }
    }
}
