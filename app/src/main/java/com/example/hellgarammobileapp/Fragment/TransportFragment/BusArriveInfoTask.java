package com.example.hellgarammobileapp.Fragment.TransportFragment;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class BusArriveInfoTask extends AsyncTask<Void, Void, String> {
    public static String TransportActivityLog = "TransportActivityLog";

    String SERVICE_KEY = "oMJ1n5M5PZvqvHTfSPehGbytfNmbgdwUDpD60PiYW7PWFf82YBRx25ryzUy4AXhxeJUdBqnQy5UVrVxuzhXk8g%3D%3D";

    boolean inarrmsg1 = false;
    boolean inrtNm = false;

    String arrmsg1 = null;
    String rtNm = null;

    String sumString = "";

    TransportFragment transportFragment;

    public BusArriveInfoTask(TransportFragment transportFragment){
        this.transportFragment = transportFragment;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/"
                    + "getStationByUid?serviceKey=" + SERVICE_KEY
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
                            Log.d(TransportActivityLog, arrmsg1);
                            inarrmsg1 = false;
                        }
                        if (inrtNm) {
                            rtNm = parser.getText();
                            Log.d(TransportActivityLog, rtNm);
                            inrtNm = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            sumString = sumString.concat(arrmsg1 + "\t" + rtNm + "\n");
                            Log.d(TransportActivityLog, sumString);
                        }
                        break;
                }
                parserEvent = parser.next();
            }

            return sumString;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String sumString) {
        super.onPostExecute(sumString);
        transportFragment.setBusInfo(sumString);
    }

}
