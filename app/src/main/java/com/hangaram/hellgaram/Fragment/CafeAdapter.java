package com.hangaram.hellgaram.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CafeAdapter extends FragmentStatePagerAdapter {

    public CafeAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        CafeCardFragment mCafeCardFragment = new CafeCardFragment();

        /* Fragment는 기본 생성자 이외에는 허용을 하지 않기 때문에
        Bundle 객체로 넘겨준다. */
        //넘겨줄 값의 개수 설정하기
        Bundle bundle = new Bundle(1);

        //position 값 넣어 전달하기
        bundle.putInt("position",position);
        mCafeCardFragment.setArguments(bundle);

        return mCafeCardFragment;
    }

    @Override
    public int getCount() {
        //Integer 최대 범위까지
        return Integer.MAX_VALUE;
    }

}
