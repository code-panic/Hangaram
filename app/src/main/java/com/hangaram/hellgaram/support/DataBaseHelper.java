package com.hangaram.hellgaram.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String Tag = "DataBaseHelper";

    private static final String DATABASE_NAME = "HellgaramDatabase.db";
    private static final int DATABASE_VERSION = 18;

    public static final String TABLE_NAME_meal = "meal";
    public static final String TABLE_NAME_timetable = "timetable";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String TABLE_CREATE_meal = "create table " + TABLE_NAME_meal + "("
                + "year integer,"
                + "month integer,"
                + "date integer,"
                + "lunch text,"
                + "dinner text)";

        //시간표 테이블 생성
        String TABLE_CREATE_timetable = "create table " + TABLE_NAME_timetable + "("
                + "period text,"
                + "mon text,"
                + "tue text,"
                + "wed text,"
                + "thu text,"
                + "fri text,"
                + "id integer PRIMARY KEY AUTOINCREMENT)";

        db.execSQL(TABLE_CREATE_meal);
        db.execSQL(TABLE_CREATE_timetable);


        String[] arr = {"period", "mon", "tue", "wed", "thu", "fri"};
        String[] week = {"", "월", "화", "수", "목", "금"};

        //시간표 테이블 값 초기화
        for (int row = 0; row < 7; row++) {
            ContentValues contentValues = new ContentValues();

            for (int column = 0; column < 6; column++) {
                if (row == 0 && column == 0) {
                    contentValues.put(arr[column], "");
                } else if (row == 0) {
                    contentValues.put(arr[column], week[column]);
                } else if (column == 0) {
                    contentValues.put(arr[column], row);
                } else {
                    contentValues.put(arr[column], "");
                }
            }

            db.insert(TABLE_NAME_timetable, null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_meal);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_timetable);
        onCreate(db);
    }
}
