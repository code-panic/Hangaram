package com.hangaram.hellgaram.Fragment.TransportationFragment;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.hangaram.hellgaram.support.Servicekey;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class BusArriveInfoTask extends AsyncTask<String, Void, ArrayList<VehicleItem>> {
    public static String log = "BusArriveInfoTask";

    private boolean inarrmsg1 = false;
    private boolean inrtNm = false;
    private boolean instNm = false;

    private String arrmsg1 = new String();
    private String rtNm = new String();
    private String stNm = new String();

    private ArrayList<VehicleItem> vehicleItems = new ArrayList<VehicleItem>();

    private StationItemView stationItemView;

    private String minWord = "분";
    private String secWord = "초";

    public BusArriveInfoTask(StationItemView stationItemView){
        this.stationItemView = stationItemView;
    }

    @Override
    protected ArrayList<VehicleItem> doInBackground(String... arsId) {
        try {
            URL url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/"
                    + "getStationByUid?serviceKey=" + Servicekey.SERVICE_KEY
                    + "&arsId=" + arsId[0]);

            StrictMode.enableDefaults();

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("arrmsg1"))
                            inarrmsg1 = true;
                        if (parser.getName().equals("rtNm"))
                            inrtNm = true;
                        if(parser.getName().equals("stNm"))
                            instNm = true;
                        break;
                    case XmlPullParser.TEXT:
                        if (inarrmsg1) {
                            arrmsg1 = parser.getText();
                            inarrmsg1 = false;
                            Log.d(log,arrmsg1);
                        }
                        if (inrtNm) {
                            rtNm = parser.getText();
                            inrtNm = false;
                            Log.d(log,rtNm);
                        }
                        if(instNm){
                            stNm = parser.getText();
                            instNm = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            VehicleItem vehicleItem = new VehicleItem(rtNm,filterArrmsg(arrmsg1));
                            vehicleItems.add(vehicleItem);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
            return vehicleItems;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<VehicleItem> vehicleItems) {
        super.onPostExecute(vehicleItems);
        stationItemView.setBusInfo(vehicleItems);
        stationItemView.setStationNameText(stNm);
        Log.d(log, vehicleItems.get(1).getTransportName());
    }

    private String filterArrmsg(String arrmsg1){
        if(arrmsg1.contains("분")){
            return arrmsg1.substring(0,arrmsg1.indexOf("분")+1);
        }else{
            return arrmsg1;
        }
    }
}
