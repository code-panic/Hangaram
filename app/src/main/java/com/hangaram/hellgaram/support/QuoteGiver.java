package com.hangaram.hellgaram.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hangaram.hellgaram.Fragment.SettingFragmnet.SettingFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class QuoteGiver {
    private static final String PACKAGE_DIR = "/data/data/com.hangaram.hellgaram/";
    private static final String DATABASE_NAME = "UpdateDatabase.db";
    private static final String TABLE_NAME = "quotes";

    private static int VERSION_CODE = 5;
    SharedPreferences sharedPreferences;

    private static String log = "QuoteGiver";

    public QuoteGiver(Context context, SettingFragment settingFragment) {
        sharedPreferences = context.getSharedPreferences("setting", MODE_PRIVATE);
        initialize(context);

        try {
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            String[] args = {TimeGiver.getMonth(), TimeGiver.getDate()};

            Cursor cursor = db.rawQuery("SELECT month,date,quote,hint From " + TABLE_NAME +
                    " where month = ? AND date = ?", args);

            Log.d(log, "cursor Count: " + cursor.getCount());

            cursor.moveToFirst();

            Log.d(log,"quote: " + cursor.getString(2));
            Log.d(log,"hint: " + cursor.getString(3));

            settingFragment.quote.setText(cursor.getString(2));
            settingFragment.hint.setText(cursor.getString(3));

            cursor.close();
        } catch (Exception e) {

        }
    }

    private void initialize(Context context) {
        File folder = new File(PACKAGE_DIR + "databases");

        if (!folder.exists())
            folder.mkdirs();

        File outfile = new File(PACKAGE_DIR + "databases/" + DATABASE_NAME);

        Log.d(log, "current VersionCode = " + sharedPreferences.getInt("versionCode", 1));
        Log.d(log, "VERSION_CODE = " + VERSION_CODE);

        if (outfile.length() <= 0 || sharedPreferences.getInt("versionCode", 1) != VERSION_CODE) {
            AssetManager assetManager = context.getResources().getAssets();
            try {
                InputStream inputStream = assetManager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);
                long filesize = inputStream.available();
                byte[] tempdata = new byte[(int) filesize];

                inputStream.read(tempdata);
                inputStream.close();

                outfile.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(outfile);
                fileOutputStream.write(tempdata);
                fileOutputStream.close();

                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putInt("versionCode", VERSION_CODE);
                ed.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File list[] = folder.listFiles();

        for(File file: list){
            Log.d(log,"file list: " + file.getName() + "\n");
        }

    }
}
