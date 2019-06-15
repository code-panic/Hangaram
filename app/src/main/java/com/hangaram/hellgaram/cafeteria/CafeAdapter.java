package com.hangaram.hellgaram.cafeteria;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class CafeAdapter extends FragmentPagerAdapter {
    private static final String TAG = "CafeAdapter";

    CafeAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int dateGap) {
        Log.d(TAG, "dateGap: " + dateGap);

        CardFrame cardFrame = new CardFrame();
        Bundle bundle = new Bundle();

        bundle.putInt("dateGap", dateGap);

        cardFrame.setArguments(bundle);
        return cardFrame;
    }

    @Override
    public int getCount() {
        /*최대 슬라이드 장을 100장 까지만*/
        return 100;
    }
}
