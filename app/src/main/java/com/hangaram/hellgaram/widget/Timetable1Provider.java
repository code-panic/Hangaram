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

public class Timetable1Provider extends AppWidgetProvider {
    private static final String TAG = "Timetable1Provider";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_TIMETABLE1";

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
            Intent intent = new Intent(context, Timetable1Provider.class);
            intent.setAction(ACTION_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

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

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context, 0, new Intent(ACTION_UPDATE), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /*모든 위젯 업데이트 하기*/
    private void updateAllWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));
        for (int appWidgetId : appWidgetIds)
            updateWidget(context, AppWidgetManager.getInstance(context), appWidgetId, PeriodGiver.getCurrentPeriod());
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int tmpPeriod) {
        Log.d(TAG, "tmpPeriod 값: " + tmpPeriod);

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable1);

        /*
         * 월요일: 1
         * 화요일: 2
         * 수요일: 3
         * 목요일: 4
         * 금요일: 5
         * */
        int currentDayOfWeek = PeriodGiver.getDayOfWeek(tmpPeriod);
        int currentPeriod = PeriodGiver.getPeriod(tmpPeriod);

        Log.d(TAG, "dayOfWeek 값: " + currentDayOfWeek);
        Log.d(TAG, "period 값: " + currentPeriod);

        /*데이터베이스 준비하기*/
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_TIMETABLE, null);

        for (int period = 1; period < 7; period++) {
            cursor.moveToPosition(period);

            for (int dayOfWeek = 1; dayOfWeek < 6; dayOfWeek++) {
                Log.d(TAG, "period: " + period + " dayOfWeek: " + dayOfWeek + " content: " + cursor.getString(dayOfWeek));
                RemoteViews item = new RemoteViews(context.getPackageName(), R.layout.item_timetable1_widget);
                item.setTextViewText(R.id.timetable1_widget_item, cursor.getString(dayOfWeek));
                updateViews.addView(R.id.timetable1_widget_grid_view, item);
            }
        }

        /*변동사항 저장하기*/
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }
}


