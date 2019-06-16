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

        /* card 맨 위의 날짜 텍스트 내용 정하기*/
        String dateString = "";

        if(dateGap == 0) {
            dateString = "오늘" ;
        } else if(dateGap == 1){
            dateString = "내일";
        } else {
            dateString = TimeGiver.getYear(dateGap) + "." + TimeGiver.getMonth(dateGap) + "." + TimeGiver.getDate(dateGap) + " (" + TimeGiver.getWeek(dateGap) + ")";
        }

        /*급식정보를 받아온 경우 card_show 를 로드한다
        * 받지 못한 경우 card_update 를 로드한다*/
        if (cursor.getCount() > 0) {
            View cardContent = inflater.inflate(R.layout.card_show, cardContainer,  true);

            cursor.moveToFirst();

            TextView dateText = cardContent.findViewById(R.id.card_show_date_text);
            final TextView cafeText = cardContent.findViewById(R.id.card_show_cafe_text);
            TabLayout tabLayout = cardContent.findViewById(R.id.card_show_tab_layout);

            final String lunch = cursor.getString(cursor.getColumnIndex("lunch"));
            final String dinner = cursor.getString(cursor.getColumnIndex("dinner"));

            dateText.setText(dateString);

            cafeText.setText(lunch);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0)
                        cafeText.setText(lunch);
                    else
                        cafeText.setText(dinner);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else {
            final View cardContent = inflater.inflate(R.layout.card_update, cardContainer, true);

            TextView dateText = cardContent.findViewById(R.id.card_update_date_text);
            Button downloadButton = cardContainer.findViewById(R.id.card_update_download_button);

            dateText.setText(dateString);

            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CafeTask cafeTask = new CafeTask(getContext());
                    cafeTask.execute(Integer.parseInt(TimeGiver.getYear(dateGap)), Integer.parseInt(TimeGiver.getMonth(dateGap)));
                }
            });
        }
        return view;
    }
}
