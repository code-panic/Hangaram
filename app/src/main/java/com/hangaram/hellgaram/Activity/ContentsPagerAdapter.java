package com.hangaram.hellgaram.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hangaram.hellgaram.Fragment.MealFragment.MealFragment;
import com.hangaram.hellgaram.Fragment.SettingFragmnet.SettingFragment;
import com.hangaram.hellgaram.Fragment.TimeTableFragment.TimeTableFragment;
import com.hangaram.hellgaram.Fragment.TransportationFragment.TransportationFragment;
import com.hangaram.hellgaram.Fragment.WeatherFragment.WeatherFragment;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private MealFragment mealFragment = new MealFragment();
    private TimeTableFragment timeTableFragment = new TimeTableFragment();
    private TransportationFragment transportationFragment = new TransportationFragment();
    private WeatherFragment weatherFragment = new WeatherFragment();
    private SettingFragment settingFragment = new SettingFragment();

    private int pageCount = 5;

    public ContentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MealFragment();
            case 1:
                return new TimeTableFragment();
            case 2:
                return new TransportationFragment();
            case 3:
                return new WeatherFragment();
            case 4:
                return new SettingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.pageCount;
    }
}
