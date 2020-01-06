package com.hangaram.hellgaram.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.station.StationTask;
import com.hangaram.hellgaram.station.simplexml.BusInfo;

import java.util.List;

public class StationProvider extends AppWidgetProvider {
    private static final String TAG = "StationProvider";

    private static final String ACTION_CLICK = "CLICK_WIDGET_BUS";
    private static final String ACTION_UPDATE = "UPDATE_WIDGET_BUS";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_UPDATE))
            updateAllWidgets(context);
        else if (intent.getAction().equals(ACTION_CLICK)) {
            SharedPreferences storage = context.getSharedPreferences("widget" + intent.getIntExtra("appWidgetId", 0), 0);

            Log.d(TAG, intent.getIntExtra("appWidgetId", 0) + "위젯 클릭됨");

            StationTask stationTask = new StationTask(new StationTask.BusCallBack() {
                @Override
                public void onSuccess (List<BusInfo> busList) {
                    updateAllWidgets(context);
                }

                @Override
                public void onFailure () { Toast.makeText(context, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show(); }
            });

            stationTask.execute("15148");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        updateAllWidgets(context);
    }

    /*모든 위젯 업데이트 하기*/
    private void updateAllWidgets(Context context) {
        /* 위젯 아이디 가져오기 */
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));

        for (int appWidgetId : appWidgetIds) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_station);

            /* storage 변수 선언 및 초기화 */
            SharedPreferences storage = context.getSharedPreferences("widget" + appWidgetId, 0);

            /* 위젯 뷰 컬러 설정 */
            if (storage.getString("backgroundColorValue", "white").equals("white")) {
                updateViews.setTextColor(R.id.stationNameTextView, Color.BLACK);
                updateViews.setTextColor(R.id.busNameTextView, Color.BLACK);
                updateViews.setTextColor(R.id.arriveInfoTextView, Color.BLACK);
                updateViews.setTextColor(R.id.updateTimeTextView, Color.BLACK);

                updateViews.setInt(R.id.backgroundLinearLayout, "setBackgroundColor", Color.argb(storage.getInt("backgroundTransparentValue", 255), 255, 255,255));
            } else if (storage.getString("backgroundColorValue", "white").equals("black")){
                updateViews.setTextColor(R.id.stationNameTextView, Color.WHITE);
                updateViews.setTextColor(R.id.busNameTextView, Color.WHITE);
                updateViews.setTextColor(R.id.arriveInfoTextView, Color.WHITE);
                updateViews.setTextColor(R.id.updateTimeTextView, Color.WHITE);

                updateViews.setInt(R.id.backgroundLinearLayout, "setBackgroundColor", Color.argb(storage.getInt("backgroundTransparentValue", 255), 255, 255,255));
            }

            /* 위젯 텍스트 설정 */
            updateViews.setTextViewText(R.id.stationNameTextView, storage.getString("stationName", "월촌중학교"));
            updateViews.setTextViewText(R.id.busNameTextView, storage.getString("busName", "양천01"));

            if (storage.getString("stationName", "월촌중학교").equals("월촌중학교") && StationTask.busInfoList15148 != null)
                setWidgetText(storage, updateViews, StationTask.busInfoList15148);
            else if (storage.getString("stationName", "월촌중학교").equals("목동이대병원") && StationTask.busInfoList15154 != null)
                setWidgetText(storage, updateViews, StationTask.busInfoList15154);
            else {
                updateViews.setTextViewText(R.id.arriveInfoTextView, "위젯을 눌러 버스정보를 다운받아 주세요");
                updateViews.setTextViewText(R.id.updateTimeTextView, "");
            }

            /* 클릭 리스너 설정 */
            Intent intent = new Intent(context, StationProvider.class);
            intent.setAction(ACTION_CLICK);

            intent.putExtra("appWidgetId", appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.backgroundLinearLayout, pendingIntent);

            /* 위젯 변경 */
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, updateViews);
        }
    }


    private void setWidgetText(SharedPreferences pref, RemoteViews updateViews, List<BusInfo> busList) {
        for (int i = 0; i < busList.size(); i++) {
            if (busList.get(i).getBusName().equals(pref.getString("busName", "양천01"))) {
                updateViews.setTextViewText(R.id.arriveInfoTextView,
                        "#" + busList.get(i).getFirstBusArriveInfo() +
                                "#" + busList.get(i).getFirstBusArriveInfo());

//                /*업데이트 7/4 오루 3:14*/
//                updateViews.setTextViewText(R.id.updateTimeTextView, "업데이트\t\t"
//                        + busList.get(i).get("month") + "/" + busList.get(0).get("day") + "\t"
//                        + busList.get(i).get("am_pm") + "\t"
//                        + busList.get(i).get("hour") + ":" + busList.get(0).get("min"));
            }
        }
    }
}