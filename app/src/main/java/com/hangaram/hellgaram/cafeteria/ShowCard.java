package com.hangaram.hellgaram.cafeteria;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.time.TimeGiver;

public class ShowCard extends Fragment {
    private static final String TAG = "ShowCard";

    private TextView mDateText;
    private TextView mCafeText;
    private TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle bundle) {
        View view = inflater.inflate(R.layout.card_show, container, false);

        int dateGap = getArguments().getInt("dateGap");

        mDateText = view.findViewById(R.id.date_text);
        mCafeText = view.findViewById(R.id.cafeteria_text);
        mTabLayout = view.findViewById(R.id.tab_layout);

        mDateText.setText(TimeGiver.getYear(dateGap) + "." + TimeGiver.getMonth(dateGap) + "." + TimeGiver.getDate(dateGap));
        setCafeText();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    CafeFragment.sIsLunch = true;
                else
                    CafeFragment.sIsLunch = false;

                setCafeText();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void setCafeText() {
        if (CafeFragment.sIsLunch) {
            mCafeText.setText(getArguments().getString("lunch"));
            mTabLayout.getTabAt(0).select();
        } else {
            mCafeText.setText(getArguments().getString("dinner"));
            mTabLayout.getTabAt(1).select();
        }
    }
}
