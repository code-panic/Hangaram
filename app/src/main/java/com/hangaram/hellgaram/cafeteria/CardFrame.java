package com.hangaram.hellgaram.cafeteria;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hangaram.hellgaram.datebase.DataBaseHelper;
import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.time.TimeGiver;

import java.sql.Time;

public class CardFrame extends Fragment {
    private static final String TAG = "CardFrame";

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_frame, container, false);

        final int dateGap = getArguments().getInt("dateGap");

        mDataBaseHelper = new DataBaseHelper(getContext());
        mDatabase = mDataBaseHelper.getReadableDatabase();

        String[] args = {TimeGiver.getYear(dateGap), TimeGiver.getMonth(dateGap), TimeGiver.getDate(dateGap)};
        Cursor cursor = mDatabase.rawQuery("select * from " + DataBaseHelper.TABLE_NAME_MEAL + " where year = ? and month = ? and date = ?", args);

        RelativeLayout cardContainer = view.findViewById(R.id.card_layout_container);
        String dateString = TimeGiver.getYear(dateGap) + "." + TimeGiver.getMonth(dateGap) + "." + TimeGiver.getDate(dateGap);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Log.d(TAG, dateGap + "번째 CardContainer 에 레이아웃 Inflate 하기");
            View cardContent = inflater.inflate(R.layout.card_show, cardContainer,  true);

            TextView dateText = cardContent.findViewById(R.id.card_show_date_text);
            TextView cafeText = cardContent.findViewById(R.id.card_show_cafe_text);
            TabLayout tabLayout = cardContent.findViewById(R.id.card_show_tab_layout);

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

            dateText.setText(dateString);
            if(CafeFragment.sIsLunch)
                cafeText.setText(cursor.getString(cursor.getColumnIndex("lunch")));
            else
                cafeText.setText(cursor.getString(cursor.getColumnIndex("dinner")));

        } else {
            View cardContent = inflater.inflate(R.layout.card_update, cardContainer, true);

            TextView dateText = cardContent.findViewById(R.id.card_update_date_text);
            Button downloadButton = cardContainer.findViewById(R.id.card_update_download_button);

            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CafeTask cafeTask = new CafeTask();
                    cafeTask.execute(Integer.parseInt(TimeGiver.getYear(dateGap)), Integer.parseInt(TimeGiver.getMonth(dateGap)));
                }
            });

            dateText.setText(dateString);
        }

        return view;
    }
}
