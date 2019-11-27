package com.hangaram.hellgaram.bus;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class BusTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "BusTask";

    private final String SERVICE_URL = "http://ws.bus.go.kr/api/rest/stationinfo/";
    private final String SERVICE_KEY = "oMJ1n5M5PZvqvHTfSPehGbytfNmbgdwUDpD60PiYW7PWFf82YBRx25ryzUy4AXhxeJUdBqnQy5UVrVxuzhXk8g%3D%3D";

    /*  busList15148: 월촌중학교 전용
     *  busList15154: 목동이대병원 전용 */
    public static ArrayList<HashMap<String, String>> busInfoList15148;
    public static ArrayList<HashMap<String, String>> busInfoList15154;

    private BusCallBack busCallBack;

    public interface BusCallBack {
        void onSuccess(ArrayList<HashMap<String, String>> busList);
        void onFailure();
    }

    public BusTask (BusCallBack busCallBack) {
        this.busCallBack = busCallBack;
    }

    public interface BusApi {
        @GET("getStationByUid")
        Call<BusInfo> getStationByUid(@Query(value = "serviceKey", encoded = true) String key, @Query("arsId") String arsId);
    }

    @Override
    protected Void doInBackground(String... arsId) {
        try {
            BusApi busApi = new Retrofit.Builder()
                    .baseUrl(SERVICE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build()
                    .create(BusTask.BusApi.class);

            Call<BusInfo> busInfoCall = busApi.getStationByUid(SERVICE_KEY, arsId[0]);

            Log.d(TAG, busInfoCall.execute().body().getMsgBody().getItemLists().get(0).getArrmsg1());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getArrmsg (String arrmsg) {
        if (arrmsg.contains("분"))
            return arrmsg.substring(0, arrmsg.indexOf("분") + 1);
        else
            return arrmsg;
    }

    public static String getStamsg (String arrmsg) {
        if (arrmsg.contains("[") && arrmsg.contains("번"))
            return arrmsg.substring(arrmsg.indexOf("[") + 1, arrmsg.indexOf("번")) + "정거장";
        else
            return  "정거장없음";
    }

    public static String getFullFlag (String isFullFlag) {
        if (isFullFlag.equals("0"))
            return  "여유";
        else
            return  "만석";
    }
}
