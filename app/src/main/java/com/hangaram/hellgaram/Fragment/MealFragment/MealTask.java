package com.hangaram.hellgaram.Fragment.MealFragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.hangaram.hellgaram.support.DataBaseHelper;
import com.hangaram.hellgaram.support.TimeGiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MealTask extends AsyncTask<Void, Void, JSONObject> {
    private static final String Tag = "MealTask";
    private static final String Schoolcode = "B100000549"; //한가람고 학교코드

    private String urlText;

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private int gap;
    private Context context;

    public MealTask(int gap, Context context) {
        this.gap = gap;
        this.context = context;

        Log.d(Tag, "TimeGiver.getYear: " + TimeGiver.getYear(gap));
        Log.d(Tag, "TimeGiver.getMonth: " + TimeGiver.getMonth(gap));
        Log.d(Tag, "TimeGiver.getDate: " + TimeGiver.getDate(gap));

        urlText = "https://schoolmenukr.ml/api/high/" + Schoolcode
                + "?year=" + TimeGiver.getYear(gap)
                + "&month=" + TimeGiver.getMonth(gap)
                + "&date=" + TimeGiver.getDate(gap);
    }


    @Override
    protected JSONObject doInBackground(Void... voids) {
        BufferedReader bufferedReader = null;

        //급식 api에 연결시도
        try {
            URL url = new URL(urlText);
            URLConnection urlConnectionn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnectionn.getInputStream()));

            StringBuilder stringBuffer = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return new JSONObject(stringBuffer.toString());
        } catch (Exception ex) {
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        if (response != null) {
            try {
                //Json 파일내용 정리
                JSONObject menu = (JSONObject) response.get("menu");

                JSONArray lunchJSONArray = (JSONArray) menu.get("lunch");
                JSONArray dinnerJSONArray = (JSONArray) menu.get("dinner");

                String lunch = filterJsonArray(lunchJSONArray);
                String dinner = filterJsonArray(dinnerJSONArray);

                //급식이 없을 때 문자열 설정
                if (lunch.isEmpty())
                    lunch = "급식 없음";

                if (dinner.isEmpty())
                    dinner = "급식 없음";

                Log.d(Tag, "lunch: " + lunch);
                Log.d(Tag, "dinner: " + dinner);

                openDataBase(context);

                String SQL = "insert into " + DataBaseHelper.TABLE_NAME_meal + " values (" +
                        TimeGiver.getYear(gap) + ", " +
                        TimeGiver.getMonth(gap) + ", " +
                        TimeGiver.getDate(gap) + ", " +
                        "'" + lunch + "', " +
                        "'" + dinner + "')";

                sqLiteDatabase.execSQL(SQL);
            } catch (JSONException ex) {
                Log.e("MealTask", "Failure", ex);
            }
        }
    }

    public String filterJsonArray(JSONArray arr) {
        //정보 추출하기
        String sumString = new String();

        try {
            for (int i = 0; i < arr.length(); i++) {
                for (int j = 0; j < arr.getString(i).length(); j++) {
                    char charAt = arr.getString(i).charAt(j);

                    if ((charAt >= '1' && charAt <= '9') || charAt == '.' || charAt == '*') {
                        sumString += arr.getString(i).substring(0, j) + "\n";
                        break;
                    } else if (j == arr.getString(i).length() - 1) {
                        if(i == arr.length() - 1)
                            sumString += arr.getString(i);
                        else
                            sumString += arr.getString(i) + "\n";
                        break;
                    }

                }
            }
        } catch (JSONException e) {
            Log.d(Tag, e.getMessage());
        }

        return sumString;
    }

    private void openDataBase(Context context) {
        //데이터 베이스 준비
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
    }
}
