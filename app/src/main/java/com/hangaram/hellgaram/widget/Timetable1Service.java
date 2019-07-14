package com.hangaram.hellgaram.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hangaram.hellgaram.R;
import com.hangaram.hellgaram.datebase.DataBaseHelper;

import java.util.ArrayList;

public class Timetable1Service extends RemoteViewsService {
    private static final String TAG = "Timetable1Service";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridFactory();
    }

    private class GridFactory implements RemoteViewsFactory {

        private ArrayList<String> subjectList = new ArrayList<>();

        @Override
        public void onCreate() {
            subjectList = getSubjectList(getBaseContext());
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public RemoteViews getViewAt(int pos) {
            RemoteViews updateViews = new RemoteViews(getBaseContext().getPackageName(), R.layout.item_timetable1_widget);
            updateViews.setTextViewText(R.id.item_text, subjectList.get(pos));

            Log.d(TAG, "pos" + pos + ": " + subjectList.get(pos));

            return updateViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int pos) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private ArrayList<String> getSubjectList(Context context) {
        ArrayList<String> subjectList = new ArrayList<>();

        /*데이터베이스 준비하기*/
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME_TIMETABLE, null);

        for (int row = 1; row < 7; row++) {
            cursor.moveToPosition(row);

            for (int column = 1; column < 6; column++)
                subjectList.add(cursor.getString(column));
        }

        return subjectList;
    }
}
