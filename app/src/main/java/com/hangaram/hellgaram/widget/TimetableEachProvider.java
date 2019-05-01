package com.hangaram.hellgaram.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.support.DataBaseHelper;
import com.hangaram.hellgaram.support.TimeGiver;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimetableEachProvider extends AppWidgetProvider {
    private static final String TAG = "TimetableEachProvider";

    private RemoteViews updateViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable_each);
        int mCurrentTime = Integer.parseInt(TimeGiver.getHour() + TimeGiver.getMinuate());

        if (mCurrentTime < 915) {
            setUpdateViewInfo(context,1);
        } else if (mCurrentTime < 1040) {
            setUpdateViewInfo(context,2);
        } else if (mCurrentTime < 1205) {
            setUpdateViewInfo(context,3);
        } else if (mCurrentTime < 1425) {
            setUpdateViewInfo(context,4);
        } else if (mCurrentTime < 1550) {
            setUpdateViewInfo(context,5);
        } else if (mCurrentTime < 1710) {
            setUpdateViewInfo(context,6);
        }

        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    void setUpdateViewInfo(Context context, int period) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME_timetable, null, null, null, null, null, null, null);

        cursor.moveToPosition(period - 1);
        updateViews.setTextViewText(R.id.mThisSubjectPeriod, "[\t" + period + "교시" + "\t]");
        String[] array = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");

        if(array.length > 0) {
            updateViews.setTextViewText(R.id.mThisSubjectName, array[0]);
            Log.d(TAG, "mThisSubjectName : " + array[0]);
        }

        if(array.length > 2) {
            updateViews.setTextViewText(R.id.mThisSubjectHint, array[1] + "/" + array[2]);
            Log.d(TAG, "mThisSubjectHint : " + array[1] + "/" + array[2]);
        }

        cursor.moveToPosition(period);

        array = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");
        updateViews.setTextViewText(R.id.mNextSubjectName, array[0]);
    }
}


