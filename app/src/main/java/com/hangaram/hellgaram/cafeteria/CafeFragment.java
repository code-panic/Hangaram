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
    private static CafeFragment instace;
    private ViewPager mViewPager;

    public static CafeFragment getInstance(){
        if(instace == null){
            instace = new CafeFragment();
        }
        return instace;
    }

    private CafeAdapter cafeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);

        mViewPager = view.findViewById(R.id.viewpager);

        cafeAdapter = new CafeAdapter(getChildFragmentManager());
        mViewPager.setAdapter(cafeAdapter);
        mViewPager.setCurrentItem(0);

        return view;
    }

    public void refresh(){
        cafeAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(cafeAdapter);
    }
}
