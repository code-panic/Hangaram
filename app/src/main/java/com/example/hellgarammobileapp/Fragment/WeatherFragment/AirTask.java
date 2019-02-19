package com.example.hellgarammobileapp.Fragment.WeatherFragment;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.hellgarammobileapp.support.Servicekey;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;

public class AirTask extends AsyncTask<Void, Void, Void> {
    private static String log = "AirTask";

    private boolean inso2Value = false;
    private boolean incoValue = false;
    private boolean ino3Value = false;
    private boolean inno2Value = false;
    private boolean inpm10Value = false;
    private boolean inpm25Value = false;
    private boolean inso2Grade = false;
    private boolean inco2Grade = false;
    private boolean ino3Grade = false;
    private boolean inno2Grade = false;
    private boolean inpm10Grade1h = false;
    private boolean inkahiValue = false;
    private boolean inkahiGrade = false;

    private String so2Value = new String(); //아황산가스 농도
    private String coValue = new String(); //일산화탄소 농도
    private String o3Value = new String(); //오존 농도
    private String no2Value = new String(); //이산화질소 농도
    private String pm10Value = new String(); //미세먼지 농도
    private String pm25Value = new String(); //초미세먼지 농도
    private String KhaiValue = new String(); //통합대기환경 수치
    private String so2Grade = new String(); //아황산가스 농도
    private String coGrade = new String(); //일산화탄소 농도
    private String o3Grade = new String(); //오존 농도
    private String no2Grade = new String(); //이산화질소 농도
    private String pm10Grade1h = new String(); //미세먼진 지수
    private String pm25Grade1h = new String(); //초미세먼지 지수
    private String KhaiGrade = new String(); //통합대기환경 지수

    private WeatherFragment weatherFragment;

    public AirTask(WeatherFragment weatherFragment) {
        this.weatherFragment = weatherFragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?"
                    + "serviceKey=" + Servicekey.SERVICE_KEY
                    + "&numOfRows=" + "1"
                    + "&pageNo=" + "1"
                    + "&stationName=" + "양천구"
                    + "&dataTerm=" + "DAILY"
                    + "&ver=" + "1.3");

            StrictMode.enableDefaults();

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("pm10Value"))
                            inpm10Value = true;
                        if (parser.getName().equals("pm25Value"))
                            inpm25Value = true;
                        if (parser.getName().equals("pm10Grade1h"))
                            inpm10Grade1h = true;
                        break;
                    case XmlPullParser.TEXT:
                        if (inpm10Value) {
                            pm10Value = parser.getText();
                            inpm10Value = false;
                            Log.d(log, "pm10Value: " + pm10Value);
                        }
                        if (inpm25Value) {
                            pm25Value = parser.getText();
                            inpm25Value = false;
                            Log.d(log, "pm25Value: " + pm25Value);
                        }
                        if (inpm10Grade1h) {
                            pm10Grade1h = parser.getText();
                            inpm10Grade1h = false;
                            Log.d(log, "pm10Grade1h: " + pm10Grade1h);
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
        weatherFragment.setPmText(pm10Value, pm25Value);
        weatherFragment.setPmGradeText(pm10Grade1h);
    }
}
