package com.hangaram.hellgaram.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hangaram.hellgaram.R;


public class CafeMainFragment extends Fragment {
    private static final String Tag = "CafeteriaFragment";

    private ViewPager mViewPager;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        mView = inflater.inflate(R.layout.fragment_cafeteria, container, false);

        mViewPager = mView.findViewById(R.id.viewpager);

        mViewPager.setAdapter(new CafeAdapter(getFragmentManager()));
        mViewPager.setCurrentItem(0);

        return mView;
    }
}
