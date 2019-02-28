package com.hangaram.hellgaram.Fragment.WeatherFragment;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.hangaram.hellgaram.support.Servicekey;
import com.hangaram.hellgaram.support.TimeGiver;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;

public class CurrentWeatherTask extends AsyncTask<Void, Void, Void>{
    private static String log = "CurrentWeatherTask";

    private boolean inobsrValue = false;

    private boolean inT1H = false;
    private boolean inREH = false;

    private String T1H = new String(); //온도
    private String REH = new String(); //습도

    private String base_date = new String();
    private String base_time = new String();

    private WeatherFragment weatherFragment;

    public CurrentWeatherTask(WeatherFragment weatherFragment) {
        this.weatherFragment = weatherFragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        base_date = TimeGiver.getYear() + TimeGiver.getMonth() + TimeGiver.getDate();
        Log.d(log,"base_date: " + base_date);
        calculateBasetime();

        try {
            URL url = new URL("http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?" +
                    "serviceKey=" + Servicekey.SERVICE_KEY +
                    "&base_date=" + base_date +
                    "&base_time=" + base_time +
                    "&nx=" + "58" +
                    "&ny=" + "126" +
                    "&numOfRows=" + "8" +
                    "&pageNo=" + "1" +
                    "&_type=xml");

            StrictMode.enableDefaults();

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("obsrValue"))
                            inobsrValue = true;
                        break;
                    case XmlPullParser.TEXT:
                        if (parser.getText().equals("T1H"))
                            inT1H = true;
                        if (parser.getText().equals("REH"))
                            inREH = true;

                        if (inobsrValue) {
                            if (inT1H) {
                                T1H = parser.getText();
                                inT1H = false;
                                Log.d(log,"T1H: " + T1H);
                            }
                            if (inREH) {
                                REH = parser.getText();
                                inREH = false;
                                Log.d(log,"REH: " + REH);
                            }
                            inobsrValue = false;
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
        weatherFragment.setT1HText(T1H);
    }

    private String calculateBasetime() {
        for (int i = 0; i < 24; i++) {
            if (Integer.parseInt(TimeGiver.getHour() + TimeGiver.getMinuate()) > i * 100 + 40) {
                if(i >= 10) {
                    base_time = i + "00";
                }else {
                    base_time = "0" + i + "00";
                }
            }
        }

        if(base_time == null){
            base_date = TimeGiver.getYesterdayYear() + TimeGiver.getYesterdayMonth() + TimeGiver.getYesterdayDate();
            return "2300";
        }

        Log.d(log,"base_time: " + base_time);
        return base_time;
    }
}

