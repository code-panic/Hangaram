package com.example.hellgarammobileapp.Fragment.MealFragment;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MealTask extends AsyncTask<String, Void, JSONObject> {

    MealFragment mealFragment;

    public MealTask(MealFragment mealFragment) {
        this.mealFragment = mealFragment;
    }

    @Override
    protected JSONObject doInBackground(String... str) {
        URLConnection urlConn;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str[0]);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
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
                JSONObject menu = (JSONObject) response.get("menu");

                JSONArray lunchJSONArray = (JSONArray) menu.get("lunch");
                JSONArray dinnerJSONArray = (JSONArray) menu.get("dinner");

                String lunchMenu = new String();
                String dinnerMenu = new String();

                lunchMenu = filterJsonArray(lunchJSONArray);
                dinnerMenu = filterJsonArray(dinnerJSONArray);

                mealFragment.setLunchMenu(lunchMenu);
                mealFragment.setDinnerMenu(dinnerMenu);
            } catch (JSONException ex) {
                Log.e("MealTask", "Failure", ex);
            }
        }
    }

    public String filterJsonArray(JSONArray arr) throws JSONException { //알레르기 정보를 걸러주느 함수
        String subString = new String();
        String sumString = new String();

        for (int i = 0; i < arr.length(); i++) {
            Log.e("MealTask", "Success: " + arr.getString(i));

            for (int j = 0; j < arr.getString(i).length(); j++) {
                char charAt = arr.getString(i).charAt(j);
                if ((charAt >= '1' && charAt <= '9') || charAt == '.' || charAt == '*') {
                    subString = arr.getString(i).substring(0, j);
                    break;
                } else if (j == arr.getString(i).length() - 1) {
                    subString = arr.getString(i).substring(0, j + 1);
                    break;
                }
            }
            sumString = sumString + subString + "\n";
        }
        return sumString;
    }
}
