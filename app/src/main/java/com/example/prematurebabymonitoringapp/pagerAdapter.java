package com.example.prematurebabymonitoringapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import org.jetbrains.annotations.NotNull;

public class pagerAdapter extends FragmentPagerAdapter {
    int numberOfTabs;

    public pagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm,BEHAVIOR_SET_USER_VISIBLE_HINT);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                basicInfoFragment tab1 = new basicInfoFragment();
                return tab1;
            case 1:
                healthFragment tab2 = new healthFragment();
                return tab2;
            case 2:
                othersFragment tab3 = new othersFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
