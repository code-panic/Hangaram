package com.hangaram.hellgaram.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.datebase.DataBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class Timetable1Provider extends AppWidgetProvider {
    private static final String TAG = "Timetable1Provider";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_TIMETABLE1";

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
            Intent intent = new Intent(context, Timetable1Provider.class);
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
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable1);

            updateViews.removeAllViews(R.id.subject_text);

            /*데이터베이스 준비하기*/
            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

            Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_TIMETABLE, null);

            for (int row = 1; row < 7; row++) {
                cursor.moveToPosition(row);

                for (int column = 1; column < 6; column++) {
                    RemoteViews subjectViews = new RemoteViews(context.getPackageName(), R.layout.item_timetable1_widget);
                    subjectViews.setTextViewText(R.id.subject_text, cursor.getString(column));

                    /*과목명에 공강이 있을 경우*/
                    if (cursor.getString(column).contains("공강")) {
                        subjectViews.setInt(R.id.subject_text, "setBackgroundColor", Color.rgb(145, 151, 181));
                    }

                    /*과목명에 담임이 있을 경우*/
                    if (cursor.getString(column).contains("담임")) {
                        subjectViews.setInt(R.id.subject_text, "setBackgroundColor", Color.rgb(172, 193, 137));
                    }

                    /*현재 가장 필요한 과목일 경우*/
                    if (WidgetManager.getNeedPeriod(0) == row && WidgetManager.getNeedDay(0) == column) {
                        subjectViews.setTextColor(R.id.subject_text, Color.WHITE);
                        subjectViews.setInt(R.id.subject_text, "setBackgroundColor", Color.BLACK);
                    }

                    updateViews.addView(R.id.widget_timetable1_grid, subjectViews);
                }
            }

            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, updateViews);
        }
    }

}