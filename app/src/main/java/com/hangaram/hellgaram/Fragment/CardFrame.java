package com.hangaram.hellgaram.Fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.support.TimeGiver;

public class CardFrame extends LinearLayout {

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    int mDateGap;

    public CardFrame(Context context, int dateGap) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_cafeteria,this, false);

        this.mDateGap = dateGap;

        mDataBaseHelper = new DataBaseHelper(getContext());
        mDatabase = mDataBaseHelper.getReadableDatabase();

        String[] args = {TimeGiver.getYear(mDateGap), TimeGiver.getMonth(mDateGap), TimeGiver.getDate(mDateGap)};
        Cursor cursor = mDatabase.rawQuery("select year, month, date from " + DataBaseHelper.TABLE_NAME_MEAL + " where year = ? and month = ? and date = ?", args);

        if(cursor.getCount() > 0)
            ((Activity)context).getFragmentManager().beginTransaction().replace(R.id.frame_layout, new CardCafe()).commit();
        else
            ((Activity)context).getFragmentManager().beginTransaction().replace(R.id.frame_layout, new CardUpdate()).commit();
    }


}
