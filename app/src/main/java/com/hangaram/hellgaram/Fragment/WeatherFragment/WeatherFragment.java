package com.hangaram.hellgaram.Fragment.WeatherFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hangaram.hellgaram.R;

public class WeatherFragment extends Fragment {
    private View view;

    private TextView T1HText;
    private TextView POPText;
    private TextView pmValueText;
    private TextView pmGradeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_weather, container, false);
        init(view.getContext());
        return view;
    }

    public void init(Context context) {
        T1HText = view.findViewById(R.id.T1HText);
        POPText = view.findViewById(R.id.POPText);
        pmValueText = view.findViewById(R.id.pmValueText);
        pmGradeText = view.findViewById(R.id.pmGradeText);

        AirTask airTask = new AirTask(this);
        ForecastWeatherTask forecastWeatherTask = new ForecastWeatherTask(this);
        CurrentWeatherTask currentWeatherTask = new CurrentWeatherTask(this);
        airTask.execute();
        forecastWeatherTask.execute();
        currentWeatherTask.execute();
    }

    public void setT1HText(String T1HString) {
        T1HText.setText(T1HString + "°C");
    }

    public void setPOPText(String POPString) {
        POPText.setText("오후 강수확률 " + POPString + "%");
    }

    public void setPmText(String pm10String, String pm25String) {
        pmValueText.setText("PM10 " + pm10String + "㎍/m³" + "PM25 " + pm25String + "㎍/m³");
    }

    public void setPmGradeText(String pm10Grade1h) {
        switch (pm10Grade1h) {
            case "1":
                pmGradeText.setText("좋음");
                break;
            case "2":
                pmGradeText.setText("보통");
                break;
            case "3":
                pmGradeText.setText("나쁨");
                break;
            case "4":
                pmGradeText.setText("매우 나쁨");
                break;
            default:
                break;
        }
    }
}
