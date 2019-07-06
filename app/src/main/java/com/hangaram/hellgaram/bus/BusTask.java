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
import java.util.HashMap;


public class BusTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "BusTask";
    private static final String SERVICE_KEY = "oMJ1n5M5PZvqvHTfSPehGbytfNmbgdwUDpD60PiYW7PWFf82YBRx25ryzUy4AXhxeJUdBqnQy5UVrVxuzhXk8g%3D%3D";


    private ArrayList<HashMap<String, String>> busList = new ArrayList<>();

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

    private String lastUpdateTime = "";  /*마지막 업데이트 시간*/

    /*후처리를 다르게 하기 위한 콜백함수 선언*/
    private BusCall mBusCall;

    public interface BusCall {
        void onSuccess(ArrayList<HashMap<String, String>> busList);
    }

    public BusTask(BusCall busCall) {
        mBusCall = busCall;
    }

    @Override
    protected Void doInBackground(String... stationName) {
        try {
            /*월촌중학교 정류장 아이디: 15148
             * 목동이대병원 정류장 아이디: 15154*/
            String arsId;

            if (stationName[0].equals("월촌중학교"))
                arsId = "15148";
            else
                arsId = " 15154";

            URL url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/"
                    + "getStationByUid?serviceKey=" + SERVICE_KEY
                    + "&arsId=" + arsId);

            /*아래 코드를 쓰지 않으면 오류가 터지므로 일단 써둔다*/
            StrictMode.enableDefaults();

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
                            extractArrmsg();

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

                            busList.add(hashMap);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
            return null;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        mBusCall.onSuccess(busList);
    }

    /*
    * arrmsg1 = 7분22초후[1번째 전]
    * arr1 = 7
    * sta1 = 1
    * */
    private void extractArrmsg() {

        /* <arrmsg1>7분22초후[1번째 전]</arrmsg1> */
        arr1 = arrmsg1.substring(0, arrmsg1.indexOf("분") + 1);
        sta1 = arrmsg1.substring(arrmsg1.indexOf("[")+ 1, arrmsg1.indexOf("번"));

        arr2 = arrmsg2.substring(0, arrmsg2.indexOf("분") + 1);
        sta2 = arrmsg2.substring(arrmsg2.indexOf("[")+ 1, arrmsg2.indexOf("번"));
    }
}