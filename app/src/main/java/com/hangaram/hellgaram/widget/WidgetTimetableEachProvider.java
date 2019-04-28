package com.hangaram.hellgaram.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.support.DataBaseHelper;
import com.hangaram.hellgaram.support.TimeGiver;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WidgetTimetableEachProvider extends AppWidgetProvider {

    private RemoteViews updateViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable_each);
        int mCurrentTime = Integer.parseInt(TimeGiver.getHour() + TimeGiver.getMinuate());

        if( mCurrentTime < 800){

        }else if(mCurrentTime < 915){

        }else if(mCurrentTime < 1040){

        }else if(mCurrentTime < 1205){

        }else if(mCurrentTime < 1310){

        }else if(mCurrentTime < 1425){

        }else if(mCurrentTime < 1550){

        }else if(mCurrentTime < 1710){

        }else{

        }
        appWidgetManager.updateAppWidget(appWidgetId,updateViews);
    }

    void setUpdateViewInfo(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        DataBaseHelper dataBaseHelper = new DataBaseHelper();
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        dataBaseHelper = new DataBaseHelper(context);

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME_timetable, null, null, null, null, null, null, null);

        cursor.moveToPosition(0);
        updateViews.setTextViewText(R.id.mThisSubjectPeriod, "[\t" + "아침종례시간" + "\t]");
        String[] array = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK)-1).split("\t");

        if(array.length > 2) {
            updateViews.setTextViewText(R.id.mThisSubjectName, array[0]);
            updateViews.setTextViewText(R.id.mThisSubjectHint, array[1] + "/" + array[2]);
        }
        cursor.moveToPosition(1);

        if (array.length > 0) {
            array = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\t");
            updateViews.setTextViewText(R.id.mNextSubjectName, array[0]);
        }
    }
}


