package com.example.hellgarammobileapp.Fragment.TimeTableFragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.hellgarammobileapp.Fragment.TimeTableFragment.TimeDBHelper;
import com.example.hellgarammobileapp.Fragment.TimeTableFragment.TimeTableItem;
import com.example.hellgarammobileapp.R;

public class TimeTableFragment extends Fragment{
    private static final String TimeTableActivity = "TimeTableActivity";
    private static final String TABLE_NAME = "timeTable";

    View view;

    TimeTableItem[][] items = new TimeTableItem[7][6];

    TimeDBHelper timeDbHelper;
    SQLiteDatabase db;

    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_timetable, container, false);
        init(view.getContext());
        return view;
    }

    private void init(Context context) {
        TableLayout tableLayout = view.findViewById(R.id.timeTableLayout);

        cursor = openDataBase(context);
        Log.d(TimeTableActivity, "func opendatabase");

        cursor.moveToNext();

        for (int i = 0; i < 7; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableLayout.addView(tableRow);

            for (int j = 0; j < 6; j++) {
                cursor.moveToPosition(i);

                items[i][j] = new TimeTableItem(context);
                items[i][j].editText.setText(cursor.getString(j+1));
                tableRow.addView(items[i][j]);
                Log.d(TimeTableActivity,"addview in tablerow");

                if(j != 5) {
                    View verticalLine = new View(context);
                    verticalLine.setLayoutParams(new TableRow.LayoutParams(3, TableRow.LayoutParams.MATCH_PARENT));
                    verticalLine.setBackgroundResource(R.drawable.timetable_line);
                    verticalLine.setBackgroundColor(Color.BLACK);
                    tableRow.addView(verticalLine);
                }
            }

            if(i != 6) {
                View horiaontalLine = new View(context);
                horiaontalLine.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                horiaontalLine.setBackgroundResource(R.drawable.timetable_line);
                horiaontalLine.setBackgroundColor(Color.BLACK);
                tableLayout.addView(horiaontalLine);
            }
        }
        cursor.close();
    }

    private Cursor openDataBase(Context context) {
        timeDbHelper = new TimeDBHelper(context);
        db = timeDbHelper.getReadableDatabase();
        Log.d(TimeTableActivity, "set db and dbhelper");

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return cursor;
    }

    public void saveTimeTableData() {
        db = timeDbHelper.getWritableDatabase();

        for (int i = 0; i < 7; i++) {
            Log.d(TimeTableActivity,"num:" + i);

            CharSequence cs = new StringBuffer(items[i][0].editText.getText());
            String UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET period = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(TimeTableActivity, cs.toString());

            cs = new StringBuffer(items[i][1].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET mon = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(TimeTableActivity, cs.toString());

            cs = new StringBuffer(items[i][2].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET tue = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(TimeTableActivity, cs.toString());

            cs = new StringBuffer(items[i][3].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET wed = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(TimeTableActivity, cs.toString());

            cs = new StringBuffer(items[i][4].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET thu = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(TimeTableActivity, cs.toString());

            cs = new StringBuffer(items[i][5].editText.getText());
            UPDATE_SQL = "UPDATE " + TABLE_NAME
                    + " SET fri = '" + cs.toString() + "'"
                    + " WHERE id = " + (i + 1);
            db.execSQL(UPDATE_SQL);
            Log.d(TimeTableActivity, cs.toString());
        }
        Log.d(TimeTableActivity, "func. saveTimeTableData");
    }
}
