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
    private static final String TABLE_NAME = "timetable";

    private final String action = "UPDATE_WIDGET_TIMETABLE_EACH";

    private AppWidgetManager mManager;
    private int[] mwidgetIds;

    private int period = 0;

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

            Intent intent = new Intent(action);
            intent.putExtra("period", period);

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

        //period = intent.getExtras().getInt("period");

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
            onUpdate(context, mManager, mwidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] widgetIds) {
        super.onUpdate(context, manager, widgetIds);

        mManager = manager;
        mwidgetIds = widgetIds;

        widgetIds = manager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int id : widgetIds)
            updateAppWidget(context, manager, id, period);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context, 0, new Intent(action), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void updateAppWidget(Context context, AppWidgetManager manager, int widgetId, int period) {
        //xml 파일 설정하기
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_timetable_each);

        //현재 시간 설정하기
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        //데이터베이스 준비하기
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //현재 과목교시 보여주기
        views.setTextViewText(R.id.this_subject_period, "|\t" + period + 1 + "교시" + "\t|");

        //현재 과목열로 이동
        cursor.moveToPosition(period);

        //현재 과목정보 가져오기
        String[] thisSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");

        //현재 과목이름 보여주기
        if (thisSubjectArray.length > 0) {
            views.setTextViewText(R.id.this_subject_name, thisSubjectArray[1]);
            Log.d(TAG, "mThisSubjectName : " + thisSubjectArray[0]);
        }

        //현재 과목에 대한 힌트 보여주기
        String thisSubjectHint = "";

        for (int i = 1; i < thisSubjectArray.length; i++) {
            thisSubjectHint += thisSubjectArray[i];

            if (i != thisSubjectArray.length - 1)
                thisSubjectHint += "/";
        }

        views.setTextViewText(R.id.this_subject_hint, thisSubjectHint);

        //다음 과목열로 이동
        cursor.moveToPosition(period + 1);

        //다음 과목정보 가져오기
        String[] nextSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");

        //다음 과목이름 보여주기
        if (nextSubjectArray.length > 0) {
            views.setTextViewText(R.id.next_subject_name, nextSubjectArray[0]);
        }

        //위젯 텍스트뷰 정보 업데이트 하기
        manager.updateAppWidget(widgetId, views);
    }
}


