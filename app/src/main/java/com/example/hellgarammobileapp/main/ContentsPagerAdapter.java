package com.example.hellgarammobileapp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hellgarammobileapp.Fragment.MealFragment.MealFragment;
import com.example.hellgarammobileapp.Fragment.TimeTableFragment.TimeTableFragment;
import com.example.hellgarammobileapp.Fragment.TransportFragment.TransportFragment;
import com.example.hellgarammobileapp.Fragment.WeatherFragment.WeatherFragment;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private MealFragment mealFragment = new MealFragment();
    private TimeTableFragment timeTableFragment = new TimeTableFragment();
    private TransportFragment transportFragment = new TransportFragment();
    private WeatherFragment weatherFragment = new WeatherFragment();

    private int pageCount = 4;

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
            case 3:
                return weatherFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.pageCount;
    }
}
