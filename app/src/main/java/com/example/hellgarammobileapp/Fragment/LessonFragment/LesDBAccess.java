package com.example.hellgarammobileapp.Fragment.LessonFragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hellgarammobileapp.support.TimeGiver;

public class LesDBAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static LesDBAccess instance;

    private LesDBAccess(Context context){
        this.openHelper = new LesDBAssetHelper(context);
    }

    public static LesDBAccess getInstance(Context context){
        if(instance == null){
            instance = new LesDBAccess(context);
        }
        return instance;
    }

    public void open(){
        this.database = openHelper.getWritableDatabase();
    }

    public void close(){
        if(database == null){
            this.database.close();
        }
    }

    public Cursor getTodayQuote(){

        String month = TimeGiver.getMonth();
        String date = TimeGiver.getDate();

        String SQL = "select month, date, quote, hint "
                + " from quotes"
                + " where month = ? AND date = ?";
        String[] args = {"2", "1"};

        return database.rawQuery("select * from quotes", null);
    }

}
