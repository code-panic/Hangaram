package com.hangaram.hellgaram.cafeteria;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.hangaram.hellgaram.datebase.DataBaseHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CafeTask extends AsyncTask<Integer, Integer, Void> {
    private static final String TAG = "CafeTask";

    private Context mContext;

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    public CafeTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {

        /*
         * 교육청 (ex. 서울 -> sen.go.kr/ )
         * 요청정보 (ex. 한 달치 급식정보 -> sts_sci_md00_001.do, 학교 스케줄 정보 -> sts_sci_sf01_001.do)
         * sculCode: 학교코드 (ex. 한가람고 -> B100000549)
         * schulCrseScCod: 학교종류 (ex. 고등학교 -> 4)
         * schulKndScCode: 학교종류 (ex. 고등학교 -> 04)
         * schYm: 요청하는 달 (ex. 201906)
         */

        mDataBaseHelper = new DataBaseHelper(mContext);
        mDatabase = mDataBaseHelper.getReadableDatabase();

        String urlString = "https://stu."
                + "sen.go.kr/"
                + "sts_sci_md00_001.do" + "?"
                + "schulCode=B100000549"
                + "&schulCrseScCode=4"
                + "&schulKndScCode=04"
                + "&schYm=" + params[0] + String.format("%02d", params[1]);

        try {
            Document doc = Jsoup.connect(urlString).get();
            Elements tds = doc.getElementsByTag("td");

            int date = 1;

            for (Element td : tds) {
                /*td 태그 중 일부는 비어 있어 의미 없는 태그가 있으므로 걸러준다*/
                if (!td.text().equals("")) {
                    String[] spilts = td.text().split(" ");

                    boolean isLunch = false;
                    boolean isDinner = false;

                    StringBuilder lunchText = new StringBuilder();
                    StringBuilder dinnerText = new StringBuilder();

                    /*점심, 저녁 정보 가져오기*/
                    for (String split : spilts) {
                        if (split.contains("중식")) {
                            isLunch = true;
                            continue;
                        } else if (split.contains("석식")) {
                            isDinner = true;
                            continue;
                        }

                        if (isLunch && !isDinner) {
                            lunchText.append(split).append("\n");
                        } else if (isLunch && isDinner) {
                            dinnerText.append(split).append("\n");
                        }
                    }

                    Log.d(TAG, params[0] + "." + params[1] + "." + date + "\n" + "---중식---\n" + lunchText.toString() + "---석식---\n" + dinnerText.toString());

                    /*데이터베이스에 저장하기*/
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("year", params[0]);
                    contentValues.put("month", params[1]);
                    contentValues.put("date", date++);
                    contentValues.put("lunch", lunchText.toString());
                    contentValues.put("dinner", dinnerText.toString());

                    mDatabase.insert(DataBaseHelper.TABLE_NAME_MEAL, null, contentValues);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
