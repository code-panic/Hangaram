package com.example.hellgarammobileapp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hellgarammobileapp.Fragment.MealFragment.MealFragment;
import com.example.hellgarammobileapp.Fragment.TimeTableFragment.TimeTableFragment;
import com.example.hellgarammobileapp.Fragment.TransportFragment.TransportFragment;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private MealFragment mealFragment = new MealFragment();
    private TimeTableFragment timeTableFragment = new TimeTableFragment();
    private TransportFragment transportFragment = new TransportFragment();

    private int pageCount = 3;

    public ContentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mealFragment;
            case 1:
                return timeTableFragment;
            case 2:
                return transportFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.pageCount;
    }
}
