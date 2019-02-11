package com.example.hellgarammobileapp.Fragment.WeatherFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hellgarammobileapp.R;

public class WeatherFragment extends Fragment {
    private View view;

    private TextView pm10Text;
    private TextView pm25Text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_weather, container, false);
        init(view.getContext());
        return view;
    }

    public void init(Context context){
        pm10Text = view.findViewById(R.id.pm10Text);
        pm25Text = view.findViewById(R.id.pm25Text);

        WeatherTask weatherTask = new WeatherTask(this);
        weatherTask.execute();
    }

    public void setPm10Text(String str){
        pm10Text.setText(str);
    }

    public void setPm25Text(String str){
        pm25Text.setText(str);
    }
}
