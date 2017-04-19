package com.pactera.enterprisesecretary.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by xiaoyang on 2017/4/19.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("===================position======================"+ position);
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

}
