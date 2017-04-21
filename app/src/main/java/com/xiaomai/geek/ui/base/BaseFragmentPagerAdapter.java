
package com.xiaomai.geek.ui.base;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XiaoMai on 2017/3/30 10:17.
 */

public abstract class BaseFragmentPagerAdapter<T> extends FragmentPagerAdapter {

    protected List<T> list;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setList(T[] fragments) {
        setList(Arrays.asList(fragments));
    }
}
