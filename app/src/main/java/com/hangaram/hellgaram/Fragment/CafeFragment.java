package com.hangaram.hellgaram.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hangaram.hellgaram.R;


public class CafeFragment extends Fragment {
    private static final String Tag = "CafeteriaFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new CafeAdapter(getContext()));
        viewPager.setCurrentItem(0);

        return view;
    }
}
