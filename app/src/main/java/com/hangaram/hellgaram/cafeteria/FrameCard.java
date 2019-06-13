package com.hangaram.hellgaram.cafeteria;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hangaram.hellgaram.datebase.DataBaseHelper;
import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.time.TimeGiver;

public class FrameCard extends Fragment {
    private static final String FrameCard = "FrameCard";

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_frame, container, false);

        int dateGap = getArguments().getInt("dateGap");

        mDataBaseHelper = new DataBaseHelper(getContext());
        mDatabase = mDataBaseHelper.getReadableDatabase();

        String[] args = {TimeGiver.getYear(dateGap), TimeGiver.getMonth(dateGap), TimeGiver.getDate(dateGap)};
        Cursor cursor = mDatabase.rawQuery("select * from " + DataBaseHelper.TABLE_NAME_MEAL + " where year = ? and month = ? and date = ?", args);

        Bundle bundle = new Bundle();
        bundle.putInt("dateGap", dateGap);

        if (cursor.getCount() > 0) {
            ShowCard showCard = new ShowCard();

            cursor.moveToFirst();

            bundle.putString("lunch", cursor.getString(cursor.getColumnIndex("lunch")));
            bundle.putString("dinner", cursor.getString(cursor.getColumnIndex("dinner")));

            showCard.setArguments(bundle);

            ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.card_frame_layout, showCard).commit();
        } else {
            UpdateCard updateCard = new UpdateCard();

            updateCard.setArguments(bundle);

            ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.card_frame_layout, updateCard).commit();
        }

        return view;
    }
}
