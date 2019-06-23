package com.hangaram.hellgaram.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.hangaram.hellgaram.R;

public class Timetable2Configure extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.widget_timetable2_configure);

        int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        if (getIntent().getExtras() != null) {
            appWidgetId = getIntent().getExtras().getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_timetable2);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent resIntent = new Intent();
        setResult(RESULT_OK, resIntent);
        finish();
    }
}
