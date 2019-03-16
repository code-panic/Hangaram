package com.hangaram.hellgaram.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String Tag = "DataBaseHelper";

    private static final String DATABASE_NAME = "HellgaramDatabase.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME_meal = "meal";
    public static final String TABLE_NAME_timetable = "timetable";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        //시간표 테이블 생성
        String TABLE_CREATE_timetable = "create table " + TABLE_NAME_timetable + "("
                + "id integer PRIMARY KEY AUTOINCREMENT,"
                + "mon text,"
                + "tue text,"
                + "wed text,"
                + "thu text,"
                + "fri text)";

        String TABLE_CREATE_meal = "create table " + TABLE_NAME_meal + "("
                + "year integer,"
                + "month integer,"
                + "date integer,"
                + "lunch text,"
                + "dinner text)";

        db.execSQL(TABLE_CREATE_timetable);
        db.execSQL(TABLE_CREATE_meal);

        //시간표 테이블 생성 시 null 지우기
        for (int i = 0; i < 7; i++) {
            String SQL = "insert into " + TABLE_NAME_timetable + "(mon, tue, wed, thu, fri) values ('' , '', '', '', '');";
            db.execSQL(SQL);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(Tag,"oldVersion: " + oldVersion);

        switch (oldVersion){
            case 1:
                String TABLE_CREATE_meal = "create table " + TABLE_NAME_meal + "("
                        + "year integer,"
                        + "month integer,"
                        + "date integer,"
                        + "lunch text,"
                        + "dinner text)";

                db.execSQL(TABLE_CREATE_meal);
        }
    }
}
