package com.hangaram.hellgaram.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.Fragment.DataBaseHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimetableEachProvider extends AppWidgetProvider {
    private static final String TAG = "TimetableEachProvider";

    private final String action = "UPDATE_WIDGET_TIMETABLE_EACH";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        //위젯 업데이트 시간 배열
        int[][] timeArray = {
                {0, 0},
                {9, 15},
                {10, 40},
                {12, 5},
                {14, 25},
                {15, 50},
        };

        for (int period = 0; period < 6; period++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timeArray[period][0]);
            calendar.set(Calendar.MINUTE, timeArray[period][1]);

            //보낼 인텐트 생성하기
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(action), PendingIntent.FLAG_UPDATE_CURRENT);

            //알람매니저 설정하기
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int mAppWidgetId : appWidgetIds)
            updateAppWidget(context, appWidgetManager, mAppWidgetId);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context, 0, new Intent(action), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //xml 파일 설정하기
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable_each);

        //현재 시간 설정하기
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        //데이터베이스 준비하기
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME_timetable, null, null, null, null, null, null, null);

        //현재 과목교시 보여주기
        updateViews.setTextViewText(R.id.mThisSubjectPeriod, "|\t" + mPeriod + "교시" + "\t|");

        //현재 과목열로 이동
        Log.d(TAG, "mPeriod - 1 = " + (mPeriod - 1));
        cursor.moveToPosition(mPeriod - 1);

        //현재 과목정보 가져오기
        String[] mThisSubjectArray = new String[0];

        if (calendar.get(Calendar.DAY_OF_WEEK) > 0 && calendar.get(Calendar.DAY_OF_WEEK) < 6) {
            Log.d(TAG, calendar.get(Calendar.DAY_OF_WEEK) + "");

            mThisSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");
        }

        //현재 과목이름 보여주기
        if (mThisSubjectArray.length > 0) {
            updateViews.setTextViewText(R.id.mThisSubjectName, mThisSubjectArray[0]);
            Log.d(TAG, "mThisSubjectName : " + mThisSubjectArray[0]);
        }

        //현재 과목에 대한 힌트 보여주기
        String mThisSubjectHintString = "";

        for (int i = 1; i < mThisSubjectArray.length; i++) {
            mThisSubjectHintString += mThisSubjectArray[i];

            if (i != mThisSubjectArray.length - 1)
                mThisSubjectHintString += "/";
        }

        updateViews.setTextViewText(R.id.mThisSubjectHint, mThisSubjectHintString);

        //다음 과목열로 이동
        cursor.moveToPosition(mPeriod);

        //다음 과목정보 가져오기
        String[] mNextSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");

        //다음 과목이름 보여주기
        if (mNextSubjectArray.length > 0) {
            updateViews.setTextViewText(R.id.mNextSubjectName, mNextSubjectArray[0]);
        }

        //위젯 텍스트뷰 정보 업데이트 하기
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }
}


