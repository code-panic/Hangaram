package com.hangaram.hellgaram.widget;

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
import com.hangaram.hellgaram.Fragment.DataBaseHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimetableEachProvider extends AppWidgetProvider {
    private static final String TAG = "TimetableEachProvider";

    private AppWidgetManager mAppWidgetManager;
    private int[] mAppWidgetIds;
    private int mPeriod = 1;    //과목 교시

    private RemoteViews updateViews;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

//        Log.d(TAG, "onReceive -> mPeriod - 1 = " + (mPeriod - 1));
//
//        //과목 교시 정보 가져오기
//        mPeriod = intent.getExtras().getInt("period");
//
//        Log.d(TAG, "onReceive -> mPeriod - 1 = " + (mPeriod - 1));
//
//
//        //위젯 모두 업데이트
//        /* 위젯을 처음 만들 때 onReceive 함수가 호출되고 그 때 intent의 period는 0으로 초기화 된다.
//           이 후 setUpdateView 함수에서 mPeriod - 1로 연산을 진행해 cursor의 인덱스가 -1로 오버플로우 된다.
//           이런 예상치 못한 상황에서 mPeriod의 값이 바뀔 수 있어 if문 처리를 해 놓았다. */
//        if (mPeriod > 0 && mPeriod < 7) {
//            mAppWidgetIds = mAppWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
//            for (int i = 0; i < mAppWidgetIds.length; i++)
//                updateAppWidget(context, mAppWidgetManager, mAppWidgetIds[i]);
//        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(TAG, "onUpdate -> mPeriod - 1 = " + (mPeriod - 1));

        //위젯들 정보 업데이트하기
        this.mAppWidgetManager = appWidgetManager;
        this.mAppWidgetIds = appWidgetIds;

        //위젯 모두 업데이트
        if (mPeriod > 0 && mPeriod < 7) {
            mAppWidgetIds = mAppWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            for (int i = 0; i < mAppWidgetIds.length; i++)
                updateAppWidget(context, mAppWidgetManager, mAppWidgetIds[i]);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //xml 파일 설정하기
        updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_timetable_each);

        //시간에 맞추어 업데이트 설정 바꾸기
        setUpdateViewInfo(context);

        //위젯 텍스트뷰 정보 업데이트 하기
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    //위젯 텍스트뷰의 업데이트 정보 설정하기
    void setUpdateViewInfo(Context context) {
        //현재 시간 설정하기
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        //데이터베이스 준비하기
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME_timetable, null, null, null, null, null, null, null);

        //현재 과목교시 보여주기
        updateViews.setTextViewText(R.id.mThisSubjectPeriod, "|\t" + mPeriod + "교시" + "\t|");

        //현재 과목열로 이동
        Log.d(TAG, "mPeriod - 1 = " + (mPeriod - 1));
        cursor.moveToPosition(mPeriod - 1);


        //현재 과목정보 가져오기
        String[] mThisSubjectArray = new String[0];

        if (calendar.get(Calendar.DAY_OF_WEEK) > 0 && calendar.get(Calendar.DAY_OF_WEEK) < 6) {
            Log.d(TAG, calendar.get(Calendar.DAY_OF_WEEK) + "");

            mThisSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");
        }

        //현재 과목이름 보여주기
        if (mThisSubjectArray.length > 0) {
            updateViews.setTextViewText(R.id.mThisSubjectName, mThisSubjectArray[0]);
            Log.d(TAG, "mThisSubjectName : " + mThisSubjectArray[0]);
        }

        //현재 과목에 대한 힌트 보여주기
        String mThisSubjectHintString = "";

        for (int i = 1; i < mThisSubjectArray.length; i++) {
            mThisSubjectHintString += mThisSubjectArray[i];

            if (i != mThisSubjectArray.length - 1)
                mThisSubjectHintString += "/";
        }

        updateViews.setTextViewText(R.id.mThisSubjectHint, mThisSubjectHintString);

        //다음 과목열로 이동
        cursor.moveToPosition(mPeriod);

        //다음 과목정보 가져오기
        String[] mNextSubjectArray = cursor.getString(calendar.get(Calendar.DAY_OF_WEEK) - 1).split("\n");

        //다음 과목이름 보여주기
        if (mNextSubjectArray.length > 0) {
            updateViews.setTextViewText(R.id.mNextSubjectName, mNextSubjectArray[0]);
        }
    }
}


