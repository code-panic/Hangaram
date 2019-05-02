package com.hangaram.hellgaram.widget;

import android.app.AlarmManager;
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
import com.hangaram.hellgaram.support.DataBaseHelper;
import com.hangaram.hellgaram.support.TimeGiver;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimetableEachProvider extends AppWidgetProvider {
    private static final String TAG = "TimetableEachProvider";

    private AppWidgetManager mAppWidgetManager;
    private int[] mAppWidgetIds;

    private RemoteViews updateViews;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int period = intent.getExtras().getInt("period");

        //위젯 모두 업데이트
        mAppWidgetIds = mAppWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < mAppWidgetIds.length; i++) {
            updateAppWidget(context, mAppWidgetManager, mAppWidgetIds[i],period);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //위젯들 정보 업데이트하기
        this.mAppWidgetManager = appWidgetManager;
        this.mAppWidgetIds = appWidgetIds;
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int period) {
        //xml 파일 설정하기
        updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable_each);

        //시간에 맞추어 업데이트 설정 바꾸기
        setUpdateViewInfo(context, period);

        //위젯 텍스트뷰 정보 업데이트 하기
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    //위젯 텍스트뷰의 업데이트 정보 설정하기
    void setUpdateViewInfo(Context context, int period) {
        //현재 시간 설정하기
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        //데이터베이스 준비하기
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME_timetable, null, null, null, null, null, null, null);

        //현재 과목교시 보여주기
        updateViews.setTextViewText(R.id.mThisSubjectPeriod, "|\t" + period + "교시" + "\t|");

        //현재 과목열로 이동
        cursor.moveToPosition(period - 1);

        //현재 과목정보 가져오기
        String[] mThisSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");

        //현재 과목이름 보여주기
        if (mThisSubjectArray.length > 0) {
            updateViews.setTextViewText(R.id.mThisSubjectName, mThisSubjectArray[0]);
            Log.d(TAG, "mThisSubjectName : " + mThisSubjectArray[0]);
        }

        //현재 과목에 대한 힌트 보여주기
        String mThisSubjectHintString = "";

        for (int i = 1; i < mThisSubjectArray.length; i++) {
            mThisSubjectHintString += mThisSubjectArray[i];

            if(i != mThisSubjectArray.length - 1)
                mThisSubjectHintString += "/";
        }

        updateViews.setTextViewText(R.id.mThisSubjectHint, mThisSubjectHintString);

        //다음 과목열로 이동
        cursor.moveToPosition(period);

        //다음 과목정보 가져오기
        String[] mNextSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");

        //다음 과목이름 보여주기
        if (mNextSubjectArray.length > 0) {
            updateViews.setTextViewText(R.id.mNextSubjectName, mNextSubjectArray[0]);
        }
    }
}


