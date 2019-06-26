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
import com.hangaram.hellgaram.datebase.DataBaseHelper;

import java.util.Calendar;

public class Timetable2Provider extends AppWidgetProvider {
    private static final String TAG = "Timetable2Provider";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_TIMETABLE2";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        /*특정 시간마다 인텐트 보내기*/
        for (int period = 0; period < PeriodGiver.sUpdateTimeArray.length; period++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, PeriodGiver.sUpdateTimeArray[period][0]);
            calendar.set(Calendar.MINUTE, PeriodGiver.sUpdateTimeArray[period][1]);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            /*보낼 인텐트 생성하기*/
            Intent intent = new Intent(context, Timetable2Provider.class);
            intent.setAction(ACTION_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, period, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            /*알람매니저 설정하기*/
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        updateAllWidgets(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_UPDATE)) {
            Log.d(TAG, "업데이트를 시작합니다.");
            updateAllWidgets(context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        updateAllWidgets(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        /*알람 예약 취소하기*/
        for (int period = 0; period < PeriodGiver.sUpdateTimeArray.length; period++) {
            Intent intent = new Intent(context, Timetable2Provider.class);
            intent.setAction(ACTION_UPDATE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(PendingIntent.getBroadcast(context, period, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    /*모든 위젯 업데이트 하기*/
    private void updateAllWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));
        for (int appWidgetId : appWidgetIds)
            updateWidget(context, AppWidgetManager.getInstance(context), appWidgetId, PeriodGiver.getCurrentPeriod());
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int tmpPeriod) {
        Log.d(TAG, "tmpPeriod 값: " + tmpPeriod);

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable2);
        String[] subjectArray;

        /*
         * 월요일: 1
         * 화요일: 2
         * 수요일: 3
         * 목요일: 4
         * 금요일: 5
         * */
        int dayOfWeek = PeriodGiver.getDayOfWeek(tmpPeriod);
        int period = PeriodGiver.getPeriod(tmpPeriod);

        Log.d(TAG, "dayOfWeek 값: " + dayOfWeek);
        Log.d(TAG, "period 값: " + period);

        /*데이터베이스 준비하기*/
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_TIMETABLE, null);

        /*현재 과목교시 보여주기*/
        updateViews.setTextViewText(R.id.this_subject_period, period + "교시");

        /*현재 과목과 힌트 보여주기*/
        cursor.moveToPosition(period);
        subjectArray = cursor.getString(dayOfWeek).split("\n");

        updateViews.setTextViewText(R.id.this_subject_name, getPeriodName(subjectArray));
        updateViews.setTextViewText(R.id.this_subject_hint, getPeriodHint(subjectArray));


        dayOfWeek = PeriodGiver.getDayOfWeek(tmpPeriod + 1);
        period = PeriodGiver.getPeriod(tmpPeriod + 1);

        /*다음 과목 보여주기*/
        cursor.moveToPosition(period);
        subjectArray = cursor.getString(dayOfWeek).split("\n");

        updateViews.setTextViewText(R.id.next_subject_name,  "다음시간\t\t\t"+ getPeriodName(subjectArray));

        /*변동사항 저장하기*/
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    private String getPeriodName(String[] subjectArray) {
        if (subjectArray.length > 0)
            return subjectArray[0];
        else
            return "";
    }

    private String getPeriodHint(String[] subjectArray) {
        StringBuilder hintString = new StringBuilder();

        for (int i = 1; i < subjectArray.length; i++) {
            hintString.append(subjectArray[i]);

            if (i != subjectArray.length - 1)
                hintString.append("/");
        }

        return hintString.toString();
    }
}


