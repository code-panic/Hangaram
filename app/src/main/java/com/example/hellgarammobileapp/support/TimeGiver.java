package com.example.hellgarammobileapp.support;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeGiver {
    static long mNow;
    static Date mDate;

    static {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
    }

    public static String getYear() {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy");
        Log.d("TimeGiver",mFormat.format(mDate));
        return mFormat.format(mDate);
    }

    public static String getMonth() {
        SimpleDateFormat mFormat = new SimpleDateFormat("MM");
        Log.d("TimeGiver",mFormat.format(mDate));
        return mFormat.format(mDate);
    }

    public static String getDate() {
        SimpleDateFormat mFormat = new SimpleDateFormat("dd");
        Log.d("TimeGiver",mFormat.format(mDate));
        return mFormat.format(mDate);
    }
}
