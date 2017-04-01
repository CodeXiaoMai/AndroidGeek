
package com.xiaomai.geek.ui.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XiaoMai on 2017/3/30 10:17.
 */

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    protected List<Fragment> fragments;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public void setFragments(Fragment[] fragments) {
        setFragments(Arrays.asList(fragments));
    }
}
