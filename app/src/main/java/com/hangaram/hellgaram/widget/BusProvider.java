package com.hangaram.hellgaram.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.bus.BusTask;

import java.util.ArrayList;
import java.util.HashMap;

public class BusProvider extends AppWidgetProvider {
    private static final String TAG = "BusProvider";
    public static final String ACTION_CLICK = "CLICK_WIDGET_BUS";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_BUS";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_UPDATE))
            updateAllWidgets(context);
        else if (intent.getAction().equals(ACTION_CLICK)) {
            SharedPreferences pref = context.getSharedPreferences("widget" + intent.getIntExtra("appWidgetId", 0), 0);

            Log.d(TAG, intent.getIntExtra("appWidgetId", 0) + " widget is clicked");

            BusTask busTask = new BusTask(new BusTask.BusCall() {
                @Override
                public void onSuccess(ArrayList<HashMap<String, String>> busList) {
                    Log.d(TAG, intent.getIntExtra("appWidgetId", 0) + " widget's BusTask is successed");
                    updateAllWidgets(context);
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, intent.getIntExtra("appWidgetId", 0) + " widget's BusTask is failed");
                    Toast.makeText(context, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }, pref.getString("stNm", "월촌중학교"));

            busTask.execute();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        updateAllWidgets(context);
    }

    /*모든 위젯 업데이트 하기*/
    private void updateAllWidgets(Context context) {
        /*위젯 아이디 가져오기*/
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));

        for (int appWidgetId : appWidgetIds) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_bus);

            /*pref 변수 초기화*/
            SharedPreferences pref = context.getSharedPreferences("widget" + appWidgetId, 0);

            /*위젯 색깔바꾸기*/
            if (pref.getInt("backgroundColor", Color.WHITE) == Color.WHITE)
                changeWidgetColor(pref, updateViews, WidgetManager.WHITE, Color.BLACK);
            else
                changeWidgetColor(pref, updateViews, WidgetManager.BLACK, Color.WHITE);

            /*위젯 텍스트 설정*/
            updateViews.setTextViewText(R.id.stNm_text, pref.getString("stNm", "월촌중학교"));
            updateViews.setTextViewText(R.id.rtNm_text, pref.getString("rtNm", "양천01"));

            if (pref.getString("stNm", "월촌중학교").equals("월촌중학교") && BusTask.busList1 != null)
                setWidgetText(pref, updateViews, BusTask.busList1);
            else if (pref.getString("stNm", "월촌중학교").equals("목동이대병원") && BusTask.busList2 != null)
                setWidgetText(pref, updateViews, BusTask.busList2);
            else {
                updateViews.setTextViewText(R.id.arrmsg1_text, "위젯을 눌러 버스정보를 다운받아 주세요");
                updateViews.setTextViewText(R.id.last_update_text, "");
            }

            Log.d(TAG, appWidgetId + "widget is updating");

            /*클릭 리스너 설정하기*/
            setClickListener(context,updateViews, appWidgetId);

            /*변동사항 저장하기*/
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, updateViews);
        }
    }

    private void changeWidgetColor(SharedPreferences pref, RemoteViews updateViews, int backgroundColor, int textColor) {
        updateViews.setTextColor(R.id.stNm_text, textColor);
        updateViews.setTextColor(R.id.rtNm_text, textColor);
        updateViews.setTextColor(R.id.arrmsg1_text, textColor);
        updateViews.setTextColor(R.id.last_update_text, textColor);

        updateViews.setInt(R.id.background_bus,
                "setBackgroundColor",
                Color.argb(pref.getInt("transparent", 255), backgroundColor, backgroundColor, backgroundColor));
    }


    private void setWidgetText(SharedPreferences pref, RemoteViews updateViews, ArrayList<HashMap<String, String>> busList) {
        for (int pos = 0; pos < busList.size(); pos++) {
            if (busList.get(pos).get("rtNm").equals(pref.getString("rtNm", "양천01"))) {
                updateViews.setTextViewText(R.id.arrmsg1_text,
                        busList.get(pos).get("arr1") + "/"
                                + busList.get(pos).get("sta1") + "/"
                                + busList.get(pos).get("isFullFlag1"));

                /*업데이트 7/4 오루 3:14*/
                updateViews.setTextViewText(R.id.last_update_text, "업데이트\t\t"
                        + busList.get(pos).get("month") + "/" + busList.get(0).get("day") + "\t"
                        + busList.get(pos).get("am_pm") + "\t"
                        + busList.get(pos).get("hour") + ":" + busList.get(0).get("min"));
            }
        }
    }

    /*위젯 클릭 시 점심-> 저녁, 저녁->점심으로 변경*/
    private void setClickListener(Context context, RemoteViews updateViews, int appWidgetId) {
        Intent intent = new Intent(context, BusProvider.class);
        intent.setAction(ACTION_CLICK);
        intent.putExtra("appWidgetId", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.background_bus, pendingIntent);
    }
}