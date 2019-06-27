package com.example.tpkkoja.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int NoOfTabs;
    public PagerAdapter(FragmentManager fm,int NumbOfTabs) {
        super(fm);
        this.NoOfTabs= NumbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFragment home = new HomeFragment();
                return home;
            case 1:
                ProfileFragment profile = new ProfileFragment();
                return profile;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }

}