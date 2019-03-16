package com.hangaram.hellgaram.Fragment.MealFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.hangaram.hellgaram.support.DataBaseHelper;

public class MealReceiver extends BroadcastReceiver {
    private static final String Tag = "MealReceiver";

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        Toast.makeText(context,"Light",Toast.LENGTH_SHORT);

        //인터넷이 연결된 경우
        if (networkInfo != null) {
            openDataBase(context);

            //데이터 베이스 정보 삭제
            String DELETE_ALL_meal = "DELETE FROM " + DataBaseHelper.TABLE_NAME_meal;
            sqLiteDatabase.execSQL(DELETE_ALL_meal);

            //데이터 베이스 정보 추가
            for(int gap = -4; gap < 5; gap++){
                MealTask mealTask = new MealTask(gap,context);
                mealTask.execute();
            }

            Log.d(Tag,"Load MealInfo");
        }
    }

    //데이터 베이스 준비
    private void openDataBase(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
    }
}
