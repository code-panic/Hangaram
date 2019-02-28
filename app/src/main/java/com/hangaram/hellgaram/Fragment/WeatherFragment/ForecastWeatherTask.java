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

public class ForecastWeatherTask extends AsyncTask<Void, Void, Void> {
    private static String log = "ForecastWeatherTask";

    private boolean infcstValue = false;

    private boolean inPOP = false;
    private boolean inSKY = false;
    private boolean inREH = false;
    private boolean inWSD = false;

    private String POP = new String(); //강수확률
    private String SKY = new String(); //하늘상태
    private String REH = new String(); //습도
    private String WSD = new String(); //풍속

    private String base_date = new String();
    private String base_time = new String();

    private WeatherFragment weatherFragment;

    public ForecastWeatherTask(WeatherFragment weatherFragment) {
        this.weatherFragment = weatherFragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        base_date = TimeGiver.getYear() + TimeGiver.getMonth() + TimeGiver.getDate();
        Log.d(log,"base_date: " + base_date);
        calculateBasetime();

        try {
            URL url = new URL("http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?" +
                    "serviceKey=" + Servicekey.SERVICE_KEY +
                    "&base_date=" + base_date +
                    "&base_time=" + base_time +
                    "&nx=" + "58" +
                    "&ny=" + "126" +
                    "&numOfRows=" + "12" +
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
                        if (parser.getName().equals("fcstValue"))
                            infcstValue = true;
                        break;
                    case XmlPullParser.TEXT:
                        if (parser.getText().equals("POP"))
                            inPOP = true;
                        if (parser.getText().equals("SKY"))
                            inSKY = true;
                        if (parser.getText().equals("REH"))
                            inREH = true;
                        if (parser.getText().equals("WSD"))
                            inWSD = true;

                        if (infcstValue) {
                            if (inPOP) {
                                POP = parser.getText();
                                inPOP = false;
                                Log.d(log,"POP:" + POP);
                            }
                            if (inSKY) {
                                SKY = parser.getText();
                                inSKY = false;
                                Log.d(log,"SKY:" + SKY);
                            }
                            if (inREH) {
                                REH = parser.getText();
                                inREH = false;
                                Log.d(log,"REH:" + REH);
                            }
                            if (inWSD) {
                                WSD = parser.getText();
                                inWSD = false;
                                Log.d(log,"WSD:" + WSD);
                            }
                            infcstValue = false;
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
        weatherFragment.setPOPText(POP);
    }

    private String calculateBasetime() {
        for (int i = 0; i < 8; i++) {
            if (Integer.parseInt(TimeGiver.getHour() + TimeGiver.getMinuate()) > (3 * i + 2) * 100 + 10) {
                if(3 * i + 2 >= 10) {
                    base_time = (3 * i + 2) + "00";
                }else {
                    base_time = "0" + (3 * i + 2) + "00";
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

