package com.hangaram.hellgaram.bus;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class BusTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "BusTask";
    private static final String SERVICE_KEY = "oMJ1n5M5PZvqvHTfSPehGbytfNmbgdwUDpD60PiYW7PWFf82YBRx25ryzUy4AXhxeJUdBqnQy5UVrVxuzhXk8g%3D%3D";

    private ArrayList<HashMap<String, String>> busList = new ArrayList<>();

    private String stNm;

    /*busList1: 월촌중학교 전용
     *busList2: 목동이대병원 전용*/
    public static ArrayList<HashMap<String, String>> busList1;
    public static ArrayList<HashMap<String, String>> busList2;


    private String arrmsg1 = "";    /*첫번째 버스 도착정보*/
    private String arrmsg2 = "";    /*두번째 버스 도착정보*/
    private String busType1 = "";   /*첫번째 버스 타입 (0: 일반버스, 1: 저상버스, 2: 굴절버스)*/
    private String busType2 = "";   /*두번째 버스 타입 (0: 일반버스, 1: 저상버스, 2: 굴절버스)*/
    private String isFullFlag1 = "";    /*첫번쨰 만차정보 (0: 만차 아님, 1: 만차)*/
    private String isFullFlag2 = "";    /*두번째 만차정보 (0: 만차 아님, 1: 만차)*/
    private String rtNm = "";    /*버스이름*/

    private boolean inarrmsg1 = false;
    private boolean inarrmsg2 = false;
    private boolean inbusType1 = false;
    private boolean inbusType2 = false;
    private boolean inisFullFlag1 = false;
    private boolean inisFullFlag2 = false;
    private boolean inrtNm = false;

    private String arr1 = "";   /*첫번째 버스 도착예정시간(분)*/
    private String sta1 = "";   /*첫번째 버스 남은 버스정거장*/
    private String arr2 = "";   /*두번째 버스 도착예정시간(분)*/
    private String sta2 = "";   /*두번째 버스 남은 버스정거장*/

    /*후처리를 다르게 하기 위한 콜백함수 선언*/
    private BusCall mBusCall;

    public interface BusCall {
        void onSuccess(ArrayList<HashMap<String, String>> busList);
        void onFailure();
    }

    public BusTask(BusCall busCall, String stNm) {
        mBusCall = busCall;
        this.stNm = stNm;
    }

    @Override
    protected Boolean doInBackground(String... stationName) {
        try {
            /*월촌중학교 정류장 아이디: 15148
             * 목동이대병원 정류장 아이디: 15154*/
            String arsId;

            if (stNm.equals("월촌중학교"))
                arsId = "15148";
            else
                arsId = "15154";

            URL url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/"
                    + "getStationByUid?serviceKey=" + SERVICE_KEY
                    + "&arsId=" + arsId);

            /*아래 코드를 쓰지 않으면 오류가 터지므로 일단 써둔다*/
//            StrictMode.enableDefaults();

            /*xml 파싱에 대한 객체 불러오기*/
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            /*버스 정보 파싱하기*/
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {

                    /*지정한 태그의 boolean 값을 true 로*/
                    case XmlPullParser.START_TAG:

                        if (parser.getName().equals("arrmsg1"))
                            inarrmsg1 = true;

                        if (parser.getName().equals("arrmsg2"))
                            inarrmsg2 = true;

                        if (parser.getName().equals("busType1"))
                            inbusType1 = true;

                        if (parser.getName().equals("busType2"))
                            inbusType2 = true;

                        if (parser.getName().equals("isFullFlag1"))
                            inisFullFlag1 = true;

                        if (parser.getName().equals("isFullFlag2"))
                            inisFullFlag2 = true;

                        if (parser.getName().equals("rtNm"))
                            inrtNm = true;

                        break;


                    /*태그의 텍스트 가져오기*/
                    case XmlPullParser.TEXT:

                        if (inarrmsg1) {
                            arrmsg1 = parser.getText();
                            inarrmsg1 = false;
                        }

                        if (inarrmsg2) {
                            arrmsg2 = parser.getText();
                            inarrmsg2 = false;
                        }

                        if (inbusType1) {
                            busType1 = parser.getText();
                            inbusType1 = false;
                        }

                        if (inbusType2) {
                            busType2 = parser.getText();
                            inbusType2 = false;
                        }

                        if (inisFullFlag1) {
                            isFullFlag1 = parser.getText();
                            inisFullFlag1 = false;
                        }

                        if (inisFullFlag2) {
                            isFullFlag2 = parser.getText();
                            inisFullFlag2 = false;
                        }

                        if (inrtNm) {
                            rtNm = parser.getText();
                            inrtNm = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            /*데이터 가공*/
                            extractArrmsg();

                            /*HashMap 에 저장*/
                            HashMap<String, String> hashMap = new HashMap<>();

                            hashMap.put("arr1", arr1);
                            hashMap.put("arr2", arr2);
                            hashMap.put("sta1", sta1);
                            hashMap.put("sta2", sta2);
                            hashMap.put("busType1", busType1);
                            hashMap.put("busType2", busType2);
                            hashMap.put("isFullFlag1", isFullFlag1);
                            hashMap.put("isFullFlag2", isFullFlag2);
                            hashMap.put("rtNm", rtNm);

                            /*마지막 업데이트 시간 저장하기*/
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DATE, 0);

                            hashMap.put("lastUpdateTime", calendar.get(Calendar.HOUR) + "시 " + calendar.get(Calendar.MINUTE) + "분");

                            busList.add(hashMap);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
            return true;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);

        if (isSuccess)
            mBusCall.onSuccess(busList);
        else
            mBusCall.onFailure();

        saveBusList(busList);

    }

    /*
     * arrmsg1 = 7분22초후[1번째 전]
     * -> arr1 = 7
     * -> sta1 = 1
     *
     * isFullFlag1 = 0
     * -> isFullFlag1 = 여유
     * */
    private void extractArrmsg() {
        /* <arrmsg1>7분22초후[1번째 전]</arrmsg1> */
        if (arrmsg1.contains("분"))
            arr1 = arrmsg1.substring(0, arrmsg1.indexOf("분") + 1);
        else
            arr1 = arrmsg1;

        if (arrmsg2.contains("분"))
            arr2 = arrmsg2.substring(0, arrmsg2.indexOf("분") + 1);
        else
            arr2 = arrmsg2;

        if (arrmsg1.contains("[") && arrmsg1.contains("번"))
            sta1 = arrmsg1.substring(arrmsg1.indexOf("[") + 1, arrmsg1.indexOf("번")) + "정거장";
        else
            sta1 = "정거장없음";

        if (arrmsg2.contains("[") && arrmsg2.contains("번"))
            sta2 = arrmsg2.substring(arrmsg2.indexOf("[") + 1, arrmsg2.indexOf("번")) + "정거장";
        else
            sta2 = "정거장없음";

        if (isFullFlag1.equals("0"))
            isFullFlag1 = "여유";
        else
            isFullFlag1 = "만석";

        if (isFullFlag2.equals("0"))
            isFullFlag2 = "여유";
        else
            isFullFlag2 = "만석";
    }

    /*busList 저장하기*/
    private void saveBusList(ArrayList<HashMap<String, String>> busList) {
        /*디폴트로 업데이트 창이 뜬다*/
        if (stNm.equals("월촌중학교"))
            busList1 = busList;
        else if (stNm.equals("목동이대병원"))
            busList2 = busList;
    }
}
