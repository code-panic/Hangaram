package com.example.hellgarammobileapp.Fragment.WeatherFragment;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.hellgarammobileapp.support.Servicekey;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;

public class WeatherTask extends AsyncTask<Void,Void,Void> {
    private static String Log = "WeatherTask";

    private boolean inpm10Grade1h = false;
    private boolean inpm25Grade1h = false;

    private String pm10Grade1h = new String();
    private String pm25Grade1h = new String();

    private WeatherFragment weatherFragment;

    public WeatherTask(WeatherFragment weatherFragment){
        this.weatherFragment = weatherFragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?"
                    +"serviceKey=" + Servicekey.SERVICE_KEY
                    +"&numOfRows=" + "1"
                    +"&pageNo=" + "1"
                    +"&stationName=" + "%EC%96%91%EC%B2%9C%EA%B5%AC"
                    +"&dataTerm=" + "DAILY"
                    +"&ver=" + "1.3");

            StrictMode.enableDefaults();

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("pm10Grade1h"))
                            inpm10Grade1h = true;
                        if (parser.getName().equals("pm25Grade1h"))
                            inpm25Grade1h = true;
                        break;
                    case XmlPullParser.TEXT:
                        if (inpm10Grade1h) {
                            pm10Grade1h = parser.getText();
                            inpm10Grade1h = false;
                        }
                        if (inpm25Grade1h) {
                            pm25Grade1h = parser.getText();
                            inpm25Grade1h = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                parserEvent = parser.next();
            }
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
        weatherFragment.setPm10Text(pm10Grade1h);
        weatherFragment.setPm25Text(pm25Grade1h);
    }
}
