package com.hangaram.hellgaram.cafeteria;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class CafeAdapter extends PagerAdapter {

    Context mContext;

    public CafeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int dateGap) {
        return new FrameCard(mContext, dateGap);
    }

    @Override
    public int getCount() {
        //Integer 최대 범위까지
        return 100;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return false;
    }
}
