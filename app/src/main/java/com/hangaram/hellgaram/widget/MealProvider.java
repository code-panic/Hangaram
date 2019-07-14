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
import android.util.Log;
import android.widget.RemoteViews;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.datebase.DataBaseHelper;
import com.hangaram.hellgaram.time.TimeGiver;

public class MealProvider extends AppWidgetProvider {
    private static final String TAG = "MealProvider";

    public static final String ACTION_CLICK = "CLICK_WIDGET_MEAL";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_MEAL";


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        /*매일 12시 업데이트*/
        WidgetManager.setAlaram(context, 0, 0, 0, ACTION_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_UPDATE)) {
            updateAllWidgets(context);
        } else if (intent.getAction().equals(ACTION_CLICK)) {
            SharedPreferences pref = context.getSharedPreferences("widget" + intent.getIntExtra("appWidgetId", 0), 0);
            SharedPreferences.Editor editor = pref.edit();

            Log.d(TAG, "클린된 위젯의 appWidgetId - " + intent.getIntExtra("appWidgetId", 0));

            editor.putBoolean("isLunch", !pref.getBoolean("isLunch", true)).apply();
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
        /*위젯 아이디 가져오기*/
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));

        for (int appWidgetId : appWidgetIds) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_cafeteria);

            /*위젯 색깔바꾸기*/
            SharedPreferences pref = context.getSharedPreferences("widget" + appWidgetId, 0);

            if (pref.getInt("backgroundColor", Color.WHITE) == Color.WHITE)
                changeWidgetColor(pref, updateViews, WidgetManager.WHITE, Color.BLACK);
            else
                changeWidgetColor(pref, updateViews, WidgetManager.BLACK, Color.WHITE);

            /*위젯 텍스트 바꾸기*/
            setWidgetText(context, updateViews, pref);

            /*위젯 클릭 시 점심-> 저녁, 저녁->점심으로 변경*/
            setClickListener(context, updateViews, appWidgetId);

            /*변동사항 저장하기*/
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, updateViews);
        }

    }

    /*위젯 색깔바꾸기*/
    private void changeWidgetColor(SharedPreferences pref, RemoteViews updateViews, int backgroundColor, int textColor) {
        updateViews.setTextColor(R.id.when_cafe_text, textColor);
        updateViews.setTextColor(R.id.what_cafe_text, textColor);

        updateViews.setInt(R.id.background_cafeteria,
                "setBackgroundColor",
                Color.argb(pref.getInt("transparent", 255), backgroundColor, backgroundColor, backgroundColor));
    }

    /*위젯 텍스트 바꾸기*/
    private void setWidgetText(Context context, RemoteViews updateViews, SharedPreferences pref) {
        /*데이터베이스 준비하기*/
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        String[] args = {TimeGiver.getYear(), TimeGiver.getMonth(), TimeGiver.getDate()};
        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_CAFETERIA
                + " where year = ? and month = ? and date = ? ", args);

        cursor.moveToFirst();

        /*위젯 텍스트 바꾸기*/
        if (pref.getBoolean("isLunch", true)) {
            updateViews.setTextViewText(R.id.when_cafe_text, "점심");
            updateViews.setTextViewText(R.id.what_cafe_text, cursor.getString(cursor.getColumnIndex("lunch")));
        } else {
            updateViews.setTextViewText(R.id.when_cafe_text, "저녁");
            updateViews.setTextViewText(R.id.what_cafe_text, cursor.getString(cursor.getColumnIndex("dinner")));
        }
    }

    /*위젯 클릭 시 점심-> 저녁, 저녁->점심으로 변경*/
    private void setClickListener(Context context, RemoteViews updateViews, int appWidgetId) {
        Intent intent = new Intent(context, MealProvider.class);
        intent.setAction(ACTION_CLICK);
        intent.putExtra("appWidgetId", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        updateViews.setOnClickPendingIntent(R.id.background_cafeteria, pendingIntent);

        Log.d(TAG, "클릭될 위젯의 appWidgetId - " + appWidgetId);
    }
}
