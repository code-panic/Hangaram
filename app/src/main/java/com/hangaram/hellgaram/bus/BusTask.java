package com.hangaram.hellgaram.bus;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Tag;


public class BusTask {
    private static final String TAG = "BusTask";

    public static final String SERVICE_URL = "http://ws.bus.go.kr/api/rest/stationinfo/";
    public static final String SERVICE_KEY = "oMJ1n5M5PZvqvHTfSPehGbytfNmbgdwUDpD60PiYW7PWFf82YBRx25ryzUy4AXhxeJUdBqnQy5UVrVxuzhXk8g%3D%3D";

    private ArrayList<HashMap<String, String>> busInfoList = new ArrayList<>();

    /*  busList15148: 월촌중학교 전용
     *  busList15154: 목동이대병원 전용 */
    public static ArrayList<HashMap<String, String>> busInfoList15148;
    public static ArrayList<HashMap<String, String>> busInfoList15154;

    public interface BusCall {
        void onSuccess(ArrayList<HashMap<String, String>> busList);
        void onFailure();
    }

    public interface BusApi {
        @GET("getStationByUid")
        Call<ServiceResult> getStationByUid(@Query(value = "serviceKey", encoded = true) String key, @Query("arsId") String arsId);
    }

    public void updateBusList(final String arsId, final BusCall busCall) {
        BusApi busApi;

        busApi = new Retrofit.Builder()
                .baseUrl(SERVICE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(BusTask.BusApi.class);

        try {
            Response<ServiceResult> response = busApi.getStationByUid(SERVICE_KEY, arsId).execute();
            Log.d(TAG, response.body().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        response.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                    factory.setNamespaceAware(true);
//                    XmlPullParser parser = factory.newPullParser();
//
//                    Log.d(TAG, response.body().item);
//
//                    parser.setInput(new StringReader(response.body().string()));
//                    int eventType = parser.getEventType();
//
//                    HashMap<String, String> busInfo = new HashMap<String, String>() {{
//                        put("arrmsg1", null);
//                        put("arrmsg2", null);
//                        put("isFullFlag1", null);
//                        put("isFullFlag2", null);
//                        put("rtNm", null);
//                    }};
//
//                    /*  버스 정보 파싱하기  */
//                    while (eventType != XmlPullParser.END_DOCUMENT) {
//                        if (eventType == XmlPullParser.START_TAG) {
//                            if (busInfo.containsKey(parser.getName())) {
//                                String key = parser.getName();
//
//                                parser.next();
//                                busInfo.put(key, parser.getName());
//                            }
//                        } else if (eventType == XmlPullParser.END_TAG) {
//                            if (parser.getName().equals("itemList")) {
//                                busInfoList.add(busInfo);
//                                Log.d(TAG, "arrmsg1" + busInfo.get("arrmsg1") +"\narrmsg2" + busInfo.get("arrmsg2") +"\nisFullFlag1" + busInfo.get("isFullFlag1") +"IsFullFlag2" + busInfo.get("isFullFlag2"));
//                            }
//                        }
//
//                        eventType = parser.next();
//                    }
//
//                    /*  깊은 복사? 얕은 복사?   */
//                    if(arsId.equals("15148")) {
//                        busInfoList15148 = busInfoList;
//                    } else if (arsId.equals("15154")) {
//                        busInfoList15154 = busInfoList;
//                    }
//
//                    busCall.onSuccess(busInfoList);
//                } catch (XmlPullParserException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                busCall.onFailure();
//            }
//        });
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
