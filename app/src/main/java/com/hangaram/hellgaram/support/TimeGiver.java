package com.hangaram.hellgaram.support;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static String getHour() {
        SimpleDateFormat mFormat = new SimpleDateFormat("HH");
        Log.d("TimeGiver",mFormat.format(mDate));
        return mFormat.format(mDate);
    }

    public static String getMinuate() {
        SimpleDateFormat mFormat = new SimpleDateFormat("mm");
        Log.d("TimeGiver",mFormat.format(mDate));
        return mFormat.format(mDate);
    }


    public static String getYear(int num){
        Calendar calendar;

        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, num);

        return calendar.get(Calendar.YEAR) + "";
    }

    public static String getMonth(int num){
        Calendar calendar;

        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, num);

        return calendar.get(Calendar.MONTH) + 1 + "";
    }

    public static String getDate(int num){
        Calendar calendar;

        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, num);

        return calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

}
