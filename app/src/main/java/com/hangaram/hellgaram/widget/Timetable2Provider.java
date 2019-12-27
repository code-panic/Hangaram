package com.hangaram.hellgaram.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.datebase.DataBaseHelper;

public class Timetable2Provider extends AppWidgetProvider {
    private static final String TAG = "Timetable2Provider";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_TIMETABLE2";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        /*특정 시간마다 인텐트 보내기*/
        for (int period = 0; period < WidgetManager.TIMETABLE_UPDATE_TIMES.length; period++)
            WidgetManager.setAlaram(context,
                    WidgetManager.TIMETABLE_UPDATE_TIMES[period][0],
                    WidgetManager.TIMETABLE_UPDATE_TIMES[period][1],
                    period,
                    ACTION_UPDATE);

        updateAllWidgets(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_UPDATE))
            updateAllWidgets(context);
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
        for (int period = 0; period < WidgetManager.TIMETABLE_UPDATE_TIMES.length; period++) {
            Intent intent = new Intent(context, Timetable2Provider.class);
            intent.setAction(ACTION_UPDATE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(PendingIntent.getBroadcast(context, period, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    /*모든 위젯 업데이트 하기*/
    private void updateAllWidgets(Context context) {
        /*위젯 아이디 가져오기*/
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));

        for (int appWidgetId : appWidgetIds) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable_2);

            /*위젯 색깔바꾸기*/
            SharedPreferences pref = context.getSharedPreferences("widget" + appWidgetId, 0);

            if (pref.getInt("backgroundColor", Color.WHITE) == Color.WHITE)
                changeWidgetColor(pref, updateViews, WidgetManager.WHITE, Color.BLACK);
            else
                changeWidgetColor(pref, updateViews, WidgetManager.BLACK, Color.WHITE);

            /*위젯 텍스트 설정*/
            setWidgetText(context, updateViews);

            /*변동사항 저장하기*/
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, updateViews);
        }
    }

    private void changeWidgetColor(SharedPreferences pref, RemoteViews updateViews, int backgroundColor, int textColor) {
        updateViews.setTextColor(R.id.this_subject_period, textColor);
        updateViews.setTextColor(R.id.this_subject_name, textColor);
        updateViews.setTextColor(R.id.this_subject_hint, textColor);
        updateViews.setTextColor(R.id.next_subject_name, textColor);

        updateViews.setInt(R.id.background_timetable2,
                "setBackgroundColor",
                Color.argb(pref.getInt("transparent", 255), backgroundColor, backgroundColor, backgroundColor));
    }

    private void setWidgetText(Context context, RemoteViews updateViews) {
        String[] subjectArray;

        /*데이터베이스 준비하기*/
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_TIMETABLE, null);

        /*현재 과목교시 보여주기*/
        updateViews.setTextViewText(R.id.this_subject_period, WidgetManager.getNeedPeriod(0) + "교시");

        /*현재 과목과 힌트 보여주기*/
        cursor.moveToPosition(WidgetManager.getNeedPeriod(0));
        subjectArray = cursor.getString(WidgetManager.getNeedDay(0)).split("\n");

        updateViews.setTextViewText(R.id.this_subject_name, WidgetManager.getPeriodName(subjectArray));
        updateViews.setTextViewText(R.id.this_subject_hint, WidgetManager.getPeriodHint(subjectArray));

        /*다음 과목 보여주기*/
        cursor.moveToPosition(WidgetManager.getNeedPeriod(1));
        subjectArray = cursor.getString(WidgetManager.getNeedDay(1)).split("\n");

        updateViews.setTextViewText(R.id.next_subject_name, "다음시간\t\t\t" + WidgetManager.getPeriodName(subjectArray));
    }
}