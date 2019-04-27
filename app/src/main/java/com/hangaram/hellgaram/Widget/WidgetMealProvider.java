package com.hangaram.hellgaram.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.support.DataBaseHelper;
import com.hangaram.hellgaram.support.TimeGiver;

public class WidgetMealProvider extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        DataBaseHelper dataBaseHelper;
        SQLiteDatabase sqLiteDatabase;

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_meal);

        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        String[] args = {TimeGiver.getYear(0), TimeGiver.getMonth(0), TimeGiver.getDate(0)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT lunch,dinner From " + DataBaseHelper.TABLE_NAME_meal + " WHERE year = ? AND month = ? AND date = ?", args);

        cursor.moveToFirst();

        updateViews.setTextViewText(R.id.mealWidgetLunchTextView, cursor.getString(0));
        updateViews.setTextViewText(R.id.mealWidgetDinnerTextView, cursor.getString(1));
        appWidgetManager.updateAppWidget(appWidgetId,updateViews);
    }



}

