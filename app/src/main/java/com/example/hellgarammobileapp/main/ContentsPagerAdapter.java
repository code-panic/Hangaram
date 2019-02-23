package com.example.hellgarammobileapp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hellgarammobileapp.Fragment.MealFragment.MealFragment;
import com.example.hellgarammobileapp.Fragment.SettingFragmnet.SettingFragment;
import com.example.hellgarammobileapp.Fragment.TimeTableFragment.TimeTableFragment;
import com.example.hellgarammobileapp.Fragment.TransportationFragment.TransportationFragment;
import com.example.hellgarammobileapp.Fragment.WeatherFragment.WeatherFragment;

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
        switch (position){
            case 0:
                return mealFragment;
            case 1:
                return timeTableFragment;
            case 2:
                return transportationFragment;
            case 3:
                return weatherFragment;
            case 4:
                return settingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.pageCount;
    }
}
