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
import com.hangaram.hellgaram.time.TimeGiver;

import java.util.Calendar;

public class MealProvider extends AppWidgetProvider {
    private static final String TAG = "MealProvider";

    public static final String ACTION_CLICK = "CLICK_WIDGET_MEAL";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_MEAL";

    private static boolean sIsLunch = true;

    private AppWidgetManager mAppWidgetManager;
    private int[] mAppWidgetIds;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        /*보낼 인텐트 생성하기*/
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_UPDATE), PendingIntent.FLAG_UPDATE_CURRENT);

        /*알람매니저 설정하기*/
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_UPDATE)) {
            updateAllWidgets(context);
        } else if(intent.getAction().equals(ACTION_CLICK)) {
            Log.d(TAG,"Uuuu");
            sIsLunch = !sIsLunch;
            //updateAllWidgets(context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        this.mAppWidgetManager = appWidgetManager;
        this.mAppWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));

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
        for (int mAppWidgetId : mAppWidgetIds)
            updateWidget(context, mAppWidgetId);
    }

    private void updateWidget(Context context, int appWidgetId) {
        Log.d(TAG,"Working");
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_cafeteria);

        /*데이터베이스 준비하기*/
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        String[] args = {TimeGiver.getYear(), TimeGiver.getMonth(), TimeGiver.getDate()};
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_CAFETERIA
                + " where year = ? and month = ? and date = ? ", args);

        cursor.moveToFirst();

        if(sIsLunch) {
            updateViews.setTextViewText(R.id.lunch_or_dinner_text, "점심");
            updateViews.setTextViewText(R.id.meal_widget_cafe_text, cursor.getString(cursor.getColumnIndex("lunch")));
        } else {
            updateViews.setTextViewText(R.id.lunch_or_dinner_text, "저녁");
            updateViews.setTextViewText(R.id.meal_widget_cafe_text, cursor.getString(cursor.getColumnIndex("dinner")));
        }

        Intent intent = new Intent(context, MealProvider.class);
        intent.setAction(ACTION_CLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.meal_widget_layout, pendingIntent);

        /*변동사항 저장하기*/
        mAppWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }
}
