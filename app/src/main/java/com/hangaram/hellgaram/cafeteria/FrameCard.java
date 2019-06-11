package com.hangaram.hellgaram.cafeteria;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hangaram.hellgaram.datebase.DataBaseHelper;
import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.time.TimeGiver;

public class FrameCard extends LinearLayout {

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    int mDateGap;

    public FrameCard(Context context, int dateGap) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_cafeteria,this, false);

        this.mDateGap = dateGap;

        mDataBaseHelper = new DataBaseHelper(getContext());
        mDatabase = mDataBaseHelper.getReadableDatabase();

        String[] args = {TimeGiver.getYear(mDateGap), TimeGiver.getMonth(mDateGap), TimeGiver.getDate(mDateGap)};
        Cursor cursor = mDatabase.rawQuery("select year, month, date from " + DataBaseHelper.TABLE_NAME_MEAL + " where year = ? and month = ? and date = ?", args);

        cursor.moveToFirst();

        Bundle bundle = new Bundle();

        bundle.putInt("dateGap",dateGap);
        bundle.putString("lunch",cursor.getString(cursor.getColumnIndex("lunch")));
        bundle.putString("dinner",cursor.getString(cursor.getColumnIndex("dinner")));

        ShowCard showCard = new ShowCard();
        UpdateCard updateCard = new UpdateCard();

        showCard.setArguments(bundle);
        updateCard.setArguments(bundle);

        if(cursor.getCount() > 0)
            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, showCard).commit();
        else
            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, updateCard).commit();
    }


}
