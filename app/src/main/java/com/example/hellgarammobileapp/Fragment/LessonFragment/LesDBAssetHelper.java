package com.example.hellgarammobileapp.Fragment.LessonFragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hellgarammobileapp.support.TimeGiver;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LesDBAssetHelper extends SQLiteAssetHelper {
    private static final String LesDBAssetHelper = "LesDBAssetHelper";

    private static final String DATABASE_NAME = "UpdateDatabase.db";
    private static final int DATABASE_VERSION = 2;

    public LesDBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


}
