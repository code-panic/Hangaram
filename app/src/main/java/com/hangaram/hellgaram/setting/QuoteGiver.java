package com.hangaram.hellgaram.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hangaram.hellgaram.time.TimeGiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class QuoteGiver {
    private static final String TAG = "QuoteGiver";

    private static final String PACKAGE_DIR = "data/com.hangaram.hellgaram/";
    private static final String DATABASE_NAME = "UpdateDatabase.db";
    private static final String TABLE_NAME = "quotes";

    private static int VERSION_CODE = 5;
    private SharedPreferences mCurrentVersion;

    private String mQuote;
    private String mHint;

    public QuoteGiver(Context context) {
        mCurrentVersion = context.getSharedPreferences("setting", MODE_PRIVATE);
        init(context);

        try {
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            String[] args = {TimeGiver.getMonth(), TimeGiver.getDate()};

            Cursor cursor = db.rawQuery("SELECT month,date,quote,hint From " + TABLE_NAME +
                    " where month = ? AND date = ?", args);

            cursor.moveToFirst();

            mQuote = cursor.getString(cursor.getColumnIndex("quote"));
            mHint = cursor.getString(cursor.getColumnIndex("hint"));

            cursor.close();
        } catch (Exception e) {

        }
    }

    private void init(Context context) {

        //데이터베이스 폴더 없을 시 만들기
        File folder = new File(PACKAGE_DIR + "databases");

        if (!folder.exists())
            folder.mkdirs();

        //데이터베이스 있는지 확인하기
        File outfile = new File(PACKAGE_DIR + "databases/" + DATABASE_NAME);

        //처음 데이터베이스를 깔거나 버전코드 변경시 실행
        if (outfile.length() <= 0 || mCurrentVersion.getInt("VERSION_CODE", 1) != VERSION_CODE) {
            AssetManager assetManager = context.getResources().getAssets();

            try {
                //asset 폴더에서 데이터베이스 InputStream 으로 가져오기
                InputStream inputStream = assetManager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);
                long fileSize = inputStream.available();
                byte[] tempData = new byte[(int) fileSize];

                inputStream.read(tempData);
                inputStream.close();

                //파일이 없을 시 생성하고 있을 경우 덮어쓴다.
                outfile.createNewFile();

                //OutStream 으로 보내기
                FileOutputStream fileOutputStream = new FileOutputStream(outfile);
                fileOutputStream.write(tempData);
                fileOutputStream.close();

                //현재 버전 저장하기
                SharedPreferences.Editor editor = mCurrentVersion.edit();
                editor.putInt("VERSION_CODE", VERSION_CODE);
                editor.apply();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getQuote() {
        return mQuote;
    }

    public String getHint() {
        return mHint;
    }
}
