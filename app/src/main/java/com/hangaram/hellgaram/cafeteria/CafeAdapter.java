package com.hangaram.hellgaram.cafeteria;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CafeAdapter extends FragmentPagerAdapter {
    private static final String TAG = "CafeAdapter";

    CafeAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int dateGap) {
        Log.d(TAG, "dateGap: " + dateGap);

        FrameCard frameCard = new FrameCard();
        Bundle bundle = new Bundle();

        bundle.putInt("dateGap", dateGap);

        frameCard.setArguments(bundle);
        return frameCard;
    }

    @Override
    public int getCount() {
        /*최대 슬라이드 장을 100장 까지만*/
        return 100;
    }
}
