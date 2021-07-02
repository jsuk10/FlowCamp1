package com.example.apps;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class MainFragmentAdapter extends FragmentPagerAdapter {

    public MainFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Tab1Fragment.newInstance();
            case 1:
                return Tab2Fragment.newInstance();
            case 2:
                return Tab2Fragment.newInstance();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}