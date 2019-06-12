package com.hangaram.hellgaram.cafeteria;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hangaram.hellgaram.R;


public class CafeFragment extends Fragment {
    private static final String TAG = "CafeteriaFragment";

    public static boolean sIsLunch = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewpager);

        Log.d(TAG,"1");
        viewPager.setAdapter(new CafeAdapter(getFragmentManager()));
        viewPager.setCurrentItem(0);

        return view;
    }
}
