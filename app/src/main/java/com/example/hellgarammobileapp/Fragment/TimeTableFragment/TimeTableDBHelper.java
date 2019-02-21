package com.example.hellgarammobileapp.Fragment.TimeTableFragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimeTableDBHelper extends SQLiteOpenHelper {
    private static final String TimeDBHelper = "TimeDBHelper";

    private static final String DATABASE_NAME = "HellgaramDatabase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "timetable";

    public TimeTableDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SQL = "create table " + TABLE_NAME + "("
                + "id integer PRIMARY KEY AUTOINCREMENT,"
                + "mon text,"
                + "tue text,"
                + "wed text,"
                + "thu text,"
                + "fri text)";

        db.execSQL(CREATE_SQL);
        Log.d(TimeDBHelper, "create timeTable");

        for (int i = 0; i < 7; i++) {
            String SQL = "insert into " + TABLE_NAME + "(mon, tue, wed, thu, fri) values ('' , '', '', '', '');";
            db.execSQL(SQL);
        }
        Log.d(TimeDBHelper, "insert records");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
