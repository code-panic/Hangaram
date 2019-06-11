package com.hangaram.hellgaram.cafeteria;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.time.TimeGiver;

public class ShowCard extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.card_cafeteria, container, false);

        int dateGap = bundle.getInt("dateGap");

        TextView dateText = view.findViewById(R.id.date_text);
        TextView cafeText = view.findViewById(R.id.cafeteria_text);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        dateText.setText(TimeGiver.getYear(dateGap) + "." + TimeGiver.getMonth(dateGap) + "." + TimeGiver.getDate(dateGap));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    CafeFragment.sIsLunch = true;
                 else
                    CafeFragment.sIsLunch = false;


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (CafeFragment.sIsLunch) {
            cafeText.setText(bundle.getString("lunch"));
            tabLayout.getTabAt(0).select();
        } else {
            cafeText.setText(bundle.getString("dinner"));
            tabLayout.getTabAt(1).select();
        }
        return view;
    }
}
