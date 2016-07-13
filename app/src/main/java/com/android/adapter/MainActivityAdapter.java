package com.android.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * 主页面的适配器，用于fragment的切换
 * Created by liujq on 2016/4/12.
 */
public class MainActivityAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public MainActivityAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public Fragment getSubFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = fragmentList.get(position);
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            String title = bundle.getString("title");
            return title == null ? "" : title;
        }
        return "";
    }

}
