
package com.xiaomai.geek.module.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by XiaoMai on 2017/3/27 18:35.
 */

public class NewsContainerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private List<String> titles;

    public NewsContainerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void setItems(List<Fragment> fragments, List<String> titles) {
        this.fragments = fragments;
        this.titles = titles;
        notifyDataSetChanged();
    }

    public void setItems(List<Fragment> fragments, String[] titles) {
        this.fragments = fragments;
        this.titles = Arrays.asList(titles);
        notifyDataSetChanged();
    }

    public void addItem(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        titles.remove(position);
        fragments.remove(position);
        notifyDataSetChanged();
    }

    public void swapItems(int fromPos, int toPos) {
        Collections.swap(titles, fromPos, toPos);
        Collections.swap(fragments, fromPos, toPos);
        notifyDataSetChanged();
    }

    public void modifyTitle(int position, String title) {
        titles.set(position, title);
        notifyDataSetChanged();
    }
}
