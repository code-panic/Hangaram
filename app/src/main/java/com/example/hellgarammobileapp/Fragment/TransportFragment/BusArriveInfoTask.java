package com.example.hellgarammobileapp.Fragment.TransportFragment;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.hellgarammobileapp.support.Servicekey;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class BusArriveInfoTask extends AsyncTask<Void, Void, ArrayList<TransportItem>> {
    public static String log = "BusArriveInfoTask";

    private boolean inarrmsg1 = false;
    private boolean inrtNm = false;

    private String arrmsg1 = new String();
    private String rtNm = new String();

    private ArrayList<TransportItem> transportItems = new ArrayList<TransportItem>();
    private TransportFragment transportFragment;

    public BusArriveInfoTask(TransportFragment transportFragment){
        this.transportFragment = transportFragment;
    }

    @Override
    protected ArrayList<TransportItem> doInBackground(Void... voids) {
        try {
            URL url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/"
                    + "getStationByUid?serviceKey=" + Servicekey.SERVICE_KEY
                    + "&arsId=15148");

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
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            TransportItem transportItem = new TransportItem(rtNm,arrmsg1);
                            transportItems.add(transportItem);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
            return transportItems;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<TransportItem> transportItems) {
        super.onPostExecute(transportItems);
        transportFragment.setBusInfo(transportItems);
        Log.d(log,transportItems.get(1).getTransportName());
    }
}
